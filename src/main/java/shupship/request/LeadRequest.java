package shupship.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class LeadRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;
    private int page;

    private int pageSize;

    private Long id;

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

    private Double quantityMonth;

    private Double weight;

    private Double expectedRevenue;

    private String industrys;

    private String subscriptionStatus;

    private String leadSource;

    private Long status;

    private List<String> industry;

}
