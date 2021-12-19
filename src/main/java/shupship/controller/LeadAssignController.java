package shupship.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shupship.common.Constants;
import shupship.domain.model.LeadAssign;
import shupship.domain.model.LeadAssignHis;
import shupship.domain.model.Users;
import shupship.request.LeadAssignRequestV2;
import shupship.response.*;
import shupship.service.ILeadAssignService;
import shupship.service.ILeadService;
import shupship.util.FileStorageService;
import shupship.util.exception.HieuDzException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lead-assigns")
@Slf4j
public class LeadAssignController extends BaseController {

    @Autowired
    private ILeadAssignService leadAssignService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity createLeadAssign(HttpServletRequest request, @Valid @RequestBody LeadAssignRequestV2 inputData) {
        Users users = getCurrentUser();
        List<LeadAssign> leadAssignList = leadAssignService.createLeadAssignV2(users, inputData);
        List<LeadAssignResponse> response = leadAssignList.stream().map(LeadAssignResponse::leadAssignModelToDto).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/import")
    public RestResponse importLeadAssign(HttpServletRequest request, @RequestParam("file") MultipartFile reapExcelDataFile) throws Exception {

        RestResponse rest = new RestResponse();
        String message = leadAssignService.checkFileInput(reapExcelDataFile);
        if (StringUtils.isEmpty(message)) {
            Users user = getCurrentUser();
            LeadAssignHisResponse listRes = leadAssignService.importFileLeadAssign(user, reapExcelDataFile);
            rest.setData(listRes);
            rest.setStatus(listRes != null ? "Thành công ❤❤❤" : "Thất bại ❤❤❤");
        } else {
            rest.setData(message);
            rest.setStatus("Thất bại");
        }
        return rest;
    }


    @GetMapping(value = "/history")
    public ResponseEntity getListFileHis(@PageableDefault(page = 1)
                                         @SortDefault.SortDefaults({@SortDefault(sort = "createdDate", direction = Sort.Direction.DESC)}) Pageable pageable) {
        Users users = getCurrentUser();
        Pageable pageablerequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        if (users.getRoles().equals("TCT") || users.getRoles().equals("CN") || users.getRoles().equals("BC")) {
            PagingRs list = leadAssignService.getLeadAssignHis(users, pageablerequest);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else throw new HieuDzException("Không có quyền");

    }

    @GetMapping(value = "/import/{fileId}")
    public ResponseEntity getDetailFile(@PathVariable Long fileId) {
        LeadAssignHisResponse leadAssignHisResponse;
        if (fileId != null)
            leadAssignHisResponse = leadAssignService.getDetailFile(fileId);
        else
            throw new HieuDzException("FileId không được để trống");
        return new ResponseEntity<>((Optional.ofNullable(leadAssignHisResponse)), HttpStatus.OK);
    }


    @GetMapping(value = "/exportDetailFile/{fileId}")
    public ResponseEntity<Resource> exportDetailFile(HttpServletRequest request,
                                                     @PathVariable(name = "fileId") Long fileId) throws Exception {
        Resource resource = new ClassPathResource("Giao-tiep-xuc-khach-hang-mau-sup-ship.xlsx");
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Lỗi");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() + "")
                .body(resource);
    }

    @GetMapping(value = "/exportDetailFileError/{fileId}")
    public ResponseEntity<Resource> exportDetailFileError(HttpServletRequest request,
                                                          @RequestParam(required = true) String type,
                                                          @PathVariable(name = "fileId") Long fileId) throws Exception {
        Resource resource = new ClassPathResource("Giao-tiep-xuc-khach-hang-mau-sup-ship.xlsx");
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Lỗi");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() + "")
                .body(resource);
    }

    @GetMapping(value = "/exportDetailFileSus/{fileId}")
    public ResponseEntity<Resource> exportDetailFileSus(HttpServletRequest request,
                                                        @RequestParam(required = true) String type,
                                                        @PathVariable(name = "fileId") Long fileId) throws Exception {
        Resource resource = new ClassPathResource("Giao-tiep-xuc-khach-hang-mau-sup-ship.xlsx");
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Lỗi");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() + "")
                .body(resource);
    }

    @GetMapping(value = "/templates/download")
    public ResponseEntity<Resource> fileUpload(HttpServletRequest request) throws Exception {
        Resource resource = new ClassPathResource("Giao-tiep-xuc-khach-hang-mau-sup-ship.xlsx");
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Lỗi");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() + "")
                .body(resource);
    }

    @GetMapping(value = "/export-excel")
    public ResponseEntity<Resource> exportExcel(@RequestParam(required = true) Long fileId,
                                                @RequestParam(required = false) String status) throws Exception {

        LeadAssignHisResponse leadAssignHisResponse = leadAssignService.getDetailFile(fileId);
        Collection<LeadAssignExcelResponse> leadAssignExcelResponses = leadAssignHisResponse.getLeadsAssignByExcel();
        if (Long.parseLong(status) == 0)
            leadAssignExcelResponses = leadAssignExcelResponses.stream().filter(e -> e.getStatus() == 0).collect(Collectors.toList());
        else if (Long.parseLong(status) == 1)
            leadAssignExcelResponses = leadAssignExcelResponses.stream().filter(e -> e.getStatus() == 1).collect(Collectors.toList());
        ByteArrayInputStream in = leadAssignService.exportExcel(leadAssignExcelResponses);
        String fileName = "CHIIETFILE" + ".xlsx";
        File targetFile = new File("data/" + fileName);
        FileUtils.copyInputStreamToFile(in, targetFile);
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + resource.getFilename() + "");
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

}
