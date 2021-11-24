package shupship.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shupship.dto.ScheduleResponseDto;
import shupship.request.ScheduleRequest;
import shupship.domain.model.Schedule;
import shupship.response.ScheduleLstResponse;
import shupship.util.exception.BusinessException;

import java.time.LocalDateTime;

public interface ScheduleService {
    Schedule createSchedule(ScheduleRequest schedule) throws Exception;

    Schedule updateSchedule(ScheduleRequest schedule, Long id) throws Exception;

    Schedule getLatestScheduleByUserIdAndLeadId(Long sysId, Long leadId) throws BusinessException;

    Page<ScheduleLstResponse> findAllSchedulesPageable(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    ScheduleResponseDto detailSchedule(Long id) throws  Exception;

}
