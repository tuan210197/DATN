package shupship.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.Lead;
import shupship.dto.LeadAssignResponseDto;
import shupship.dto.ScheduleResponseDto;
import shupship.enums.LeadSource;
import shupship.enums.LeadStatus;
import shupship.enums.LeadStatusVi;
import shupship.enums.LeadType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadResponseDto {
    private long id;

    private String title;

    private String fullName;

    private String phone;

    private String unmaskPhone;

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
    private AddressResponseDto address;

    private Long annualQuantity;

    private Double expectedRevenue;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private String leadSource;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<CompetitorResponseDto> competitors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<IndustryResponseDto> industries;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<ProductResponseDto> products;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ScheduleResponseDto> schedules;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LeadAssignResponseDto> leadAssigns;

    public static LeadResponseDto leadModelToDto(Lead data) {
        LeadResponseDto response = new LeadResponseDto();
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
        ///response.setPayment(data.getPayment());
        response.setOther(data.getOther());
        if (data.getType() != null)
            response.setType(LeadType.getByValue(data.getType()).name());
        response.setCustomerCode(data.getCustomerCode());
        response.setInProvincePrice(data.getInProvincePrice());
        response.setOutProvincePrice(data.getOutProvincePrice());
        if (data.getStatus().equals(LeadStatus.NOT_CONTACTED.getType())) {
            response.setStatus(LeadStatus.NEW.name());
            response.setStatusDescription(LeadStatusVi.valueOf(LeadStatus.getByValue(1L).name()).getType());
        } else {
            response.setStatus(LeadStatus.getByValue(data.getStatus()).name());
            response.setStatusDescription(LeadStatusVi.valueOf(LeadStatus.getByValue(data.getStatus()).name()).getType());
        }
        if (data.getAddress() != null) {
            response.setAddress(new AddressResponseDto(
                    data.getAddress().getHomeNo().replaceAll(data.getAddress().getHomeNo(), "*"),
                    data.getAddress().getStreet().replaceAll(data.getAddress().getStreet(), "*"),
                    data.getAddress().getDistrict(), data.getAddress().getProvince()));
        }
        response.setPhone(data.getPhone().replaceAll(".(?=.{4})", "*"));
        response.setUnmaskPhone(data.getPhone());
        response.setLeadSource(StringUtils.isNotBlank(data.getLeadSource()) ? LeadSource.valueOf(data.getLeadSource()).getValue() : "");
        if (data.getLeadAssigns() != null) {
            List<LeadAssignResponseDto> leadAssignResponseDtos = data.getLeadAssigns().stream().map(LeadAssignResponseDto::leadAssignModelToDto).collect(Collectors.toList());
            response.setLeadAssigns(leadAssignResponseDtos);
        }
//        if (CollectionUtils.isNotEmpty(data.getCompetitors())) {
//            List<CompetitorResponseDto> competitorResponseDtos = data.getCompetitors().stream().map(e -> new CompetitorResponseDto(e.getId(), e.getCode(), e.getName(), e.getDescription(), e.getStatus())).collect(Collectors.toList());
//            response.setCompetitors(competitorResponseDtos);
//        } else response.setCompetitors(new ArrayList<>());
//        if (CollectionUtils.isNotEmpty(data.getProducts())) {
//            List<ProductResponseDto> productResponses = data.getProducts().stream().map(e -> new ProductResponseDto(e.getId(), e.getCode(), e.getName(), e.getType(), e.getDescription(), e.getStatus())).collect(Collectors.toList());
//            response.setProducts(productResponses);
//        } else response.setProducts(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(data.getIndustries())) {
            List<IndustryResponseDto> industryResponses = data.getIndustries().stream().map(e -> new IndustryResponseDto(e.getId(), e.getCode(), e.getName(), e.getDescription())).collect(Collectors.toList());
            response.setIndustries(industryResponses);
        } else response.setIndustries(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(data.getSchedules())) {
            List<ScheduleResponseDto> scheduleResponseDtos = data.getSchedules().stream().map(ScheduleResponseDto::scheduleModelToDto).collect(Collectors.toList());
            response.setSchedules(scheduleResponseDtos);
        }
        return response;
    }
}
