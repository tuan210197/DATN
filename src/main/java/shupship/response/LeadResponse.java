package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import shupship.domain.model.*;
import java.util.List;

@Data
public class LeadResponse{
    private Long id;
    private String salutation;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String description;
    private Long type;
    private Double quantityMonth;
    private String customerCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Schedule> schedules;
    private String companyName;
    private Long leadScore;
    private String leadStage;
    private String subscriptionStatus;
    private String leadSource;
    private Long convertStatus;
    private Long status;
    private String representation;
    private String title;
    private Double expectedRevenue;
    private Long isFromEVTP;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<LeadAssign> leadAssigns;

    public static LeadResponse leadModelToDto(Lead model) {
        LeadResponse data = new LeadResponse();
        data.setId(model.getId());
        data.setFullName(model.getFullName());
        data.setIsFromEVTP(model.getIsFromEVTP());
        data.setExpectedRevenue(model.getExpectedRevenue());
        data.setTitle(model.getTitle());
        data.setRepresentation(model.getRepresentation());
        data.setStatus(model.getStatus());
        data.setConvertStatus(model.getConvertStatus());
        data.setLeadScore(model.getLeadScore());
        data.setCompanyName(model.getCompanyName());
        data.setCustomerCode(model.getCustomerCode());
        data.setQuantityMonth(model.getQuantityMonth());
        data.setType(model.getType());
//        data.setDescription(model.getDescription());
//        data.setGender(model.getGender());
        data.setPhone(model.getPhone());
        data.setEmail(model.getEmail());
        data.setFullName(model.getFullName());
        data.setLastName(model.getLastName());
        data.setFirstName(model.getFirstName());
//        data.setSalutation(model.getSalutation());
        data.setId(model.getId());
        return data;
    }
}
