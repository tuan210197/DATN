package shupship.response;

import lombok.Data;
import shupship.domain.model.DeptOffice;
import shupship.domain.model.PostOffice;


@Data
public class PostOfficeResponse {
    private Long id;
    private String postCode;
    private String postName;

    public static PostOfficeResponse leadModelToDto(PostOffice postOffice){
        PostOfficeResponse model = new PostOfficeResponse();
        model.setId(postOffice.getId());
        model.setPostCode(postOffice.getPostCode());
        model.setPostName(postOffice.getPostName());
        return model;
    }
}
