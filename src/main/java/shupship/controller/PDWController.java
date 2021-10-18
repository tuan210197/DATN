package shupship.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.domain.model.Province;
import shupship.response.LeadResponse;
import shupship.response.ProvinceResponse;
import shupship.service.IProvinceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PDWController {

    @Autowired
    IProvinceService provinceService;

    @GetMapping(value = "/province")
    public ResponseEntity getListProvince(){
        List<Province> list = provinceService.listProvince();
        List<ProvinceResponse> listProvinceResponses = list.stream().map(ProvinceResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(listProvinceResponses, HttpStatus.OK);
    }

}
