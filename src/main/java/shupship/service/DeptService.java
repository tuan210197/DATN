package shupship.service;

import org.springframework.stereotype.Service;
import shupship.domain.dto.DeptOfficeDTO;
import shupship.domain.model.DeptOffice;
import shupship.domain.model.Users;

import java.util.List;
@Service
public interface DeptService {

    List<DeptOfficeDTO> getAll();
}
