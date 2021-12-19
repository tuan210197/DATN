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

    private Long tyLeHT;

    private Long tyLeTX;

    public static ReportMonthlyPostOfficeDto convertObject(ReportAllDepts reportAllDept) {

        ReportMonthlyPostOfficeDto reportMonthlyPostOfficeDto = new ReportMonthlyPostOfficeDto();
        reportMonthlyPostOfficeDto.setPostCode(reportAllDept.getPostCode() != null ? reportAllDept.getPostCode() : "" );
        reportMonthlyPostOfficeDto.setTotalEmployees(reportAllDept.getEmployees() != null ? reportAllDept.getEmployees() : 0L);
        reportMonthlyPostOfficeDto.setTotalAssigns(reportAllDept.getTotalAssigns() != null ? reportAllDept.getTotalAssigns() : 0L);
        reportMonthlyPostOfficeDto.setAssigned(reportAllDept.getAssigned() != null ? reportAllDept.getAssigned() : 0L);
        reportMonthlyPostOfficeDto.setSuccesses(reportAllDept.getSuccesses() != null ? reportAllDept.getSuccesses() : 0L);
        reportMonthlyPostOfficeDto.setContacting(reportAllDept.getContacting() != null ? reportAllDept.getContacting() : 0L);
        reportMonthlyPostOfficeDto.setFails(reportAllDept.getFails() != null ? reportAllDept.getFails() : 0L);
        reportMonthlyPostOfficeDto.setEmployeeNotAssigned(reportAllDept.getEmployeeNotAssigned() != null ? reportAllDept.getEmployeeNotAssigned() : 0L);

        if (reportAllDept.getTotalAssigns() != null && reportAllDept.getTotalAssigns() > 0) {
            reportMonthlyPostOfficeDto.setTyLeHT((reportAllDept.getSuccesses() + reportAllDept.getFails() ) * 100 / reportAllDept.getTotalAssigns());
            reportMonthlyPostOfficeDto.setTyLeTX((reportAllDept.getContacting() ) * 100 / reportAllDept.getTotalAssigns());
        } else {
            reportMonthlyPostOfficeDto.setTyLeTX(0L);
            reportMonthlyPostOfficeDto.setTyLeHT(0L);
        }

        return reportMonthlyPostOfficeDto;
    }

}
