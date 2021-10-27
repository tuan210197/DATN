package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shupship.service.DeptService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/list")
    public void getAllDept(HttpServletRequest request) {
        deptService.getAll();
    }
}