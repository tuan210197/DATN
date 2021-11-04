package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.Lead;
import shupship.domain.model.Schedule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadHadPhoneResponseDto {
    private String errorCode;
    private String message;
    private String customerCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ScheduleResponseLeadHadPhoneDto> schedules;

    public static LeadHadPhoneResponseDto leadModelToDto(Lead data, List<Schedule> schedules) {
        LeadHadPhoneResponseDto response = new LeadHadPhoneResponseDto();
        if (StringUtils.isNotBlank(data.getCustomerCode())) {
            response.setCustomerCode(data.getCustomerCode());
        } else {
            response.setCustomerCode("Khách hàng chưa có Mã!");
        }
        response.setPhone(data.getPhone());
//        if (CollectionUtils.isNotEmpty(schedules)) {
//            List<ScheduleResponseLeadHadPhoneDto> scheduleResponseDtos = schedules.stream().map(ScheduleResponseLeadHadPhoneDto::scheduleModelToDto).collect(Collectors.toList());
//            response.setSchedules(scheduleResponseDtos);
//        }
        return response;
    }

    public static LeadHadPhoneResponseDto leadModelHadPhoneToDto(Lead data, List<Schedule> schedules, Map<Long, String> map) {
        LeadHadPhoneResponseDto response = new LeadHadPhoneResponseDto();
        if (StringUtils.isNotBlank(data.getCustomerCode())) {
            response.setCustomerCode(data.getCustomerCode());
        } else {
            response.setCustomerCode("Khách hàng chưa có Mã!");
        }
        response.setPhone(data.getPhone());
        if (CollectionUtils.isNotEmpty(schedules)) {
            List<ScheduleResponseLeadHadPhoneDto> scheduleResponseDtos = schedules.stream().map((Schedule schedule) -> ScheduleResponseLeadHadPhoneDto.scheduleModelToDto(schedule, map)).collect(Collectors.toList());
            response.setSchedules(scheduleResponseDtos);
        }
        return response;
    }
}
