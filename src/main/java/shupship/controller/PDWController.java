package shupship.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shupship.domain.model.*;
import shupship.repo.IIndustryRepository;
import shupship.repo.IPostOfficeRepository;
import shupship.repo.IWardRepository;
import shupship.response.*;
import shupship.service.IDistrictService;
import shupship.service.IProvinceService;
import shupship.util.exception.ErrorMessage;
import shupship.util.exception.HieuDzException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PDWController extends BaseController{

    @Autowired
    IProvinceService provinceService;

    @Autowired
    IDistrictService districtService;

    @Autowired
    IWardRepository wardRepository;

    @Autowired
    IIndustryRepository industryRepository;

    @Autowired
    IPostOfficeRepository postOfficeRepository;

    @GetMapping(value = "/province")
    public ResponseEntity getListProvince(HttpServletRequest request) {
        List<Province> list = provinceService.listProvince();
        List<ProvinceResponse> listProvinceResponses = list.stream().map(ProvinceResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(listProvinceResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/district")
    public ResponseEntity getDistrictbyProvince(HttpServletRequest request, @RequestParam(required = true) String provinceCode) {

        if (StringUtils.isEmpty(provinceCode))
            throw new HieuDzException("Bắt buộc phải chọn Tỉnh/TP");
        List<District> districts = districtService.getDistrictByProvinceCode(provinceCode);
        List<DistrictResponse> districtResponses = districts.stream().map(DistrictResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(districtResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/ward")
    public ResponseEntity getWardByDistrict(HttpServletRequest request, @RequestParam(required = true) String districtCode) {

        if (StringUtils.isEmpty(districtCode))
            throw new HieuDzException("Bắt buộc phải chọn Quận/Huyện");
        List<Ward> wards = wardRepository.getWardByDistrictCode(districtCode);
        List<WardResponse> wardResponses = wards.stream().map(WardResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(wardResponses, HttpStatus.OK);
    }

    @GetMapping(value = "/industry")
    public ResponseEntity getIndustry(HttpServletRequest request) {
        List<Industry> industries = industryRepository.findAll();
        List<IndustryResponse> industries1 = industries.stream().map(IndustryResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(industries1, HttpStatus.OK);
    }

    @GetMapping(value = "/fillCbx")
    public ResponseEntity fillCombobox(){
        Users users = getCurrentUser();
        PostOffice postOffice = postOfficeRepository.findPostOfficeByCode(users.getPostCode());
        FillCbx fillCbx = new FillCbx();
        fillCbx.setDeptCode(postOffice.getDeptOffice().getCode());
        fillCbx.setDeptId(postOffice.getDeptOffice().getId());
        fillCbx.setPostCode(postOffice.getPostCode());
        fillCbx.setPostId(postOffice.getId());
        return new ResponseEntity<>(fillCbx, HttpStatus.OK);
    }

}
