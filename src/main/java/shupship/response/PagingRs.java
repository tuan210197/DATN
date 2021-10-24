package shupship.response;

import lombok.Data;

import java.util.Collection;

@Data
public class PagingRs {
    private long totalItem;
    private int totalPage;
    Collection<?> data;
}
