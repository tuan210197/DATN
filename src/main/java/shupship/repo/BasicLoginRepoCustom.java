package shupship.repo;


import shupship.domain.dto.UserInfoDTO;

public interface BasicLoginRepoCustom {

    UserInfoDTO getActiveUserByEmail(String email);

}
