package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.Schedule;
import shupship.enums.ScheduleStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulesMonthlyDto {
    private Long id;
    private String status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String leadName;

    public static SchedulesMonthlyDto scheduleModelToReportDto(Schedule schedule) {

        SchedulesMonthlyDto responseDto = new SchedulesMonthlyDto();
        responseDto.setId(schedule.getId());
        responseDto.setFromDate(schedule.getFromDate());
        responseDto.setToDate(schedule.getToDate());

        if (StringUtils.isNotEmpty(schedule.getLead().getCompanyName()))
            responseDto.setLeadName(schedule.getLead().getCompanyName());
        else if (StringUtils.isNotEmpty(schedule.getLead().getFullName()))
            responseDto.setLeadName(schedule.getLead().getFullName());
        else responseDto.setLeadName("");

        if (schedule.getStatus() != null)
            responseDto.setStatus(ScheduleStatus.getByValue(schedule.getStatus()).name());

        return responseDto;
    }
}
