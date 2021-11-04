package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Schedule;
import shupship.enums.ScheduleStatus;
import shupship.enums.ScheduleStatusVi;
import shupship.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponseLeadHadPhoneDto implements Comparable<ScheduleResponseLeadHadPhoneDto> {

    private String createdBy;
    private String statusDescription;
    private LocalDateTime createdDate;
    private ResultLeadResponse result;

    public static ScheduleResponseLeadHadPhoneDto scheduleModelToDto(Schedule schedule, Map<Long, String> map) {
        ScheduleResponseLeadHadPhoneDto scheduleResponseDto = new ScheduleResponseLeadHadPhoneDto();
//        Users user = getCurrentUser();
        scheduleResponseDto.setCreatedBy(map.get(schedule.getCreatedBy()));
        if (schedule.getResult() != null) {
            scheduleResponseDto.setCreatedDate(DateTimeUtils.instantToLocalDateTime(schedule.getResult().getCreatedDate()));
        } else {
            scheduleResponseDto.setCreatedDate((schedule.getFromDate()));
        }
        scheduleResponseDto.setStatusDescription(ScheduleStatusVi.valueOf(ScheduleStatus.getByValue(schedule.getStatus()).name()).getType());
        return scheduleResponseDto;
    }

    @Override
    public int compareTo(ScheduleResponseLeadHadPhoneDto o) {
        if (this.getResult() == null && o.getResult() != null) {
            return DateTimeUtils.StringToLocalDateTime2(o.getResult().getCreatedDate()).compareTo((this.getCreatedDate()));
        }
        if (this.getResult() != null && o.getResult() == null) {
            return (o.getCreatedDate()).compareTo(DateTimeUtils.StringToLocalDateTime2(this.getResult().getCreatedDate()));
        }
        if (this.getResult() == null && o.getResult() == null) {
            return (o.getCreatedDate()).compareTo((this.getCreatedDate()));
        }
        return DateTimeUtils.StringToLocalDateTime2(o.getResult().getCreatedDate()).compareTo(DateTimeUtils.StringToLocalDateTime2(this.getResult().getCreatedDate()));
    }
}
