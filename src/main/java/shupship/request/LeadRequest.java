package shupship.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import shupship.domain.model.Lead;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class LeadRequest extends BaseRequest{
    private int page;
    private int pageSize;

    private static final long serialVersionUID = 1L;

    private Long id;

    private String salutation;

    private String firstName;

    private String middleName;

    private String lastName;

    @Size(max = 100)
    private String fullName;

    private String email;

    private String phone;

    private String mobile;

    private String gender;

    private String description;

    @Size(max = 100)
    private String representation;

    private Double quantityMonth;

    private Double weight;

    @Size(max = 100)
    private String quality;

    @Size(max = 100)
    private String compensation;

    @Size(max = 100)
    private String payment;

    @Size(max = 100)
    private String other;

    private AddressRequest address;

    @Setter(value = AccessLevel.NONE)
    private Date dateOfBirth;

    @Setter(value = AccessLevel.NONE)
    private String dateOfBirthStr;

    @Size(max = 100)
    private String title;

    @Size(max = 100)
    private String companyName;

    private AddressRequest companyAddress;

    private String industry;

    private Long numOfEmp;

    private Long annualQuantity;

    private Double expectedRevenue;

    private Long leadScore;

    private String leadStage;

    private String subscriptionStatus;

    private String rating;

    private String leadSource;

    private Long ownerId;

    private String ownerBranchCode;

    private String website;

    private String facebookId;

    private String zaloId;

    private String skypeId;

    private Long status;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private Long notInCampaignId;

    private List<String> competitors;

    private List<String> products;

    private List<String> industries;

}
