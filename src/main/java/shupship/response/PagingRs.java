package shupship.response;

import lombok.Data;
import shupship.domain.model.Lead;

import java.util.Collection;
import java.util.List;

@Data
public class PagingRs {
    private long totalItem;
    private int totalPage;
    Collection<?> data;
}
