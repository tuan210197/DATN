package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import shupship.domain.model.*;
import shupship.dto.LeadAssignResponseDto;
import shupship.dto.ScheduleResponseDto;
import shupship.enums.*;
import shupship.util.DateTimeUtils;

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

    private LocalDateTime impactDate;

    private String companyName;

    private String representation;

    private Double quantityMonth;

    private Double weight;

    private String quality;

    private String compensation;

    private String type;

    private String status;

    private String statusDescription;

    private String customerCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressResponse address;

    private Double expectedRevenue;

    private Double inProvincePrice;

    private Double outProvincePrice;

    private String leadSource;

    private String statusLa;

    private String statusDescLa;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String successCusCode;

    public static LeadResponse leadModelToDto(Lead data) {
        LeadResponse response = new LeadResponse();
        response.setId(data.getId());
        response.setFullName(data.getFullName());
        response.setTitle(data.getTitle());
        response.setExpectedRevenue(data.getExpectedRevenue() == null ? 0 :data.getExpectedRevenue());
        response.setCompanyName(data.getCompanyName() == null ? "" :data.getCompanyName());
        response.setRepresentation(data.getRepresentation() == null ? "" :data.getRepresentation());
        response.setQuantityMonth(data.getQuantityMonth() == null ? 0 : data.getQuantityMonth());
        response.setWeight(data.getWeight() == null ? 0 :data.getWeight());
        response.setQuality(data.getQuality() == null ? "" :data.getQuality());
        response.setCompensation(data.getCompensation() == null ? "" :data.getCompensation());
        if (data.getType() != null)
            response.setType(LeadType.getByValue(data.getType()).name());
        response.setCustomerCode(data.getCustomerCode() == null ? "KH".concat(String.valueOf(data.getId())) : data.getCustomerCode());
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
        response.setLeadSource(StringUtils.isNotBlank(data.getLeadSource()) ? data.getLeadSource() : "");

        if (CollectionUtils.isNotEmpty(data.getLeadAssigns())) {
            List<LeadAssign> leadAssign = data.getLeadAssigns().stream().sorted(Comparator.comparing(LeadAssign::getId).reversed()).collect(Collectors.toList());
            List<LeadAssignResponse> leadAssignResponse = leadAssign.stream().map(LeadAssignResponse::leadAssignModelToDto).collect(Collectors.toList());
            response.setLeadAssigns(leadAssignResponse);

            LeadAssign model = leadAssign.get(0);

            response.setStatusLa(LeadStatus.getByValue(model.getStatus()).name());
            response.setStatusDescLa(LeadStatusVi.valueOf(LeadStatus.getByValue(model.getStatus()).name()).getType());

            if (model.getStatus() == 1 || model.getStatus() == 5) {
                response.setStatusLa(LeadStatus.getByValue(LeadStatus.NEW.getType()).name());
                response.setStatusDescLa(LeadStatusVi.valueOf(LeadStatus.NEW.name()).getType());
                response.setCreatedDate(DateTimeUtils.instantToLocalDateTime(model.getCreatedDate()));
                response.setImpactDate(DateTimeUtils.instantToLocalDateTime(model.getCreatedDate()));
            } else if (model.getStatus() == 3 || model.getStatus() == 4) {
                List<Result> listResult = null;
                listResult = model.getLeads().getSchedules().stream().map(Schedule::getResult).collect(Collectors.toList());
                Result result = listResult.get(listResult.size() - 1);
                response.setCreatedDate(DateTimeUtils.instantToLocalDateTime(model.getCreatedDate()));
                if (result != null) {
                    response.setImpactDate(DateTimeUtils.instantToLocalDateTime(result.getCreatedDate()));
                }
            } else {
                if (CollectionUtils.isNotEmpty(model.getLeads().getSchedules())) {
                    Schedule schedule = new Schedule();
                    schedule = model.getLeads().getSchedules().stream().collect(Collectors.toList()).get(0);
                    response.setImpactDate(schedule.getFromDate());
                }
                response.setCreatedDate(DateTimeUtils.instantToLocalDateTime(model.getCreatedDate()));
            }

        } else {
            response.setCreatedDate(DateTimeUtils.instantToLocalDateTime(data.getCreatedDate()));
            response.setLeadAssigns(new ArrayList<>());
        }
        if (CollectionUtils.isNotEmpty(data.getIndustries())) {
            List<IndustryResponse> industryResponses = data.getIndustries().stream().map(e -> new IndustryResponse(e.getCode(), e.getName())).collect(Collectors.toList());
            response.setIndustries(industryResponses);
        } else response.setIndustries(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(data.getSchedules())) {
            List<ScheduleResponseDto> scheduleResponseDtos = data.getSchedules().stream().map(ScheduleResponseDto::scheduleModelToDto).collect(Collectors.toList());
            response.setSchedules(scheduleResponseDtos);
        } response.setSchedules(new ArrayList<>());
        if (data.getStatus().equals(LeadStatus.SUCCESS.getType())) {
            Result result = data.getSchedules().stream().filter(e -> e.getStatus().equals(ScheduleStatus.SUCCESS.getType())).limit(1).map(Schedule::getResult).collect(Collectors.toList()).get(0);
            response.setSuccessCusCode(result.getCustomerCode());
        }
        return response;
    }
}
