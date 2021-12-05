package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Schedule;
import shupship.enums.ScheduleStatus;
import shupship.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponseLeadDto implements Comparable<ScheduleResponseLeadDto> {
    private Long id;
    private String createdBy;
    private LocalDateTime createDate;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String description;
    private String status;
    private String statusDescription;
    private ResultLeadResponse result;

    public ScheduleResponseLeadDto(Long id, LocalDateTime fromDate, LocalDateTime toDate, String description, String status) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.description = description;
        this.status = status;
    }

    public static ScheduleResponseLeadDto scheduleModelToDto(Schedule schedule) {
        ScheduleResponseLeadDto scheduleResponseDto = new ScheduleResponseLeadDto();

//        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
//        LocalDateTime date = LocalDateTime.ofInstant(schedule.getCreatedDate(), zone);

        scheduleResponseDto.setId(schedule.getId());
        scheduleResponseDto.setCreatedBy(schedule.getUser().getFullName());
        scheduleResponseDto.setCreateDate(schedule.getCreatedDate());
        scheduleResponseDto.setFromDate(schedule.getFromDate());
        scheduleResponseDto.setToDate(schedule.getToDate());
        scheduleResponseDto.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());
//        scheduleResponseDto.setStatusDescription(ScheduleStatusVi.valueOf(ScheduleStatus.getByValue(schedule.getStatus()).name()).getType());
        if (schedule.getResult() != null) {
            scheduleResponseDto.setResult(ResultLeadResponse.resultModelToDto(schedule.getResult()));
        }
        return scheduleResponseDto;
    }

    public static ScheduleResponseLeadDto scheduleResponseDtoToDto(ScheduleResponseDto schedule) {
        ScheduleResponseLeadDto scheduleResponseDto = new ScheduleResponseLeadDto();
        scheduleResponseDto.setId(schedule.getId());
        scheduleResponseDto.setFromDate(schedule.getFromDate());
        scheduleResponseDto.setToDate(schedule.getToDate());
        if (schedule.getResult() != null) {
            scheduleResponseDto.setResult(ResultLeadResponse.resultResultResponseToDto(schedule.getResult()));
        }
        return scheduleResponseDto;
    }

    @Override
    public int compareTo(ScheduleResponseLeadDto o) {
        if (this.getResult() == null && o.getResult() != null) {
            return DateTimeUtils.StringToLocalDateTime2(o.getResult().getCreatedDate()).compareTo((this.getFromDate()));
        }
        if (this.getResult() != null && o.getResult() == null) {
            return (o.getFromDate()).compareTo(DateTimeUtils.StringToLocalDateTime2(this.getResult().getCreatedDate()));
        }
        if (this.getResult() == null && o.getResult() == null) {
            return (o.getFromDate()).compareTo((this.getFromDate()));
        }
        return o.getResult().getCreatedDate().compareTo(this.getResult().getCreatedDate());
    }
}
