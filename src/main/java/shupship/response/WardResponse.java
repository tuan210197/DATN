package shupship.response;

import lombok.Data;
import shupship.domain.model.Ward;

@Data
public class WardResponse {
    private String wardId;
    private String wardCode;
    private String wardName;

    public static WardResponse leadModelToDto(Ward ward){
        WardResponse model = new WardResponse();
        model.setWardId(ward.getWardId());
        model.setWardCode(ward.getWardCode());
        model.setWardName(ward.getWardName());
        return model;
    }
}
