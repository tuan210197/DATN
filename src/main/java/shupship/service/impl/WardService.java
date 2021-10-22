package shupship.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.Ward;
import shupship.repo.IWardRepository;
import shupship.service.IWardService;

import java.util.List;

@Service
public class WardService implements IWardService {

    @Autowired
    IWardRepository wardRepository;

    @Override
    public List<Ward> getWardByDistrictCode(String districtCode) {
        return wardRepository.getWardByDistrictCode(districtCode);
    }
}
