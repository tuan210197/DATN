package shupship.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shupship.domain.model.District;
import shupship.domain.model.Province;
import shupship.domain.model.Ward;
import shupship.exception.ErrorMessage;
import shupship.exception.HieuDzException;
import shupship.repo.IWardRepository;
import shupship.response.DistrictResponse;
import shupship.response.LeadResponse;
import shupship.response.ProvinceResponse;
import shupship.response.WardResponse;
import shupship.service.IDistrictService;
import shupship.service.IProvinceService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PDWController {

    @Autowired
    IProvinceService provinceService;

    @Autowired
    IDistrictService districtService;

    @Autowired
    IWardRepository wardRepository;

    @GetMapping(value = "/province")
    public ResponseEntity getListProvince(){
        List<Province> list = provinceService.listProvince();
        List<ProvinceResponse> listProvinceResponses = list.stream().map(ProvinceResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(listProvinceResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/district")
    public ResponseEntity getDistrictbyProvince(@RequestParam(required = true) String provinceCode){

        if (StringUtils.isEmpty(provinceCode))
            throw new HieuDzException(new ErrorMessage("ERR001","Bắt buộc phải chọn Tỉnh/TP"));
        List<District> districts = districtService.getDistrictByProvinceCode(provinceCode);
        List<DistrictResponse> districtResponses = districts.stream().map(DistrictResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(districtResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/ward")
    public ResponseEntity getWardByDistrict(@RequestParam(required = true) String districtCode){

        if (StringUtils.isEmpty(districtCode))
            throw new HieuDzException(new ErrorMessage("ERR001","Bắt buộc phải chọn Quận/Huyện"));
        List<Ward> wards = wardRepository.getWardByDistrictCode(districtCode);
        List<WardResponse> wardResponses = wards.stream().map(WardResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(wardResponses, HttpStatus.OK);
    }

}
