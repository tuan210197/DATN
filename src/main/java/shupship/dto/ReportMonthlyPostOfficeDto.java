package shupship.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ReportMonthlyPostOfficeDto {

    private String postCode;

    private BigInteger totalEmployees;

    private BigInteger totalAssigns;

    private BigInteger successes;

    private BigInteger contacting;

    private BigInteger fails;

    private BigInteger employeeNotAssigned; // SL cá nhân chưa có tiếp xúc

    private BigInteger assigned;

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
