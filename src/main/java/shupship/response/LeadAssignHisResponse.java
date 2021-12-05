package shupship.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import shupship.domain.model.LeadAssignHis;
import shupship.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class LeadAssignHisResponse {

    private Long id;

    private String fileName;

    private Long total;

    private Long totalValid;

    private Long totalInvalid;

    private Long status;

    private Long empId;

    private LocalDateTime createdDate;

    private Collection<LeadAssignExcelResponse> leadsAssignByExcel;

    public static LeadAssignHisResponse leadAssignHisToDto(LeadAssignHis his) {
        LeadAssignHisResponse res = new LeadAssignHisResponse();
        res.setId(his.getId());
        res.setFileName(his.getFileName());
        res.setTotal(his.getTotal());
        res.setTotalValid(his.getTotalValid());
        res.setTotalInvalid(his.getTotalInvalid());
        res.setStatus(his.getStatus());
        if (his.getFileCreator() != null && his.getFileCreator().getEmpSystemId() != null) {
            res.setEmpId(his.getFileCreator().getEmpSystemId());
        } else res.setEmpId(null);
        res.setCreatedDate(his.getCreatedDate());
        return res;
    }

    public static LeadAssignHisResponse leadAssignHisToExcel(LeadAssignHis his) {
        LeadAssignHisResponse res = new LeadAssignHisResponse();
        res.setId(his.getId());
        res.setFileName(his.getFileName());
        res.setTotal(his.getTotal());
        res.setTotalValid(his.getTotalValid());
        res.setTotalInvalid(his.getTotalInvalid());
        res.setStatus(his.getStatus());
        if (his.getFileCreator() != null && his.getFileCreator().getEmpSystemId() != null) {
            res.setEmpId(his.getFileCreator().getEmpSystemId());
        } else res.setEmpId(null);
        res.setCreatedDate(his.getCreatedDate());
        if (CollectionUtils.isNotEmpty(his.getLeadAssignExcels())) {
            List<LeadAssignExcelResponse> leadAssignExcelResponseDtoList = his.getLeadAssignExcels().stream().map(LeadAssignExcelResponse::leadAssignExcelModelToDto).collect(Collectors.toList());
            res.setLeadsAssignByExcel(leadAssignExcelResponseDtoList);
        }
        return res;
    }
}
