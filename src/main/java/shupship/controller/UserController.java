package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.dto.UserInfoDTO;
import shupship.domain.model.Users;
import shupship.repo.UserRepository;
import shupship.response.UsersResponse;
import shupship.service.UserService;
import shupship.service.mapper.UserMapper;
import shupship.util.CriteriaUtil;
import shupship.util.exception.HieuDzException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    @GetMapping(value = "/list")
    public ResponseEntity<Page<UserInfoDTO>> list(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "asc", required = false) String asc,
            @RequestParam(value = "desc", required = false) String desc) {


        Pageable pageable = PageRequest.of(page, size, CriteriaUtil.sort(asc, desc));
        Users user = getCurrentUser();
        Page<UserInfoDTO> pages = userService.list(user.getRoles(), user.getDeptCode(),user.getPostCode(),pageable).map(mapper::toDto);
        return ResponseEntity.ok(pages);
    }

    @GetMapping()
    public ResponseEntity<?> searchUser( @RequestParam("keyword") String keyword) {
        Users user = getCurrentUser();
        List<Users> users = userService.searchUser(user.getRoles(), user.getDeptCode(),user.getPostCode(),keyword);
        List<UserInfoDTO> list = users.stream().map(mapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/postcode")
    public ResponseEntity getUsersByPostCode(@RequestParam(required = true) String postCode){
        if (StringUtils.isEmpty(postCode))
            throw new HieuDzException("Bắt buộc phải chọn Bưu cục");
        List<Users> listUsers = userService.getUsersByPostCode(postCode);
        List<UsersResponse> usersResponses = listUsers.stream().map(UsersResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(usersResponses, HttpStatus.OK);
    }

}
