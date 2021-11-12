package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.model.DeptOffice;
import shupship.repo.IDeptOffciveRepository;
import shupship.response.DeptOfficeResponse;
import shupship.service.IDeptService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/dept")
public class DeptController extends BaseController {

    @Autowired
    private IDeptService deptService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllDept(HttpServletRequest request) {
       List<DeptOffice> deptOfficeList = deptService.getListDeptCode();
       List<DeptOfficeResponse> deptOfficeResponses = deptOfficeList.stream().map(DeptOfficeResponse::leadModelToDto).collect(Collectors.toList());
       return new ResponseEntity<>(deptOfficeResponses, HttpStatus.OK);
    }
}