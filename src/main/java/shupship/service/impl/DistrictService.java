package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.District;
import shupship.repo.IDistrictRepository;
import shupship.service.IDistrictService;

import java.util.List;

@Service
public class DistrictService implements IDistrictService {


    @Autowired
    IDistrictRepository districtRepository;

    @Override
    public List<District> getDistrictByProvinceCode(String provinceCode) {
        return districtRepository.getDistrictByProvinceCode(provinceCode);
    }
}
