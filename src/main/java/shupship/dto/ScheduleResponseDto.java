package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Schedule;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponseDto {
    private Long id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String description;
    private String status;
    private ResultResponse result;

    public ScheduleResponseDto(Long id, LocalDateTime fromDate, LocalDateTime toDate, String description, String status) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.description = description;
        this.status = status;
    }

    public static ScheduleResponseDto scheduleModelToDto(Schedule schedule) {
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto();
        scheduleResponseDto.setId(schedule.getId());
        scheduleResponseDto.setFromDate(schedule.getFromDate());
        scheduleResponseDto.setToDate(schedule.getToDate());
        if (schedule.getResult() != null) {
            scheduleResponseDto.setResult(ResultResponse.resultModelToDto(schedule.getResult()));
        }
        return scheduleResponseDto;
    }
}
