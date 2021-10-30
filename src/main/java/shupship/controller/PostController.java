package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.model.PostOffice;
import shupship.response.PostOfficeResponse;
import shupship.service.IPostService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @GetMapping("/list/{deptId}")
    public ResponseEntity getPostByDeptId(@PathVariable Long deptId) {
        List<PostOffice> postOffices = postService.getPostByDeptId(deptId);
        List<PostOfficeResponse> postOfficeResponses = postOffices.stream().map(PostOfficeResponse::leadModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(postOfficeResponses, HttpStatus.OK);
    }
}
