package shupship.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shupship.common.Constants;
import shupship.dto.SchedulesOfEmployeeMonthlyDto;
import shupship.helper.PagingRs;
import shupship.helper.ResponseUtil;
import shupship.service.IReportService;
import shupship.util.DateTimeUtils;
import shupship.util.exception.HieuDzException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    IReportService reportService;

    public ResponseEntity reportAllCrm(@PageableDefault(page = 1)
                                       @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                       @RequestParam(required = false) String from,
                                       @RequestParam(required = false) String to,
                                       @RequestParam(required = false) String dept) throws Exception {


        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);


        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());

        PagingRs pagingRs = reportService.reportAllPageable(pageRequest, startTimestamp, endTimestamp, dept);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));

    }


    @GetMapping(value = "/post")
    public ResponseEntity reportByPostCodeOnEVTP(@PageableDefault(page = 1)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                                 @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) String post) throws Exception {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);


        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = reportService.reportOnEVTPByPostCode(pageRequest, startTimestamp, endTimestamp, post);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/emp")
    public ResponseEntity reportByEmployeeOnEVTP(@PageableDefault(page = 1)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                                 @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) Long id) throws Exception {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        SchedulesOfEmployeeMonthlyDto pagingRs = reportService.reportByEmployeeOnEVTP(pageRequest, startDate, endDate, id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/dept/emp")
    public ResponseEntity reportAllEmpsInDept(@PageableDefault(page = 1)
                                              @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                              @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) String deptCode) throws Exception {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = reportService.reportAllEmpsInDept(pageRequest, startTimestamp, endTimestamp, deptCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

}
