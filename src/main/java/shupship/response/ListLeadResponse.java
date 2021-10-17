package shupship.response;

import lombok.Data;
import shupship.domain.model.Lead;

import java.util.List;
@Data
public class ListLeadResponse {
    List<LeadResponse> leadList;
    private int totalItem;
    private int totalPage;

    public void setLeadList(List<Lead> listLead) {
    }
}
