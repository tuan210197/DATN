package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shupship.domain.model.Result;
import shupship.response.AddressResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse {

    private Long id;
    private String result;
    private String reasonDescription;
    private String reason;
    private String suggestions;
    private String proposal;
    private String description;
    private String policy;
    private String customerCode;
    private Long status;
    private String statusDescription;
    private Double districtPercent;
    private Double inProvincePercent;
    private Double outProvincePercent;
    private Double discount;
    private Double refundPercent;
    private AddressResponse pickupAddress;
    private ScheduleResponseResultDto nextSchedule;


    public static ResultResponse resultModelToDto(Result result) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setId(result.getId());
        resultResponse.setReason(result.getReason());
        resultResponse.setSuggestions(result.getProposal());
        resultResponse.setProposal(result.getProposal());
        resultResponse.setStatus(result.getStatus());
        resultResponse.setResult(result.getResult());
        resultResponse.setInProvincePercent(result.getInProvincePrice());
        resultResponse.setOutProvincePercent(result.getOutProvincePrice());
        resultResponse.setDistrictPercent(result.getDistrictPercent());
        resultResponse.setDiscount(result.getDiscount());
        resultResponse.setRefundPercent(result.getRefundPercent());
        resultResponse.setCustomerCode(result.getCustomerCode());
        if (result.getAddress() != null) {
            resultResponse.setPickupAddress(AddressResponse.leadModelToDto1(result.getAddress()));
        }
        return resultResponse;
    }
}
