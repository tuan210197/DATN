package shupship.service.mapper;


import org.mapstruct.Mapper;
import shupship.domain.dto.UserInfoDTO;
import shupship.domain.model.Users;



@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toEntity(UserInfoDTO dto);

    UserInfoDTO toDto(Users entity);


}
