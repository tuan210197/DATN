package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.DeptOffice;
import shupship.repo.DeptOffciveRepository;
import shupship.service.DeptService;

import java.util.List;
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptOffciveRepository deptOffciveRepository;

    @Override
    public List<DeptOffice> getAll() {

        return deptOffciveRepository.findAll();



    }
}
