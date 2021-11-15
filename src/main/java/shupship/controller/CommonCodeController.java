package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shupship.domain.dto.CommonCodeResponseDto;
import shupship.service.ICommonService;

import java.util.List;

public class CommonCodeController {

    @Autowired
    ICommonService commonService;

    @GetMapping(value = "/{classCd}")
    public ResponseEntity findCommonCodeByClassCd(@PathVariable String classCd) throws Exception {
        List<CommonCodeResponseDto> data = commonService.findCommonCodeByClassCd(classCd);
        return new ResponseEntity(data, HttpStatus.OK);
    }

}
