package shupship.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.dao.ReportDao;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.domain.model.BasicLogin;
import shupship.domain.model.Schedule;
import shupship.domain.model.Users;
import shupship.dto.ReportMonthlyDeptDto;
import shupship.dto.ReportMonthlyPostOfficeDto;
import shupship.dto.SchedulesMonthlyDto;
import shupship.dto.SchedulesOfEmployeeMonthlyDto;
import shupship.helper.PagingRs;
import shupship.repo.BasicLoginRepo;
import shupship.repo.IScheduleRepository;
import shupship.repo.UserRepo;
import shupship.response.ReportEmployeeOnApp;
import shupship.service.IReportService;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class ReportService implements IReportService {

    @Autowired
    IScheduleRepository scheduleRepository;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BasicLoginRepo basicLoginRepo;


    @Autowired
    ReportDao reportDao;

    @Override
    public PagingRs reportAllPageable(Pageable pageRequest, Timestamp startTimestamp, Timestamp endTimestamp, String deptCode) throws Exception {
        Page<ReportMonthlyDeptDto> reportPage;

        List<ReportMonthlyDeptDto> rs = reportDao.reportAllCrm(startTimestamp, endTimestamp);

        Users user = getCurrentUser();

        if (StringUtils.isNotEmpty(deptCode))
            rs = rs.stream().filter(Objects::nonNull).filter(e -> e.getDeptCode().equals(deptCode)).collect(Collectors.toList());
        else if (user.getRoles().equals("CN"))
            rs = rs.stream().filter(Objects::nonNull).filter(e -> e.getDeptCode().equals(user.getDeptCode())).collect(Collectors.toList());

        reportPage = new PageImpl<>(rs, pageRequest, rs.size());

        Long countDept = 0L;
        Long countPost = 0L;
        Long totalEmployees = 0L;
        Long totalAssigns = 0L;
        Long successes = 0L;
        Long contacting = 0L;
        Long fails = 0L;
        Long employeeNotAssigned = 0L;
        Long assigned = 0L;
        for (ReportMonthlyDeptDto data : rs) {
            Long countPost1 = 0L;
            Long totalEmployees1 = 0L;
            Long totalAssigns1 = 0L;
            Long successes1 = 0L;
            Long contacting1 = 0L;
            Long fails1 = 0L;
            Long employeeNotAssigned1 = 0L;
            Long assigned1 = 0L;
            countDept += 1;
            List<ReportMonthlyPostOfficeDto> post = data.getReportMonthlyPostOfficeDtos();
            for (ReportMonthlyPostOfficeDto postOffice : post) {
                countPost += 1;
                countPost1 += 1;
                totalEmployees += postOffice.getTotalEmployees();
                totalAssigns += postOffice.getTotalAssigns();
                successes += postOffice.getSuccesses();
                contacting += postOffice.getContacting();
                fails += postOffice.getFails();
                employeeNotAssigned += postOffice.getEmployeeNotAssigned();
                assigned += postOffice.getAssigned();

                totalEmployees1 += postOffice.getTotalEmployees();
                totalAssigns1 += postOffice.getTotalAssigns();
                successes1 += postOffice.getSuccesses();
                contacting1 += postOffice.getContacting();
                fails1 += postOffice.getFails();
                employeeNotAssigned1 += postOffice.getEmployeeNotAssigned();
                assigned1 += postOffice.getAssigned();
            }
            data.setTotalPost(countPost1);
            data.setTotalCountEmp(totalEmployees1);
            data.setTotalAssigns(totalAssigns1);
            data.setTotalSuccesses(successes1);
            data.setTotalContacting(contacting1);
            data.setTotalFails(fails1);
            data.setTotalEmployeeNotAssigned(employeeNotAssigned1);
            if (totalAssigns1 != 0){
                data.setTyHT((fails1 + successes1) * 100 / totalAssigns1 );
                data.setTyTX((contacting1) * 100 / totalAssigns1);
            } else {
                data.setTyTX(0L);
                data.setTyHT(0L);
            }
            data.setTotalLeadNotTX(totalAssigns1 - assigned1);
            data.setTotalAssigned(assigned1);
        }

        PagingRs pagingRs = new PagingRs();
        pagingRs.setData(reportPage.getContent());
        pagingRs.setTotalCount(reportPage.getTotalElements());
        pagingRs.setTotalAssigned(assigned);
        pagingRs.setTotalDept(countDept);
        pagingRs.setTotalPost(countPost);
        pagingRs.setTotalCountEmp(totalEmployees);
        pagingRs.setTotalAssigns(totalAssigns);
        pagingRs.setTotalSuccesses(successes);
        pagingRs.setTotalContacting(contacting);
        pagingRs.setTotalFails(fails);
        pagingRs.setTotalEmployeeNotAssigned(employeeNotAssigned);
        if (totalAssigns != 0){
            pagingRs.setTyLeHT((fails + successes) * 100 / totalAssigns );
            pagingRs.setTyLeTX((contacting) * 100 / totalAssigns);
        } else {
            pagingRs.setTyLeTX(0L);
            pagingRs.setTyLeTX(0L);
        }
        return pagingRs;
    }

    @Override
    public PagingRs reportOnEVTPByPostCode(Pageable pageRequest, Timestamp startDate, Timestamp endDate, String post) throws Exception {
        Users user = getCurrentUser();
        if (user.getRoles().equals("TCT") || user.getRoles().equals("CN") || user.getRoles().equals("BC")) {

            String postCode = "";
            if (user.getRoles().equals("BC"))
                postCode = user.getPostCode();
            else postCode = post;

            Page<ReportMonthlyEmployeeDto> reportPage;
            List<ReportMonthlyEmployeeDto> rs = reportDao.reportByPost(startDate, endDate, postCode);

            reportPage = new PageImpl<>(rs, pageRequest, rs.size());

            Long countDept = 1L;
            Long countPost = 1L;
            Long totalEmployees = 0L;
            Long totalAssigns = 0L;
            Long successes = 0L;
            Long contacting = 0L;
            Long fails = 0L;
            Long employeeNotAssigned = 0L;
            Long assigned = 0L;
            for (ReportMonthlyEmployeeDto model : rs) {
                totalAssigns += model.getTotalAssigns().longValue();
                successes += model.getSuccesses().longValue();
                contacting += model.getContacting().longValue();
                fails += model.getFails().longValue();
                employeeNotAssigned += model.getTotalAssigns().longValue() - model.getAssigned().longValue();
                assigned += model.getAssigned().longValue();
            }

            PagingRs pagingRs = new PagingRs();
            pagingRs.setData(reportPage.getContent());
            pagingRs.setTotalCount(reportPage.getTotalElements());
            pagingRs.setTotalAssigned(assigned);
            pagingRs.setTotalDept(countDept);
            pagingRs.setTotalPost(countPost);
            pagingRs.setTotalCountEmp(reportPage.getTotalElements());
            pagingRs.setTotalAssigns(totalAssigns);
            pagingRs.setTotalSuccesses(successes);
            pagingRs.setTotalContacting(contacting);
            pagingRs.setTotalFails(fails);
            pagingRs.setTotalEmployeeNotAssigned(employeeNotAssigned);
            return pagingRs;
        } else throw new HieuDzException("Không có quyền xem!");
    }

    @Override
    public PagingRs reportAllEmpsInDept(Pageable pageRequest, Timestamp startTimestamp, Timestamp endTimestamp, String deptCode) throws Exception {
        Page<ReportMonthlyEmployeeDto> reportPage;
        List<ReportMonthlyEmployeeDto> rs = new ArrayList<>();

        Users user = getCurrentUser();

        if (user.getRoles().equals("TCT") || user.getRoles().equals("CN"))
            if (StringUtils.isNotEmpty(deptCode))
                rs = reportDao.reportAllEmpsInDept(startTimestamp, endTimestamp, deptCode);
            else
                rs = reportDao.reportAllEmpsInDept(startTimestamp, endTimestamp, user.getDeptCode());
        else throw new HieuDzException("Không có quyền xem!");

        reportPage = new PageImpl<>(rs, pageRequest, rs.size());

        Long countDept = 1L;
        Long countPost = 0L;
        Long totalEmployees = 0L;
        Long totalAssigns = 0L;
        Long successes = 0L;
        Long contacting = 0L;
        Long fails = 0L;
        Long employeeNotAssigned = 0L;
        Long assigned = 0L;
        HashSet<String> listPost = new HashSet();

        for (ReportMonthlyEmployeeDto model : rs) {
            countPost += 1;
            totalAssigns += model.getTotalAssigns().longValue();
            successes += model.getSuccesses().longValue();
            contacting += model.getContacting().longValue();
            fails += model.getFails().longValue();
            employeeNotAssigned += model.getTotalAssigns().longValue() - model.getAssigned().longValue();
            assigned += model.getAssigned().longValue();
        }

        PagingRs pagingRs = new PagingRs();
        pagingRs.setData(reportPage.getContent());
        pagingRs.setTotalCount(reportPage.getTotalElements());
        pagingRs.setTotalAssigned(assigned);
        pagingRs.setTotalDept(countDept);
        pagingRs.setTotalPost(countPost);
        pagingRs.setTotalCountEmp(reportPage.getTotalElements());
        pagingRs.setTotalAssigns(totalAssigns);
        pagingRs.setTotalSuccesses(successes);
        pagingRs.setTotalContacting(contacting);
        pagingRs.setTotalFails(fails);
        pagingRs.setTotalEmployeeNotAssigned(employeeNotAssigned);
        return pagingRs;
    }

    @Override
    public ReportEmployeeOnApp reportEmployeeOnApp(Timestamp from, Timestamp to, Long id) {
        List<ReportEmployeeOnApp> reportEmployeeOnAppList = reportDao.reportOfEmployee(from, to, id);
        ReportEmployeeOnApp model = reportEmployeeOnAppList.get(0);
        model.setTyLeHT((model.getSuccesses().doubleValue() + model.getFails().doubleValue()) * 100 / model.getTotalAssigns().doubleValue());
        model.setTyLeTX(model.getContacting().doubleValue() * 100 / model.getTotalAssigns().doubleValue());
        return model;
    }

    @Override
    public SchedulesOfEmployeeMonthlyDto reportByEmployeeOnEVTP(Pageable pageRequest, LocalDateTime startDate, LocalDateTime endDate, Long id) throws Exception {
        Users user = getCurrentUser();
        if (user.getRoles().contains("TCT") || user.getRoles().equals("CN") || user.getRoles().equals("BC")) {
            SchedulesOfEmployeeMonthlyDto response = new SchedulesOfEmployeeMonthlyDto();

            List<Schedule> schedules = scheduleRepository.findAllByUserId(startDate, endDate, id);
            List<SchedulesMonthlyDto> schedulesMonthlyDtos = new ArrayList<>();
            for (Schedule s : schedules) {
                SchedulesMonthlyDto res = SchedulesMonthlyDto.scheduleModelToReportDto(s);
                schedulesMonthlyDtos.add(res);
            }
            Users emp = userRepo.getUserById(id);
            if (emp != null) {
                response.setEmpSystemId(emp.getEmpSystemId());
                response.setEmpCode(emp.getEmployeeCode());
                response.setFullName(emp.getFullName());
                response.setSchedules(schedulesMonthlyDtos.stream().sorted(Comparator.comparing(SchedulesMonthlyDto::getFromDate).reversed()).distinct().collect(Collectors.toList()));
                return response;
            } else throw new HieuDzException("Nhân viên không tồn tại!");

        } else throw new HieuDzException("Không có quyền xem!");
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
