package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.dto.DeptOfficeDTO;
import shupship.domain.model.DeptOffice;
import shupship.domain.model.Users;
import shupship.service.DeptService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllDept(HttpServletRequest request) {
       List<DeptOfficeDTO> deptOfficeList = deptService.getAll();
       return ResponseEntity.ok(deptOfficeList);
    }
}