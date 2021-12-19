package shupship.request;

import lombok.Data;
import shupship.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ScheduleRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Long leadId;

    @NotBlank
    private String fromDate;

    @NotBlank
    private String toDate;

}
