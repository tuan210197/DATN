package shupship.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeadUpdateRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;
    private int page;
    private int pageSize;

    private String representation;
    @Size(max = 100)
    private String fullName;

    private String phone;

    private String description;

    private AddressRequest address;

    @Size(max = 100)
    private String title;

    @Size(max = 100)
    private String companyName;

    private String industrys;

    private Long leadScore;

    private String leadStage;

    private String subscriptionStatus;

    private Double quantityMonth;

    private Double weight;

    private Double expectedRevenue;

    private Double inProvincePercent;

    private Double outProvincePercent;

    private String leadSource;

    private Long status;

    private List<String> industry;

}
