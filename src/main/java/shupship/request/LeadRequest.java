package shupship.request;

import lombok.Data;
import shupship.domain.model.Lead;

@Data
public class LeadRequest extends Lead{
    private int pageSize;
    private int page;

}
