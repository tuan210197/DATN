package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportMonthlyDeptDto {

    private String deptCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ReportMonthlyPostOfficeDto> reportMonthlyPostOfficeDtos;

    public static List<ReportMonthlyPostOfficeDto> convertList(List<ReportAllDepts> reportAllDepts) {

        List<ReportMonthlyPostOfficeDto> responses = new ArrayList<>();
        for (ReportAllDepts report : reportAllDepts) {
            ReportMonthlyPostOfficeDto reportMonthlyPostOfficeDto = ReportMonthlyPostOfficeDto.convertObject(report);

            responses.add(reportMonthlyPostOfficeDto);
        }
        return responses;
    }
}
