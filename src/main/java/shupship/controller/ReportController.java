package shupship.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shupship.common.Constants;
import shupship.dao.ReportDao;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.domain.model.BasicLogin;
import shupship.domain.model.Users;
import shupship.dto.ReportMonthlyDeptDto;
import shupship.dto.ReportMonthlyPostOfficeDto;
import shupship.dto.SchedulesOfEmployeeMonthlyDto;
import shupship.helper.PagingRs;
import shupship.helper.ResponseUtil;
import shupship.repo.BasicLoginRepo;
import shupship.repo.UserRepo;
import shupship.response.ReportEmployeeOnApp;
import shupship.service.IExportService;
import shupship.service.IReportService;
import shupship.util.DateTimeUtils;
import shupship.util.FileStorageService;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    IReportService reportService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    IExportService exportService;

    @Autowired
    BasicLoginRepo basicLoginRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    ReportDao reportDao;

    @GetMapping(value = "")
    public ResponseEntity reportAllCrm(@PageableDefault(page = 1)
                                       @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                       @RequestParam(required = false) String from,
                                       @RequestParam(required = false) String to,
                                       @RequestParam(required = false) String dept) throws Exception {


        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = reportService.reportAllPageable(pageRequest, startTimestamp, endTimestamp, dept);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/export-cn")
    public ResponseEntity<Resource> exportCN(HttpServletRequest request, @RequestParam(required = false) String from,
                                             @RequestParam(required = false) String to,
                                             @RequestParam(required = false) String dept) throws Exception {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }
        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        Users user = getCurrentUser();
        List<ReportMonthlyDeptDto> rs = new ArrayList<>();
        if (user.getRoles().equals("TCT") || user.getRoles().equals("CN") || user.getRoles().equals("BC")) {
            rs = reportDao.reportAllCrm(startTimestamp, endTimestamp);
            if (StringUtils.isNotEmpty(dept))
                rs = rs.stream().filter(Objects::nonNull).filter(e -> e.getDeptCode().equals(dept)).collect(Collectors.toList());
            else if (user.getRoles().equals("CN"))
                rs = rs.stream().filter(Objects::nonNull).filter(e -> e.getDeptCode().equals(user.getDeptCode())).collect(Collectors.toList());
        }

        for(ReportMonthlyDeptDto data : rs){
            Long countPost = 0L;
            Long totalEmployees = 0L;
            Long totalAssigns = 0L;
            Long successes = 0L;
            Long contacting = 0L;
            Long fails = 0L;
            Long employeeNotAssigned = 0L;
            Long assigned = 0L;

            List<ReportMonthlyPostOfficeDto> post = data.getReportMonthlyPostOfficeDtos();
            for (ReportMonthlyPostOfficeDto postOffice : post) {
                countPost += 1;
                totalEmployees += postOffice.getTotalEmployees();
                totalAssigns += postOffice.getTotalAssigns();
                successes += postOffice.getSuccesses();
                contacting += postOffice.getContacting();
                fails += postOffice.getFails();
                employeeNotAssigned += postOffice.getEmployeeNotAssigned();
                assigned += postOffice.getAssigned();
            }
            data.setTotalPost(countPost);
            data.setTotalCountEmp(totalEmployees);
            data.setTotalAssigns(totalAssigns);
            data.setTotalSuccesses(successes);
            data.setTotalContacting(contacting);
            data.setTotalFails(fails);
            data.setTotalEmployeeNotAssigned(employeeNotAssigned);
            if (totalAssigns != 0){
                data.setTyHT((fails + successes) * 100 / totalAssigns);
                data.setTyTX((contacting) * 100 / totalAssigns);
            } else {
                data.setTyTX(0L);
                data.setTyHT(0L);
            }
            data.setTotalLeadNotTX(totalAssigns - assigned);
            data.setTotalAssigned(assigned);
        }

        ByteArrayInputStream bais = exportService.exportChiNhanh(rs);
        String fileName = "BaoCaoTXChiNhanh" + ".xlsx";
        File targetFile = new File("data/" + fileName);
        FileUtils.copyInputStreamToFile(bais, targetFile);
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


    @GetMapping(value = "/post")
    public ResponseEntity reportByPostCodeOnEVTP(@PageableDefault(page = 1)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                                 @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) String post) throws Exception {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);


        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = reportService.reportOnEVTPByPostCode(pageRequest, startTimestamp, endTimestamp, post);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/emp")
    public ResponseEntity reportByEmployeeOnEVTP(@PageableDefault(page = 1)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                                 @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) Long id) throws Exception {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        SchedulesOfEmployeeMonthlyDto pagingRs = reportService.reportByEmployeeOnEVTP(pageRequest, startDate, endDate, id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/dept/emp")
    public ResponseEntity reportAllEmpsInDept(@PageableDefault(page = 1)
                                              @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                              @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) String deptCode) throws Exception {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");

        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }

        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, Constants.PAGE_SIZE, pageable.getSort());
        PagingRs pagingRs = reportService.reportAllEmpsInDept(pageRequest, startTimestamp, endTimestamp, deptCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/reportEmp")
    public ResponseEntity reportOfEmployee(@PageableDefault(page = 1)
                                              @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)}) Pageable pageable,
                                              @RequestParam(required = false) String month) throws Exception {

        Users users = getCurrentUser();

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        String from = "01-".concat(month);

        if (StringUtils.isBlank(month))
            throw new HieuDzException("Tháng không được để trống!");

        if (StringUtils.isNotBlank(from)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = startDate.plusDays(1).toLocalDate().atStartOfDay();
        }

        assert endDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");

        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        ReportEmployeeOnApp pagingRs = reportService.reportEmployeeOnApp(startTimestamp, endTimestamp, users.getEmpSystemId());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pagingRs));
    }

    @GetMapping(value = "/export-excel")
    public ResponseEntity<Resource> exportExcel(HttpServletRequest request, @RequestParam(required = false) String from,
                                                @RequestParam(required = false) String to,
                                                @RequestParam(required = false) String dept) throws Exception {
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

    @GetMapping(value = "/export-route")
    public ResponseEntity<Resource> exportRoute(HttpServletRequest request, @RequestParam(required = false) String from,
                                                @RequestParam(required = false) String to,
                                                @RequestParam(required = false) String dept) throws Exception {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }
        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        Users user = getCurrentUser();



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

    @GetMapping(value = "/export-bc")
    public ResponseEntity<Resource> exportBC(HttpServletRequest request,
                                             @RequestParam(required = false) String from, @RequestParam(required = false) String to,
                                             @RequestParam(required = false) String dept) throws Exception {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isBlank(from) && !DateTimeUtils.isValidDate(from) && StringUtils.isBlank(to) && !DateTimeUtils.isValidDate(to))
            throw new HieuDzException("Ngày nhập vào không đúng định dạng!");
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            startDate = DateTimeUtils.StringToLocalDate(from).atStartOfDay();
            endDate = DateTimeUtils.StringToLocalDate(to).plusDays(1).atStartOfDay();
        }
        assert startDate != null;
        if (startDate.isAfter(endDate))
            throw new HieuDzException("Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        Users user = getCurrentUser();
        List<ReportMonthlyDeptDto> rs = new ArrayList<>();
        if (user.getRoles().equals("TCT") || user.getRoles().equals("CN") || user.getRoles().equals("BC")) {
            rs = reportDao.reportAllCrm(startTimestamp, endTimestamp);
            if (StringUtils.isNotEmpty(dept))
                rs = rs.stream().filter(Objects::nonNull).filter(e -> e.getDeptCode().equals(dept)).collect(Collectors.toList());
            else if (user.getRoles().equals("CN"))
                rs = rs.stream().filter(Objects::nonNull).filter(e -> e.getDeptCode().equals(user.getDeptCode())).collect(Collectors.toList());
        }
        ByteArrayInputStream bais = exportService.exportBuuCuc(rs);
        String fileName = "BaoCaoTXBuuCuc" + ".xlsx";
        File targetFile = new File("data/" + fileName);
        FileUtils.copyInputStreamToFile(bais, targetFile);
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

    protected Users getCurrentUser() throws Exception {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        Users users = userRepo.findByUid(basicLogin.getUserUid());
        if (users == null) {
            throw new ApplicationException("Users is null");
        }
        return users;
    }
}
