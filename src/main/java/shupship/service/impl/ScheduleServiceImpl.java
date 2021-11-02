package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import shupship.domain.model.Lead;
import shupship.domain.model.Users;
import shupship.enums.LeadStatus;
import shupship.enums.ScheduleStatus;
import shupship.repo.ILeadRepository;
import shupship.repo.IScheduleRepository;
import shupship.request.ScheduleRequest;
import shupship.domain.model.Schedule;
import shupship.service.ScheduleService;
import shupship.util.DateTimeUtils;
import shupship.util.exception.BusinessException;
import shupship.util.exception.ErrorMessage;
import shupship.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static shupship.util.CommonUtils.getCurrentUser;
import static shupship.util.DateTimeUtils.validateOverlapTime;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private ILeadRepository leadRepository;

    @Autowired
    private ScheduleService scheduleService;


    @Override
    public Schedule createSchedule(ScheduleRequest inputData) throws Exception {

        LocalDateTime fromDate = DateTimeUtils.StringToLocalDateTime(inputData.getFromDate());
        LocalDateTime toDate = DateTimeUtils.StringToLocalDateTime(inputData.getToDate());

        Users user = getCurrentUser();
        Lead lead = leadRepository.findLeadById(inputData.getLeadId());

        if (lead == null) {
            throw new NotFoundException(new ErrorMessage("ERR_004", "Khách hàng không tồn tại."));
        }
        validateSchedule(fromDate, toDate);

        List<Schedule> schedules = scheduleRepository.getSchedulesByUserId(user.getUid());
        validateOverlapTime(fromDate, toDate, schedules);
        Schedule schedule = scheduleService.getLatestScheduleByUserIdAndLeadId(user.getUid(), inputData.getLeadId());

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
        Schedule outData = scheduleRepository.save(data);
        if (lead.getStatus() == 1 || lead.getStatus() == 5) {
            lead.setStatus(LeadStatus.CONTACTING.getType());
        }
        leadRepository.save(lead);
        return outData;
    }



    @Override
    public Schedule getLatestScheduleByUserIdAndLeadId(String uid, Long leadId) throws BusinessException {
        return scheduleRepository.getLatestScheduleByUserIdAndLeadId(uid, leadId);
    }

    private void validateSchedule(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate.plusMinutes(5).isBefore(LocalDateTime.now()) || toDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException(new ErrorMessage("ERR_100", "Không thể đặt thời gian trong quá khứ"));
        }
        if (toDate.isBefore(fromDate)) {
            throw new BusinessException(new ErrorMessage("ERR_101", "Ngày kết thúc không thể trước ngày bắt đầu"));
        }
        if (fromDate.until(toDate, ChronoUnit.MINUTES) < 30) {
            throw new BusinessException(new ErrorMessage("ERR_102", "Thời gian tiếp xúc cách nhau ít nhất 30 phút"));
        }
    }
}
