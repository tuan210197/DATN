package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Schedule;
import shupship.enums.ScheduleStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponseResultDto {
    private Long id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String description;
    private String status;
    private String statusDescription;

    public static ScheduleResponseResultDto scheduleModelToDto(Schedule schedule) {
        ScheduleResponseResultDto scheduleResponseDto = new ScheduleResponseResultDto();
        scheduleResponseDto.setId(schedule.getId());
        scheduleResponseDto.setFromDate(schedule.getFromDate());
        scheduleResponseDto.setToDate(schedule.getToDate());
        scheduleResponseDto.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());
        scheduleResponseDto.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());
        return scheduleResponseDto;
    }
}
