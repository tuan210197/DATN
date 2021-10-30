package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.DeptOffice;
import shupship.repo.IDeptOffciveRepository;
import shupship.service.IDeptService;

import java.util.List;
@Service
public class DeptService implements IDeptService {

    @Autowired
    IDeptOffciveRepository deptOffciveRepository;

    @Override
    public List<DeptOffice> getListDeptCode() {
        return (List<DeptOffice>) deptOffciveRepository.findAll();
    }
}
