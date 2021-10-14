package shupship.domain.dto;

import lombok.Data;

import java.util.List;


@Data
public class MapDto {
    private Long total;
    private List<AddressMapDto> data;
}
