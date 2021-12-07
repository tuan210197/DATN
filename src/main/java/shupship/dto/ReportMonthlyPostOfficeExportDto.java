package shupship.dto;

import lombok.Data;

@Data
public class ReportMonthlyPostOfficeExportDto extends ReportMonthlyPostOfficeDto{

    private String deptCode;

    public void fromReportMonthlyPostOfficeDto(ReportMonthlyPostOfficeDto dto, String deptCode) {
        this.setDeptCode(deptCode);
        this.setPostCode(dto.getPostCode());
        this.setAssigned(dto.getAssigned());
        this.setContacting(dto.getContacting());
        this.setFails(dto.getFails());
        this.setTotalEmployees(dto.getTotalEmployees());
        this.setEmployeeNotAssigned(dto.getEmployeeNotAssigned());
        this.setTotalAssigns(dto.getTotalAssigns());
        this.setSuccesses(dto.getSuccesses());
    }

}
