package shupship.service;

import org.springframework.data.domain.Pageable;
import shupship.dto.SchedulesOfEmployeeMonthlyDto;

import java.time.LocalDateTime;

public interface IReportService {
    SchedulesOfEmployeeMonthlyDto reportByEmployeeOnEVTP(Pageable pageRequest, LocalDateTime startDate, LocalDateTime endDate, Long id) throws Exception;

}
