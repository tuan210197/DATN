package shupship.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ReportMonthlyPostOfficeDto {

    private String postCode;

    private Long totalEmployees;

    private Long totalAssigns;

    private Long successes;

    private Long contacting;

    private Long fails;

    private Long employeeNotAssigned; // SL cá nhân chưa có tiếp xúc

    private Long assigned;

    public static ReportMonthlyPostOfficeDto convertObject(ReportAllDepts reportAllDept) {

        ReportMonthlyPostOfficeDto reportMonthlyPostOfficeDto = new ReportMonthlyPostOfficeDto();
        reportMonthlyPostOfficeDto.setPostCode(reportAllDept.getPostCode());
        reportMonthlyPostOfficeDto.setTotalEmployees(reportAllDept.getEmployees());
        reportMonthlyPostOfficeDto.setTotalAssigns(reportAllDept.getTotalAssigns());
        reportMonthlyPostOfficeDto.setAssigned(reportAllDept.getAssigned());
        reportMonthlyPostOfficeDto.setSuccesses(reportAllDept.getSuccesses());
        reportMonthlyPostOfficeDto.setContacting(reportAllDept.getContacting());
        reportMonthlyPostOfficeDto.setFails(reportAllDept.getFails());
        reportMonthlyPostOfficeDto.setEmployeeNotAssigned(reportAllDept.getEmployeeNotAssigned());

        return reportMonthlyPostOfficeDto;
    }

}
