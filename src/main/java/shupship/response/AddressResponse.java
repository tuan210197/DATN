package shupship.response;

import lombok.Data;
import shupship.domain.model.Address;

@Data
public class AddressResponse{
	private Long id;
	private String homeNo;
	private String ward;
	private String district;
	private String province;

	public static AddressResponse leadModelToDto(Address model) {
		AddressResponse addressResponse = new AddressResponse();
		addressResponse.setId(model.getId());
		addressResponse.setHomeNo(model.getHomeNo());
		addressResponse.setWard(model.getWard());
		addressResponse.setDistrict(model.getDistrict());
		addressResponse.setProvince(model.getProvince());
		return addressResponse;
	}

}