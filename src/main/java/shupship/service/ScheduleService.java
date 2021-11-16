package shupship.service;

import shupship.dto.ScheduleResponseDto;
import shupship.request.ScheduleRequest;
import shupship.domain.model.Schedule;
import shupship.util.exception.BusinessException;

public interface ScheduleService {
    Schedule createSchedule(ScheduleRequest schedule) throws Exception;

    Schedule updateSchedule(ScheduleRequest schedule, Long id) throws Exception;

    Schedule getLatestScheduleByUserIdAndLeadId(Long sysId, Long leadId) throws BusinessException;

    ScheduleResponseDto detailSchedule(Long id) throws  Exception;
}
