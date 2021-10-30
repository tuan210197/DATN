package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import shupship.domain.model.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LeadResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<LeadAssign> leadAssigns;

    private Long id;
    private String phone;
    private Long type;
    private String fullName;
    private String customerCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Schedule> schedules;
    private String companyName;
    private String subscriptionStatus;
    private String leadSource;
    private Long convertStatus;
    private Long status;
    private String representation;
    private String title;
    private Long isFromEVTP;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressResponse addressResponse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<IndustryResponse> industries;

    public static LeadResponse leadModelToDto(Lead model) {
        LeadResponse data = new LeadResponse();
        data.setId(model.getId());
        data.setLeadSource(model.getLeadSource());
        data.setFullName(model.getFullName());
        data.setIsFromEVTP(model.getIsFromEVTP());
        data.setTitle(model.getTitle());
        data.setRepresentation(model.getRepresentation());
        data.setStatus(model.getStatus());
        data.setCompanyName(model.getCompanyName());
        data.setCustomerCode(model.getCustomerCode());
        data.setType(model.getType());
        data.setPhone(model.getPhone());
        data.setFullName(model.getFullName());
        Address address = model.getAddress();
        if (address != null)
            data.setAddressResponse(AddressResponse.leadModelToDto(address));

       List<IndustryResponse> industryResponses = model.getIndustries().stream().map(e -> new IndustryResponse(e.getCode(),e.getName())).collect(Collectors.toList());
       data.setIndustries(industryResponses);
        return data;
    }
}
