package shupship.service;

import org.springframework.stereotype.Service;
import shupship.domain.model.DeptOffice;

import java.util.List;
@Service
public interface DeptService {

    List<DeptOffice> getAll();
}
