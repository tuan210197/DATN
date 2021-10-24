package shupship.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeadUpdateRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;
    private String fullName;

    private String phone;

    private String representation;

    private String title;

    private String companyName;

    private Long annualQuantity;

    private Double expectedRevenue;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private String leadSource;

    private Double quantityMonth;

    private Double weight;

    private String quality;

    private String compensation;

    private String payment;

    private String other;

    private AddressRequest address;

    private List<String> competitors;

    private List<String> products;

    private List<String> industries;

}
