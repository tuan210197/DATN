package shupship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.dto.IndustryResponseDto;
import shupship.domain.model.Lead;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadStatusVi;
import shupship.enums.LeadType;
import shupship.response.AddressResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadResponseWithDescriptionDto {
    private long id;

    private String title;

    private String fullName;

    private String phone;

    private String companyName;

    private String representation;

    private Double quantityMonth;

    private Double weight;

    private String quality;

    private String compensation;

    private String payment;

    private String other;

    private String status;

    private String statusDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressResponse address;

    private Long annualQuantity;

    private Double expectedRevenue;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private String leadSource;

    private String type;

    private String customerCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<IndustryResponseDto> industries;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ScheduleResponseLeadDto> schedules;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LeadAssignResponseDto> leadAssigns;

    public static LeadResponseWithDescriptionDto leadModelToDto(Lead data) {

        LeadResponseWithDescriptionDto response = new LeadResponseWithDescriptionDto();
        response.setId(data.getId());
        response.setFullName(data.getFullName());
        response.setTitle(data.getTitle());
        response.setExpectedRevenue(data.getExpectedRevenue());
        response.setAnnualQuantity(data.getAnnualQuantity());
        response.setCompanyName(data.getCompanyName());
        response.setRepresentation(data.getRepresentation());
        response.setQuantityMonth(data.getQuantityMonth());
        response.setWeight(data.getWeight());
        response.setQuality(data.getQuality());
        response.setCompensation(data.getCompensation());
        response.setOther(data.getOther());
        response.setType(LeadType.getByValue(data.getType()).name());
        response.setCustomerCode(data.getCustomerCode());
        response.setInProvincePrice(data.getInProvincePrice());
        response.setOutProvincePrice(data.getOutProvincePrice());
        if (data.getStatus().equals(LeadStatus.NOT_CONTACTED.getType()) && data.getIsFromEVTP() != null && data.getIsFromEVTP().equals(1L)) {
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
            addressResponse.setFomatAddress(data.getAddress().getFomatAddress() == null ? "" : data.getAddress().getFomatAddress());
            response.setAddress(addressResponse);
        }
        response.setPhone(data.getPhone());
        response.setLeadSource(StringUtils.isNotBlank(data.getLeadSource()) ? data.getLeadSource(): "");

        if (CollectionUtils.isNotEmpty(data.getIndustries())) {
            List<IndustryResponseDto> industryResponses = data.getIndustries().stream().map(e -> new IndustryResponseDto(e.getId(), e.getCode(), e.getName(), e.getDescription())).collect(Collectors.toList());
            response.setIndustries(industryResponses);
        } else response.setIndustries(new ArrayList<>());

        if (CollectionUtils.isNotEmpty(data.getSchedules())) {
            List<ScheduleResponseLeadDto> scheduleResponsDtos = data.getSchedules().stream().map(ScheduleResponseLeadDto::scheduleModelToDto).collect(Collectors.toList());
            response.setSchedules(scheduleResponsDtos);
        } response.setSchedules(new ArrayList<>());

        if (CollectionUtils.isNotEmpty(data.getLeadAssigns())) {
            List<LeadAssignResponseDto> leadAssignResponseDtos = data.getLeadAssigns().stream().map(LeadAssignResponseDto::leadAssignModelToDto).collect(Collectors.toList());
            response.setLeadAssigns(leadAssignResponseDtos);
        }
        return response;
    }
}
