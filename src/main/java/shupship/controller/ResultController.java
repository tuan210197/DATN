package shupship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.dto.ResultResponse;
import shupship.request.ResultRequest;
import shupship.service.IResultService;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    IResultService resultService;

    @PostMapping
    public ResponseEntity createResult(@RequestBody ResultRequest inputData) throws Exception {
        ResultResponse data = resultService.createResult(inputData);
        return new ResponseEntity(data, HttpStatus.CREATED);
    }
}
