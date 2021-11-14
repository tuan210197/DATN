package shupship.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.Result;

@Data
public class ResultRequest {
    private Long scheduleId;

    private Long status;

    private String reason;

    private String proposal;

    private String description;

    private String fromDate;

    private String toDate;

    private Double districtPercent;

    private Double inProvincePercent;

    private Double outProvincePercent;

    private Double refundPercent;

    private String customerCode;

    private AddressRequest pickupAddress;

    public static Result resultDtoToModel(ResultRequest resultRequest) {
        Result result = new Result();
        result.setReason(resultRequest.getReason());
        result.setProposal(resultRequest.getProposal());
        result.setDistrictPercent(resultRequest.getDistrictPercent());
        result.setInProvincePrice(resultRequest.getInProvincePercent());
        result.setRefundPercent(resultRequest.getRefundPercent());
        result.setStatus(resultRequest.getStatus());
        result.setCustomerCode(resultRequest.getCustomerCode());
        if (resultRequest.getPickupAddress() != null && StringUtils.isNotEmpty(resultRequest.getPickupAddress().getProvince())) {
            result.setAddress(AddressRequest.addressDtoToModel(resultRequest.getPickupAddress()));
        }
        return result;
    }

    public static Result resultDtoToModel(Result result, ResultRequest resultRequest) {
        result.setReason(resultRequest.getReason());
        result.setProposal(resultRequest.getProposal());
        result.setDistrictPercent(resultRequest.getDistrictPercent());
        result.setInProvincePrice(resultRequest.getInProvincePercent());
        result.setRefundPercent(resultRequest.getRefundPercent());
        result.setStatus(resultRequest.getStatus());
        result.setCustomerCode(resultRequest.getCustomerCode());
        return result;
    }
}
