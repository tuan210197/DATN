package shupship.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.Lead;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadStatusVi;
import shupship.enums.LeadType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadScheduleResponseDto {
    private long id;

    private String title;

    private String fullName;

    private String phone;

    private String companyName;

    private String representation;

    private Long annualQuantity;

    private Double expectedRevenue;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private String leadSource;

    private String status;

    private String statusDescription;

    private String type;

    private String customerCode;

    private AddressResponse address;

    public static LeadScheduleResponseDto leadScheduleModelToDto(Lead data) {
        LeadScheduleResponseDto response = new LeadScheduleResponseDto();
        response.setId(data.getId());
        response.setFullName(data.getFullName());
        response.setTitle(data.getTitle());
        response.setExpectedRevenue(data.getExpectedRevenue());
        response.setAnnualQuantity(data.getAnnualQuantity());
        response.setCompanyName(data.getCompanyName());
        response.setRepresentation(data.getRepresentation());
        response.setPhone(data.getPhone());
        response.setLeadSource(StringUtils.isNotBlank(data.getLeadSource()) ? LeadSource.valueOf(data.getLeadSource()).getValue() : "");
        response.setStatus(LeadStatus.getByValue(data.getStatus()).name());
        response.setStatusDescription(LeadStatusVi.valueOf(LeadStatus.getByValue(data.getStatus()).name()).getType());
        response.setType(LeadType.getByValue(data.getType()).name());
        response.setCustomerCode(data.getCustomerCode());
        response.setInProvincePrice(data.getInProvincePrice());
        response.setOutProvincePrice(data.getOutProvincePrice());
        if (data.getAddress() != null) {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setId(data.getAddress().getId());
            addressResponse.setHomeNo(data.getAddress().getHomeNo() == null ? "" : data.getAddress().getHomeNo());
            addressResponse.setWard(data.getAddress().getWard() == null ? "" : data.getAddress().getWard());
            addressResponse.setDistrict(data.getAddress().getDistrict() == null ? "" : data.getAddress().getDistrict());
            addressResponse.setProvince(data.getAddress().getProvince() == null ? "" : data.getAddress().getProvince());
            response.setAddress(addressResponse);
        }
        return response;
    }
}
