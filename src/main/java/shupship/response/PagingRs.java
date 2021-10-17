package shupship.response;

import lombok.Data;

import java.util.Collection;

@Data
public class PagingRs {
    private int totalItem;
    private int totalPage;
    Collection<?> data;
}
