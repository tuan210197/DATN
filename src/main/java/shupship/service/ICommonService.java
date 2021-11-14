package shupship.service;

import org.springframework.stereotype.Service;
import shupship.domain.dto.CommonCodeResponseDto;
import shupship.domain.model.CommonCode;
import shupship.domain.model.Users;

import java.util.List;

public interface ICommonService {

    List<CommonCodeResponseDto> findCommonCodeByClassCd(String classCd) throws Exception;

    CommonCodeResponseDto findCommonCodeByClassCdAndExtValue(String classCd, Long extValue);

    List<CommonCode> getCommonCodeByClassCdStr(Users user, String... classCdStr) throws Exception;
}
