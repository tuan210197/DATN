package shupship.service;

import shupship.domain.model.District;

import java.util.List;

public interface IDistrictService {
    List<District> getDistrictByProvinceCode(String provinceCode);
}
