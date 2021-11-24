package shupship.helper;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

@Data
public class PagingRs {
    private long totalCount;
    private Long totalDept;
    private Long totalPost;
    private Long totalCountEmp;
    private Long totalAssigns;
    private Long totalSuccesses;
    private Long totalContacting;
    private Long totalFails;
    private Long totalEmployeeNotAssigned;
    private Long totalAssigned;
    private Collection<?> data;
    private NavigationInfo paging;

    public void setPaging(Pageable pageable, Page page, String baseUrl) {
        setPaging(NavigationUtil.generateNavigationInfo(pageable, page, baseUrl));
    }
}
