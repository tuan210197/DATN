package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.LocalDate;
import shupship.domain.dto.CommonCodeResponseDto;
import shupship.domain.model.*;
import shupship.dto.ResultLeadResponse;
import shupship.dto.ScheduleResponseDto;
import shupship.enums.LeadStatus;
import shupship.enums.ScheduleStatus;
import shupship.repo.*;
import shupship.request.ScheduleRequest;
import shupship.response.ScheduleLstResponse;
import shupship.service.ICommonService;
import shupship.service.ScheduleService;
import shupship.util.DateTimeUtils;
import shupship.util.exception.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static shupship.util.DateTimeUtils.validateOverlapTime;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private ILeadRepository leadRepository;

    @Autowired
    private ILeadAssignRepository leadAssignRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private BasicLoginRepo basicLoginRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ICommonService commonService;

    @Override
    public Schedule createSchedule(ScheduleRequest inputData) throws Exception {

        LocalDateTime fromDate = DateTimeUtils.StringToLocalDateTime(inputData.getFromDate());
        LocalDateTime toDate = DateTimeUtils.StringToLocalDateTime(inputData.getToDate());

        Users user = getCurrentUser();
        Lead lead = leadRepository.findLeadById(inputData.getLeadId());

        if (lead == null) {
            throw new HieuDzException("Khách hàng không tồn tại");
        }
        validateSchedule(fromDate, toDate);

        List<Schedule> schedules = scheduleRepository.getSchedulesByUserId(user.getEmpSystemId());
        validateOverlapTime(fromDate, toDate, schedules);
        Schedule schedule = scheduleService.getLatestScheduleByUserIdAndLeadId(user.getEmpSystemId(), inputData.getLeadId());

        if (schedule != null) {
            schedule.setIsLatest(0);
            scheduleRepository.save(schedule);
        }

        Schedule data = new Schedule();

        data.setLead(lead);
        data.setFromDate(fromDate);
        data.setToDate(toDate);
        data.setStatus(ScheduleStatus.NEW.getType());
        data.setIsLatest(1);
        data.setIsLatestResult(0);
        data.setNextScheduleId(inputData.getNextScheduleId());
        data.setCreatedBy(user.getEmpSystemId());
        Schedule outData = scheduleRepository.save(data);

        if (lead.getStatus() == 1 || lead.getStatus() == 5) {
            lead.setStatus(LeadStatus.CONTACTING.getType());
            leadRepository.save(lead);
        }

        LeadAssign leadAssign = leadAssignRepository.getLeadAssignById(lead.getId());
        if (leadAssign == null)
            throw new HieuDzException("Khách hàng chưa dc giao tiếp xúc");
        if (leadAssign.getStatus() == 5 || leadAssign.getStatus() == 4) {
            leadAssign.setStatus(2L);
            leadAssignRepository.save(leadAssign);
        }
        return outData;
    }

    @Override
    public Schedule updateSchedule(ScheduleRequest inputData, Long id) throws Exception {
        Users user = getCurrentUser();
        Schedule existData = scheduleRepository.getScheduleById(id, user.getEmpSystemId());
        if (existData == null) {
            throw new HieuDzException("Lịch không tồn tại");
        }

        LocalDateTime fromDate = DateTimeUtils.StringToLocalDateTime(inputData.getFromDate());
        LocalDateTime toDate = DateTimeUtils.StringToLocalDateTime(inputData.getToDate());

        validateScheduleUpdate(fromDate, toDate);
        List<Schedule> schedules = scheduleRepository.getSchedulesByUserId(user.getEmpSystemId()).stream().filter(e -> e.getId() != existData.getId()).collect(Collectors.toList());
        validateOverlapTime(fromDate, toDate, schedules);

        existData.setToDate(toDate);
        existData.setFromDate(fromDate);
        Schedule outData = scheduleRepository.save(existData);
        return outData;
    }

    public Users getCurrentUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        Users users = userRepo.findByUid(basicLogin.getUserUid());
        if (users == null) {
            throw new ApplicationException("Users is null");
        }
        return users;
    }


    @Override
    public Schedule getLatestScheduleByUserIdAndLeadId(Long sysId, Long leadId) throws BusinessException {
        return scheduleRepository.getLatestScheduleByUserIdAndLeadId(sysId, leadId);
    }

    @Override
    public Page<ScheduleLstResponse> findAllSchedulesPageable(Pageable pageable, LocalDateTime date, LocalDateTime end) {
        Page<Schedule> schedulePage = Page.empty();
        Users users = getCurrentUser();
        if (date != null || end != null) {
            Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
            schedulePage = scheduleRepository.findAll(pageRequest, date, end, users.getEmpSystemId());
        } else {
            schedulePage = scheduleRepository.findAllScheduler(pageable);
        }
        Page<ScheduleLstResponse> scheduleResponseLeadDtoPage = schedulePage.map(ScheduleLstResponse::scheduleModelToDto);

        for (ScheduleLstResponse s : scheduleResponseLeadDtoPage) {
            ResultLeadResponse resultLeadResponse = s.getResult();
            if (resultLeadResponse == null) {
                continue;
            }
            s.setResult(resultLeadResponse);
        }

        return scheduleResponseLeadDtoPage;
    }

    private CommonCodeResponseDto getStatusDescription(Long statusId) {
        return commonService.findCommonCodeByClassCdAndExtValue("RESULT_STATUS", statusId);
    }

    private CommonCodeResponseDto getTypeDescription(Long priceId) {
        return commonService.findCommonCodeByClassCdAndExtValue("PRICE_TYPE", priceId);
    }

    @Override
    public ScheduleResponseDto detailSchedule(Long id) throws Exception {
        Users users = getCurrentUser();
        Schedule existData = scheduleRepository.getScheduleById(id, users.getEmpSystemId());
        if (existData == null) {
            throw new HieuDzException("Lịch không tồn tại");
        }
        ScheduleResponseDto scheduleResponseDto = ScheduleResponseDto.scheduleModelToDto(existData);
        return scheduleResponseDto;
    }

    private void validateSchedule(LocalDateTime fromDate, LocalDateTime toDate) {
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));
        if (fromDate.plusMinutes(5).isBefore(now) || toDate.isBefore(now)) {
            throw new HieuDzException("Không thể đặt thời gian trong quá khứ");
        }
        if (toDate.isBefore(fromDate)) {
            throw new HieuDzException("Ngày kết thúc không thể trước ngày bắt đầu");
        }
        if (fromDate.until(toDate, ChronoUnit.MINUTES) < 30) {
            throw new HieuDzException("Thời gian tiếp xúc cách nhau ít nhất 30 phút");
        }
    }

    private void validateScheduleUpdate(LocalDateTime fromDate, LocalDateTime toDate) {
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));
        if (toDate.isBefore(now)) {
            throw new HieuDzException("Không thể đặt thời gian trong quá khứ");
        }
        if (toDate.isBefore(fromDate)) {
            throw new HieuDzException( "Ngày kết thúc không thể trước ngày bắt đầu");
        }
        if (fromDate.until(toDate, ChronoUnit.MINUTES) < 30) {
            throw new HieuDzException("Thời gian tiếp xúc cách nhau ít nhất 30 phút");
        }
    }
}
