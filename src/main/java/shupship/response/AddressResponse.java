package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Address;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AddressResponse {
    private Long id;
    private String homeNo;
    private String ward;
    private String district;
    private String province;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String fomatAddress;

    public static AddressResponse leadModelToDto(Address model) {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(model.getId());
        addressResponse.setHomeNo(model.getHomeNo());
        addressResponse.setWard(model.getWard());
        addressResponse.setDistrict(model.getDistrict());
        addressResponse.setProvince(model.getProvince());
        addressResponse.setFomatAddress(model.getFomatAddress());
        return addressResponse;
    }


    public static AddressResponse leadModelToDto1(Address model) {
        AddressResponse addressResponse = new AddressResponse();
//        addressResponse.setId(model.getId());
        addressResponse.setHomeNo(model.getHomeNo());
        addressResponse.setWard(model.getWard());
        addressResponse.setDistrict(model.getDistrict());
        addressResponse.setProvince(model.getProvince());
        addressResponse.setFomatAddress(model.getFomatAddress());
        return addressResponse;
    }
}