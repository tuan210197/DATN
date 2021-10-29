package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.dto.UserInfoDTO;
import shupship.service.UserService;
import shupship.service.mapper.UserMapper;
import shupship.util.CriteriaUtil;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper mapper;

    @GetMapping("/list")
    public ResponseEntity<Page<UserInfoDTO>> list(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "asc", required = false) String asc,
            @RequestParam(value = "desc", required = false) String desc) {
//        securityService.checkPermission(getUserRoles(), Constant.Function.USER_LIST);

        Pageable pageable = PageRequest.of(page, size, CriteriaUtil.sort(asc, desc));
        Page<UserInfoDTO> pages = userService.list(pageable).map(mapper::toDto);
        return ResponseEntity.ok(pages);
    }

}
