package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shupship.dto.ScheduleResponseDto;
import shupship.enums.ScheduleStatus;
import shupship.helper.ResponseUtil;
import shupship.request.ScheduleRequest;
import shupship.domain.model.Schedule;
import shupship.response.LeadResponse;
import shupship.service.ScheduleService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
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

    @GetMapping(value = "/{scheduleId}")
    public ResponseEntity getSchedulebyId(@PathVariable Long scheduleId) throws Exception {
        ScheduleResponseDto schedule = scheduleService.detailSchedule(scheduleId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schedule));
    }
}
