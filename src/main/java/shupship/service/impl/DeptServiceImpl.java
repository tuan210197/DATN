package shupship.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.dto.DeptOfficeDTO;
import shupship.domain.model.DeptOffice;
import shupship.domain.model.Users;
import shupship.repo.DeptOffciveRepository;
import shupship.service.DeptService;

import java.util.List;
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptOffciveRepository deptOffciveRepository;

    @Override
    public List<DeptOfficeDTO> getAll() {

        DeptOffice deptOffice = (DeptOffice) deptOffciveRepository.getAllDeptOffice();

        DeptOfficeDTO dto = new DeptOfficeDTO();
        BeanUtils.copyProperties(deptOffice, dto);

        dto.setDeptCode(deptOffice.getDeptCode());
        dto.setDeptName(deptOffice.getDeptName());
        return (List<DeptOfficeDTO>) dto;
    }
}
