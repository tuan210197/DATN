package shupship.service;

import org.springframework.data.domain.Pageable;
import shupship.dto.SchedulesOfEmployeeMonthlyDto;
import shupship.helper.PagingRs;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface IReportService {
    SchedulesOfEmployeeMonthlyDto reportByEmployeeOnEVTP(Pageable pageRequest, LocalDateTime startDate, LocalDateTime endDate, Long id) throws Exception;

    PagingRs reportAllPageable(Pageable pageRequest, Timestamp startTimestamp, Timestamp endTimestamp, String deptCode) throws Exception;

    PagingRs reportOnEVTPByPostCode(Pageable pageRequest, Timestamp startDate, Timestamp endDate, String post) throws Exception;

    PagingRs reportAllEmpsInDept(Pageable pageRequest, Timestamp startTimestamp, Timestamp endTimestamp, String deptCode) throws Exception;

}
