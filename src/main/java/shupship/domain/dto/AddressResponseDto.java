package shupship.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Address;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AddressResponseDto {
    private String homeNo;

    private String street;

    private String district;

    private String province;

    public static AddressResponseDto addressModelToDto(Address address) {
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        if (address != null) {
            addressResponseDto.setHomeNo(address.getHomeNo());
            addressResponseDto.setStreet(address.getStreet());
            addressResponseDto.setDistrict(address.getDistrict());
            addressResponseDto.setProvince(address.getProvince());

            return addressResponseDto;
        }
        return null;
    }
}
