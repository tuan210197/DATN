package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shupship.dto.ScheduleResponseDto;
import shupship.enums.ScheduleStatus;
import shupship.helper.ResponseUtil;
import shupship.request.ScheduleRequest;
import shupship.domain.model.Schedule;
import shupship.response.PagingRs;
import shupship.response.ScheduleLstResponse;
import shupship.service.ScheduleService;
import shupship.util.DateTimeUtils;
import shupship.util.exception.HieuDzException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

@RestController
@Slf4j
@RequestMapping("/api/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;


    @PostMapping(value = "/save")
    public ResponseEntity createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) throws Exception {
        Schedule schedule = scheduleService.createSchedule(scheduleRequest);

        ScheduleResponseDto response = new ScheduleResponseDto();
        response.setId(schedule.getId());
        response.setDescription(schedule.getDescription());
        response.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());
        response.setFromDate(schedule.getFromDate());
        response.setToDate(schedule.getToDate());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping(value = "/update/{scheduleId}")
    public ResponseEntity updateSchedule(@RequestBody ScheduleRequest inputData, @PathVariable Long scheduleId) throws Exception {
        Schedule data = scheduleService.updateSchedule(inputData, scheduleId);

        ScheduleResponseDto response = new ScheduleResponseDto();
        response.setId(data.getId());
        response.setDescription(data.getDescription());
        response.setStatus(ScheduleStatus.getByValue(data.getStatus()).name());
        response.setFromDate(data.getFromDate());
        response.setToDate(data.getToDate());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getSchedulesPaging(@PageableDefault(page = 1)
                                             @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                             @RequestParam(required = false) String date, @RequestParam(required = false) String month) {

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        LocalDate localDate;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isNotEmpty(date) && !DateTimeUtils.isValidDate(date)) {
            throw new HieuDzException("Illegal Arguments");
        }
        if (StringUtils.isNotEmpty(month) && !DateTimeUtils.isValidDate(month)) {
            throw new HieuDzException("Illegal Arguments");
        }
        if (StringUtils.isNotEmpty(date)) {
            localDate = DateTimeUtils.StringToLocalDate(date);
            startDate = localDate.atStartOfDay();
            endDate = localDate.plusDays(1).atStartOfDay();
        }

        if (StringUtils.isNotEmpty(month)) {
            localDate = DateTimeUtils.StringToLocalDate(month);
            startDate = DateTimeUtils.getFirstOfCurrentMonth(localDate).atStartOfDay();
            endDate = DateTimeUtils.getEndOfCurrentMonth(localDate).plusDays(1).atStartOfDay();
        }
        Page<ScheduleLstResponse> resp = scheduleService.findAllSchedulesPageable(pageRequest, startDate, endDate);
        PagingRs pagingRs = new PagingRs();
        pagingRs.setData(resp.getContent());
        pagingRs.setTotalItem(resp.getTotalElements());
        return new ResponseEntity(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/{scheduleId}")
    public ResponseEntity getSchedulebyId(@PathVariable Long scheduleId) throws Exception {
        ScheduleResponseDto schedule = scheduleService.detailSchedule(scheduleId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schedule));
    }
}
