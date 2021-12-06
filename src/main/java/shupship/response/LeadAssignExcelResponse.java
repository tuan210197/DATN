package shupship.response;

import lombok.Data;
import shupship.domain.model.LeadAssignExcel;

@Data
public class LeadAssignExcelResponse {

    private Long id;

    private String fullName;

    private String companyName;

    private String representation;

    private String title;

    private String phone;

    private AddressResponse address;

    private Long status;

    private String error;

    private String leadSource;

    private String deptCode;

    private String postCode;

    private Long empSystemId;

    private String empCode;


    public static LeadAssignExcelResponse leadAssignExcelModelToDto(LeadAssignExcel leadAssignExcel) {
        LeadAssignExcelResponse leadAssignExcelResponseDto = new LeadAssignExcelResponse();

        leadAssignExcelResponseDto.setId(leadAssignExcel.getId());
        leadAssignExcelResponseDto.setError(leadAssignExcel.getError());
        leadAssignExcelResponseDto.setStatus(leadAssignExcel.getStatus());
        leadAssignExcelResponseDto.setCompanyName(leadAssignExcel.getCompanyName());
        leadAssignExcelResponseDto.setFullName(leadAssignExcel.getFullName());
        leadAssignExcelResponseDto.setRepresentation(leadAssignExcel.getRepresentation());
        leadAssignExcelResponseDto.setTitle(leadAssignExcel.getTitle());
        leadAssignExcelResponseDto.setPhone(leadAssignExcel.getPhone());
        leadAssignExcelResponseDto.setLeadSource(leadAssignExcel.getLeadSource());
        if (leadAssignExcel.getAddress() != null && leadAssignExcel.getAddress().getId() != null)
            leadAssignExcelResponseDto.setAddress(AddressResponse.leadModelToDto(leadAssignExcel.getAddress()));
        leadAssignExcelResponseDto.setDeptCode(leadAssignExcel.getDeptCode());
        leadAssignExcelResponseDto.setPostCode(leadAssignExcel.getPostCode());
        leadAssignExcelResponseDto.setEmpSystemId(leadAssignExcel.getEmpSystemId());
        leadAssignExcelResponseDto.setEmpCode(leadAssignExcel.getEmployeeCode());

        return leadAssignExcelResponseDto;
    }

}
