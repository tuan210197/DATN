package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Schedule;
import shupship.dto.ResultLeadResponse;
import shupship.dto.ScheduleResponseResultDto;
import shupship.enums.ScheduleStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleLstResponse {
    private Long id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String description;
    private String status;
    private String statusDescription;
    private LeadScheduleResponse lead;
    private ResultLeadResponse result;
    private ScheduleResponseResultDto nextSchedule;

    public ScheduleLstResponse(Long id, LocalDateTime fromDate, LocalDateTime toDate, String description, String status) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.description = description;
        this.status = status;
    }

    public static ScheduleLstResponse scheduleModelToDto(Schedule schedule) {
        ScheduleLstResponse scheduleResponseDto = new ScheduleLstResponse();
        scheduleResponseDto.setId(schedule.getId());
        scheduleResponseDto.setFromDate(schedule.getFromDate());
        scheduleResponseDto.setToDate(schedule.getToDate());
        scheduleResponseDto.setLead(LeadScheduleResponse.leadScheduleModelToDto(schedule.getLead()));
        scheduleResponseDto.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());
//      scheduleResponseDto.setStatusDescription(ScheduleStatusVi.valueOf(ScheduleStatus.getByValue(schedule.getStatus()).name()).getType());
        if (schedule.getResult() != null) {
            scheduleResponseDto.setResult(ResultLeadResponse.resultModelToDto(schedule.getResult()));
        }
        return scheduleResponseDto;
    }

}
