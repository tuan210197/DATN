package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportMonthlyDeptDto {

    private String deptCode;
    private Long totalPost;
    private Long totalCountEmp;
    private Long totalAssigns;
    private Long totalSuccesses;
    private Long totalContacting;
    private Long totalFails;
    private Long totalEmployeeNotAssigned;
    private Long tyHT;
    private Long totalLeadNotTX;
    private Long tyTX;
    private Long totalAssigned;

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

    public static ReportMonthlyDeptDto convertToDto(ReportMonthlyDeptDto model){
        ReportMonthlyDeptDto data = new ReportMonthlyDeptDto();


        return data;
    }

}
