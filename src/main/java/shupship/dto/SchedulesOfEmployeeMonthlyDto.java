package shupship.dto;

import lombok.Data;

import java.util.List;

@Data
public class SchedulesOfEmployeeMonthlyDto {

    private Long empSystemId;

    private String empCode;

    private String fullName;

    private List<SchedulesMonthlyDto> schedules;

}
