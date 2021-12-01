package shupship.response;

import lombok.Data;
import shupship.domain.model.DeptOffice;
import shupship.domain.model.District;


@Data
public class DeptOfficeResponse {
    private Long id;
    private String deptCode;
    private String deptName;

    public static DeptOfficeResponse leadModelToDto(DeptOffice deptOffice){
        DeptOfficeResponse model = new DeptOfficeResponse();
        model.setId(deptOffice.getId());
        model.setDeptCode(deptOffice.getCode());
        model.setDeptName(deptOffice.getDeptName());
        return model;
    }
}
