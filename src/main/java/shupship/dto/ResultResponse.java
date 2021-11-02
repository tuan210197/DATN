package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Result;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse {

    private Long id;
    private String result;
    private String reasonDescription;
    private Long reason;
    private String suggestions;
    private String proposal;
    private String description;
    private String policy;
    private String customerCode;
    private Long status;
    private String statusDescription;
    private Double districtPercent;
    private Double inProvincePercent;
    private Double refundPercent;
//    private PriceResponseDto price;
//    private AddressResponseDto pickupAddress;
    private ScheduleResponseResultDto nextSchedule;


    public static ResultResponse resultModelToDto(Result result) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setId(result.getId());
        resultResponse.setPolicy(result.getPolicy());
        resultResponse.setReason(result.getReason());
        resultResponse.setSuggestions(result.getProposal());
        resultResponse.setProposal(result.getProposal());
        resultResponse.setStatus(result.getStatus());
        resultResponse.setDescription(result.getDescription());
        resultResponse.setResult(result.getResult());
//        resultResponse.setInProvincePercent(result.getInProvincePercent());
//        resultResponse.setDistrictPercent(result.getDistrictPercent());
        resultResponse.setRefundPercent(result.getRefundPercent());
        resultResponse.setCustomerCode(result.getCustomerCode());
//        if (result.getPrice() != null) {
//            resultResponse.setPrice(PriceResponseDto.priceModelTodto(result.getPrice()));
//        }
//        if (result.getAddress() != null) {
//            resultResponse.setPickupAddress(AddressResponseDto.addressModelToDto(result.getAddress()));
//        }
        return resultResponse;
    }
}
