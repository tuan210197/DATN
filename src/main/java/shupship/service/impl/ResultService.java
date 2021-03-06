package shupship.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.dto.CommonCodeResponseDto;
import shupship.domain.model.*;
import shupship.dto.ResultResponse;
import shupship.dto.ScheduleResponseResultDto;
import shupship.enums.LeadStatus;
import shupship.enums.ResultStatus;
import shupship.enums.ScheduleStatus;
import shupship.repo.*;
import shupship.request.AddressRequest;
import shupship.request.ResultRequest;
import shupship.request.ScheduleRequest;
import shupship.service.IResultService;
import shupship.service.ScheduleService;
import shupship.util.DateTimeUtils;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import java.time.LocalDateTime;

@Service
@Transactional
public class ResultService implements IResultService {

    @Autowired
    IScheduleRepository scheduleRepository;

    @Autowired
    ILeadRepository leadRepository;

    @Autowired
    IResultRepository resultRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ILeadAssignRepository leadAssignRepository;

    @Autowired
    BasicLoginRepo basicLoginRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public ResultResponse createResult(ResultRequest resultRequest) throws Exception {

        String startDateStr = null, endDateStr = null;
        Users user = getCurrentUser();
        Schedule schedule = scheduleRepository.getScheduleById(resultRequest.getScheduleId(), user.getEmpSystemId());
        Schedule scheduleLatestResult = scheduleRepository.getScheduleLatestResultById(user.getEmpSystemId());
        if (StringUtils.isNotBlank(resultRequest.getFromDate()) && StringUtils.isNotBlank(resultRequest.getToDate())) {
            startDateStr = resultRequest.getFromDate();
            endDateStr = resultRequest.getToDate();
        }
        if (schedule == null)
            throw new HieuDzException("L???ch kh??ng t???n t???i");

        Lead lead = leadRepository.findLeadById(schedule.getLead().getId());

        //V???i c??c k???t qu??? l?? KH t??? ch???i ti???p x??c v?? Th???t b???i th?? m???c ????? xu???t l?? b???t bu???c
        if (resultRequest.getStatus() == ResultStatus.FAIL.getType()) {
            if (StringUtils.isEmpty(resultRequest.getReason())) {
                throw new HieuDzException("B???t bu???c ph???i nh???p m???c l?? do th???t b???i");
            }
        }

        // V???i c??c k???t qu??? l?? th??nh c??ng th?? b???t bu???c nh???p c??c tr?????ng sau:
        if (resultRequest.getStatus() == ResultStatus.SUCCESS.getType()) {
            if (resultRequest.getPickupAddress() == null) {
                throw new HieuDzException("B???t bu???c nh???p m???c ?????a ch??? l???y h??ng");
            }
            if (resultRequest.getInProvincePercent() == null || resultRequest.getInProvincePercent() == 0) {
                throw new HieuDzException("B???t bu???c nh???p m???c gi?? n???i t???nh");
            }
            if (resultRequest.getOutProvincePercent() == null || resultRequest.getOutProvincePercent() == 0) {
                throw new HieuDzException("B???t bu???c nh???p m???c gi?? ngo???i t???nh");
            }
        }
        //check chi co cap nhat sau thoi diem dat lich
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime startDate = schedule.getFromDate();
        LocalDateTime endDate = schedule.getToDate();
        if (!DateTimeUtils.isSameDay(currentTime, startDate)) {
            throw new HieuDzException("Kh??ng ???????c c???p nh???p k???t qu??? ti???p x??c t???i th???i ??i???m n??y");
        }
        Result result = schedule.getResult();
        Result outData;
        Schedule nextSchedule = scheduleRepository.getNextScheduleByUserIdAndScheduleId(user.getEmpSystemId(), schedule.getId());
        if (result != null) {
            result = ResultRequest.resultDtoToModel(result, resultRequest);
            if (schedule.getIsLatestResult() != 1) {
                schedule.setIsLatestResult(1);
            }
            if (scheduleLatestResult != null) {
                scheduleLatestResult.setIsLatestResult(0);
                scheduleRepository.save(scheduleLatestResult);
            }
            //Check update address
            if (resultRequest.getPickupAddress() != null) {
                Address addressUpdate = AddressRequest.addressDtoToModel(resultRequest.getPickupAddress());
                Address address = result.getAddress();
                if (address != null) {
                    address.setHomeNo(addressUpdate.getHomeNo());
                    address.setDistrict(addressUpdate.getDistrict());
                    address.setWard(addressUpdate.getWard());
                    address.setProvince(addressUpdate.getProvince());
                    address.setStatus(addressUpdate.getStatus());
                    result.setAddress(address);
                } else {
                    result.setAddress(addressUpdate);
                }
            }
            outData = resultRepository.save(result);

            if (nextSchedule != null && StringUtils.isNotEmpty(resultRequest.getFromDate())) {
                if (DateTimeUtils.isOverlapTime(schedule.getFromDate(), schedule.getToDate(), DateTimeUtils.StringToLocalDateTime(startDateStr),
                        DateTimeUtils.StringToLocalDateTime(endDateStr))) {
                    throw new HieuDzException("L???ch b??? tr??ng v???i l???ch tr?????c ????");
                }
                ScheduleRequest request = new ScheduleRequest();
                request.setFromDate(resultRequest.getFromDate());
                request.setToDate(resultRequest.getToDate());
                nextSchedule = scheduleService.updateSchedule(request, nextSchedule.getId());
            } else if (nextSchedule == null && StringUtils.isNotEmpty(resultRequest.getFromDate())) {
                nextSchedule = createNextSchedule(resultRequest, startDateStr, endDateStr, schedule, nextSchedule);
            }
        } else {
            result = ResultRequest.resultDtoToModel(resultRequest);
            //V???i c??c k???t qu??? l?? KH t??? ch???i ti???p x??c v?? Th???t b???i th?? m???c ????? xu???t l?? b???t bu???c
            if (resultRequest.getStatus() == ResultStatus.FAIL.getType()) {
                if (StringUtils.isEmpty(resultRequest.getReason())) {
                    throw new HieuDzException("B???t bu???c ph???i nh???p m???c l?? do th???t b???i");
                }
            }

            // V???i c??c k???t qu??? l?? th??nh c??ng th?? b???t bu???c nh???p c??c tr?????ng sau:
            if (resultRequest.getStatus() == ResultStatus.SUCCESS.getType()) {
                if (resultRequest.getPickupAddress() == null) {
                    throw new HieuDzException("B???t bu???c nh???p m???c ?????a ch??? l???y h??ng");
                }
                if (resultRequest.getInProvincePercent() == null || resultRequest.getInProvincePercent() == 0) {
                    throw new HieuDzException("B???t bu???c nh???p m???c gi?? n???i t???nh");
                }
                if (resultRequest.getOutProvincePercent() == null || resultRequest.getOutProvincePercent() == 0) {
                    throw new HieuDzException("B???t bu???c nh???p m???c gi?? ngo???i t???nh");
                }
            }
            //Check create address
            if (resultRequest.getPickupAddress() != null && result.getAddress() == null) {
                Address address = AddressRequest.addressDtoToModel(resultRequest.getPickupAddress());
                result.setAddress(address);
            }
            result.setSchedule(schedule);
            outData = resultRepository.save(result);
            schedule.setResult(result);
            if (schedule.getIsLatestResult() != 1) {
                schedule.setIsLatestResult(1);
            }
            if (scheduleLatestResult != null) {
                scheduleLatestResult.setIsLatestResult(0);
                scheduleRepository.save(scheduleLatestResult);
            }
            scheduleRepository.save(schedule);

            nextSchedule = createNextSchedule(resultRequest, startDateStr, endDateStr, schedule, nextSchedule);
        }
        LeadAssign leadAssign = leadAssignRepository.findLeadAssignByLeadIdAndEmpId(schedule.getLead().getId(), user.getEmpSystemId());
        if(leadAssign == null)
            throw new HieuDzException("Ph???i v??o t??i kho???n nh??n vi??n");
        updateLeadAndScheduleStatus(outData, lead, schedule, leadAssign);

        ResultResponse response = ResultResponse.resultModelToDto(outData);
        if (nextSchedule != null) {
            response.setNextSchedule(ScheduleResponseResultDto.scheduleModelToDto(nextSchedule));
        }
        return response;
    }

