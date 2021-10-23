package shupship.service;

import shupship.domain.model.Ward;

import java.util.List;

public interface IWardService {
    List<Ward> getWardByDistrictCode(String districtCode);
}
