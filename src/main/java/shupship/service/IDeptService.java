package shupship.service;

import org.springframework.stereotype.Service;
import shupship.domain.model.DeptOffice;

import java.util.List;


public interface IDeptService {
    List<DeptOffice> getListDeptCode();
}
