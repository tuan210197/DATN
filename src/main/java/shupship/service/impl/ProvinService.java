package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.Province;
import shupship.repo.IProvinceRepository;
import shupship.service.IProvinceService;

import java.util.List;

@Service
public class ProvinService implements IProvinceService {

    @Autowired
    IProvinceRepository provinceRepository;

    @Override
    public List<Province> listProvince() {
        return (List<Province>) provinceRepository.findAll();
    }
}