    private Schedule createNextSchedule(ResultRequest inputData, String startDateStr, String endDateStr, Schedule schedule, Schedule nextSchedule) throws Exception {

        if (StringUtils.isNotEmpty(inputData.getFromDate())) {
            if ((!DateTimeUtils.isValidLocalDateTime(startDateStr) || !DateTimeUtils.isValidLocalDateTime(endDateStr))) {
                throw new HieuDzException("Illegal Arguments");
            }
            if (DateTimeUtils.isOverlapTime(schedule.getFromDate(), schedule.getToDate(), DateTimeUtils.StringToLocalDateTime(startDateStr),
                    DateTimeUtils.StringToLocalDateTime(endDateStr))) {
                throw new HieuDzException("L???ch b??? tr??ng v???i l???ch tr?????c ????");
            }
            ScheduleRequest scheduleRequest = new ScheduleRequest();
            scheduleRequest.setLeadId(schedule.getLead().getId());
            scheduleRequest.setFromDate(startDateStr);
            scheduleRequest.setToDate(endDateStr);
            scheduleRequest.setNextScheduleId(schedule.getId());
            nextSchedule = scheduleService.createSchedule(scheduleRequest);
        }
        return nextSchedule;
    }

    private void updateLeadAndScheduleStatus(Result outData, Lead lead, Schedule schedule, LeadAssign leadAssign) {

        if (1 == outData.getStatus() || 2 == outData.getStatus() || 4 == outData.getStatus() || 6 == outData.getStatus()) {
            lead.setStatus(LeadStatus.CONTACTING.getType());
            schedule.setStatus(ScheduleStatus.CONTACTING.getType());
            leadAssign.setStatus(LeadStatus.CONTACTING.getType());
        } else if (3 == outData.getStatus() || outData.getStatus() == 7) {
            lead.setStatus(LeadStatus.SUCCESS.getType());
            schedule.setStatus(ScheduleStatus.SUCCESS.getType());
            leadAssign.setStatus(LeadStatus.SUCCESS.getType());
        } else if (5 == outData.getStatus() || outData.getStatus() == 8) {
            lead.setStatus(LeadStatus.FAILED.getType());
            schedule.setStatus(LeadStatus.FAILED.getType());
            leadAssign.setStatus(LeadStatus.FAILED.getType());
        }
        scheduleRepository.save(schedule);
        leadRepository.save(lead);
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
}
