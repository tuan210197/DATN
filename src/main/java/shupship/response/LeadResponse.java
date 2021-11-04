package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.*;
import shupship.dto.ScheduleResponseDto;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadStatusVi;
import shupship.enums.LeadType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LeadResponse {

    private long id;

    private String title;

    private String fullName;

    private String phone;

    private String unmaskPhone;

    private LocalDateTime createdDate;

    private String companyName;

    private String representation;

    private Double quantityMonth;

    private Double weight;

    private String quality;

    private String compensation;

    private String payment;

    private String other;

    private String type;

    private String status;

    private String statusDescription;

    private String customerCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressResponse address;

    private Long annualQuantity;

    private Double expectedRevenue;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private String leadSource;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<IndustryResponse> industries;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ScheduleResponseDto> schedules;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LeadAssignResponse> leadAssigns;

    public static LeadResponse leadModelToDto(Lead data) {
        LeadResponse response = new LeadResponse();
        response.setId(data.getId());
        response.setFullName(data.getFullName());
        response.setTitle(data.getTitle());
        response.setExpectedRevenue(data.getExpectedRevenue() == null ? 0 :data.getExpectedRevenue());
        response.setAnnualQuantity(data.getAnnualQuantity() == null ? 0 :data.getAnnualQuantity());
        response.setCompanyName(data.getCompanyName() == null ? "" :data.getCompanyName());
        response.setRepresentation(data.getRepresentation() == null ? "" :data.getRepresentation());
        response.setQuantityMonth(data.getQuantityMonth() == null ? 0 : data.getQuantityMonth());
        response.setWeight(data.getWeight() == null ? 0 :data.getWeight());
        response.setQuality(data.getQuality() == null ? "" :data.getQuality());
        response.setCompensation(data.getCompensation() == null ? "" :data.getCompensation());
        response.setOther(data.getOther() == null ? "" : data.getOther());
        if (data.getType() != null)
            response.setType(LeadType.getByValue(data.getType()).name());
        response.setCustomerCode(data.getCustomerCode() == null ? "" : data.getCustomerCode());
        response.setInProvincePrice(data.getInProvincePrice() == null ? 0 :data.getInProvincePrice());
        response.setOutProvincePrice(data.getOutProvincePrice() == null ? 0 : data.getOutProvincePrice());
        if (data.getStatus().equals(LeadStatus.NOT_CONTACTED.getType())) {
            response.setStatus(LeadStatus.NEW.name());
            response.setStatusDescription(LeadStatusVi.valueOf(LeadStatus.getByValue(1L).name()).getType());
        } else {
            response.setStatus(LeadStatus.getByValue(data.getStatus()).name());
            response.setStatusDescription(LeadStatusVi.valueOf(LeadStatus.getByValue(data.getStatus()).name()).getType());
        }
        if (data.getAddress() != null) {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setId(data.getAddress().getId());
            addressResponse.setHomeNo(data.getAddress().getHomeNo() == null ? "" : data.getAddress().getHomeNo());
            addressResponse.setWard(data.getAddress().getWard() == null ? "" : data.getAddress().getWard());
            addressResponse.setDistrict(data.getAddress().getDistrict() == null ? "" : data.getAddress().getDistrict());
            addressResponse.setProvince(data.getAddress().getProvince() == null ? "" : data.getAddress().getProvince());
            response.setAddress(addressResponse);
        }
        response.setUnmaskPhone(data.getPhone() == null ? "" : data.getPhone().replaceAll(".(?=.{4})", "*"));
        response.setPhone(data.getPhone() == null ? "" : data.getPhone());
        response.setLeadSource(StringUtils.isNotBlank(data.getLeadSource()) ? LeadSource.valueOf(data.getLeadSource()).getValue() : "");
        if (data.getLeadAssigns() != null) {
            List<LeadAssignResponse> leadAssignResponseDtos = data.getLeadAssigns().stream().map(LeadAssignResponse::leadAssignModelToDto)
                    .sorted(Comparator.comparing(LeadAssignResponse::getId).reversed())
                    .collect(Collectors.toList());
            response.setLeadAssigns(leadAssignResponseDtos);
            if (leadAssignResponseDtos.size() > 0) {
                response.setCreatedDate(leadAssignResponseDtos.get(0).getCreatedDate());
            }

        }
        if (CollectionUtils.isNotEmpty(data.getIndustries())) {
            List<IndustryResponse> industryResponses = data.getIndustries().stream().map(e -> new IndustryResponse(e.getCode(), e.getName())).collect(Collectors.toList());
            response.setIndustries(industryResponses);
        } else response.setIndustries(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(data.getSchedules())) {
            List<ScheduleResponseDto> scheduleResponseDtos = data.getSchedules().stream().map(ScheduleResponseDto::scheduleModelToDto).collect(Collectors.toList());
            response.setSchedules(scheduleResponseDtos);
        }
        return response;
    }
}
