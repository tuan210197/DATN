package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Schedule;
import shupship.enums.ScheduleStatus;
import shupship.enums.ScheduleStatusVi;
import shupship.response.LeadResponse;
import shupship.response.LeadScheduleResponse;

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
    private LeadScheduleResponse lead;
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
        scheduleResponseDto.setLead(LeadScheduleResponse.leadScheduleModelToDto(schedule.getLead()));
        scheduleResponseDto.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());
        if (schedule.getResult() != null) {
            scheduleResponseDto.setResult(ResultResponse.resultModelToDto(schedule.getResult()));
        }
        return scheduleResponseDto;
    }
}
