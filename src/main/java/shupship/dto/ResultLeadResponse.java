package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Result;
import shupship.response.AddressResponse;
import shupship.util.DateTimeUtils;

import static shupship.util.DateTimeUtils.instantToLocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultLeadResponse implements Comparable<ResultLeadResponse>{

    private Long id;
    private String result;
    private String reasonDescription;
    private String reason;
    private String suggestions;
    private String description;
    private String policy;
    private Long status;
    private String statusDescription;
    private String customerCode;
    private Double districtPercent;
    private Double inProvincePercent;
    private Double refundPercent;
    private String createdDate;
    ///private PriceResponseDto price;
    private AddressResponse address;



    public static ResultLeadResponse resultModelToDto(Result result) {
        ResultLeadResponse resultResponse = new ResultLeadResponse();
        resultResponse.setId(result.getId());
        resultResponse.setReason(result.getReason());
        resultResponse.setSuggestions(result.getProposal());
        resultResponse.setStatus(result.getStatus());;
        resultResponse.setResult(result.getResult());
        resultResponse.setCustomerCode(result.getCustomerCode());
        resultResponse.setInProvincePercent(result.getInProvincePrice());
        resultResponse.setDistrictPercent(result.getDistrictPercent());
        resultResponse.setRefundPercent(result.getRefundPercent());
        resultResponse.setCreatedDate(instantToLocalDateTime(result.getLastModifiedDate()).toString());
//        if (result.getPrice() != null) {
//            resultResponse.setPrice(PriceResponseDto.priceModelTodto(result.getPrice()));
//        }
        if (result.getAddress() != null) {
            resultResponse.setAddress(AddressResponse.leadModelToDto(result.getAddress()));
        }
        return resultResponse;
    }

    public static ResultLeadResponse resultResultResponseToDto(ResultResponse result) {
        ResultLeadResponse resultResponse = new ResultLeadResponse();
        resultResponse.setId(result.getId());
        resultResponse.setPolicy(result.getPolicy());
        resultResponse.setReason(result.getReason());
        resultResponse.setSuggestions(result.getSuggestions());
        resultResponse.setStatus(result.getStatus());
        resultResponse.setDescription(result.getDescription());
        resultResponse.setResult(result.getResult());
        resultResponse.setInProvincePercent(result.getInProvincePercent());
        resultResponse.setDistrictPercent(result.getDistrictPercent());
        resultResponse.setRefundPercent(result.getRefundPercent());
//        if (result.getPrice() != null) {
//            resultResponse.setPrice(result.getPrice());
//        }
        if (result.getPickupAddress() != null) {
            resultResponse.setAddress(result.getPickupAddress());
        }
        return resultResponse;
    }

    @Override
    public int compareTo(ResultLeadResponse o) {
        return DateTimeUtils.StringToLocalDateTime(this.getCreatedDate()).compareTo(DateTimeUtils.StringToLocalDateTime(o.getCreatedDate()));
    }
}
