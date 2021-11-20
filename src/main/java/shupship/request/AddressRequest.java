package shupship.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import shupship.domain.model.Address;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    private String homeNo;

    ////private String street;

    private String ward;

    private String district;

    private String province;

    private String region;

    private String country;

    private String description;

    private BigDecimal lat;

    private BigDecimal lng;

    private String objectName;

    private String fomatAddress;

    public static Address addressDtoToModel(AddressRequest addressRequest) {
        Address address = new Address();
        address.setHomeNo(addressRequest.getHomeNo());
        address.setWard(addressRequest.getWard());
        address.setDistrict(addressRequest.getDistrict());
        address.setProvince(addressRequest.getProvince());
        address.setFomatAddress(addressRequest.getFomatAddress());
        return address;
    }

    public static AddressRequest addressModelToDto(Address address) {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setHomeNo(address.getHomeNo());
        addressRequest.setWard(address.getWard());
        addressRequest.setDistrict(address.getDistrict());
        addressRequest.setProvince(address.getProvince());
        addressRequest.setFomatAddress(address.getFomatAddress());
        return addressRequest;
    }

}