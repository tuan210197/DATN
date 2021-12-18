package shupship.response;

import lombok.Data;
import shupship.domain.model.Industry;

@Data
public class FillCbx {

    private Long deptId;
    private Long postId;
    private String deptCode;
    private String postCode;

}
