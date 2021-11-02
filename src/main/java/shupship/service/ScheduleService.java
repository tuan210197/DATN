package shupship.service;

import shupship.request.ScheduleRequest;
import shupship.domain.model.Schedule;
import shupship.util.exception.BusinessException;

public interface ScheduleService {
    Schedule createSchedule(ScheduleRequest schedule) throws Exception;

    Schedule getLatestScheduleByUserIdAndLeadId(String uid, Long leadId) throws BusinessException;
}
