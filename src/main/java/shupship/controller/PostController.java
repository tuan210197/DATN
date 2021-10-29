package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shupship.domain.model.PostOffice;
import shupship.service.PostService;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/{dept_id}/list")
    public ResponseEntity<?> getPostByDeptId(@PathVariable Long dept_id) {
        Optional<PostOffice> postOffices = postService.getPostByDeptId(dept_id);
        return ResponseEntity.ok(postOffices);
    }

}
