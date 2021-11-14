package shupship.service.impl;

import org.springframework.stereotype.Service;
import shupship.domain.dto.CommonCodeResponseDto;
import shupship.domain.model.CommonCode;
import shupship.domain.model.Users;
import shupship.service.ICommonService;

import java.util.List;

@Service
public class CommonService implements ICommonService {
    @Override
    public List<CommonCodeResponseDto> findCommonCodeByClassCd(String classCd) throws Exception {
        return null;
    }

    @Override
    public CommonCodeResponseDto findCommonCodeByClassCdAndExtValue(String classCd, Long extValue) {
        return null;
    }

    @Override
    public List<CommonCode> getCommonCodeByClassCdStr(Users user, String... classCdStr) throws Exception {
        return null;
    }
}
