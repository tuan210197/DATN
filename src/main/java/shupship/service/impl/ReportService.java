package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.BasicLogin;
import shupship.domain.model.Schedule;
import shupship.domain.model.Users;
import shupship.dto.SchedulesMonthlyDto;
import shupship.dto.SchedulesOfEmployeeMonthlyDto;
import shupship.repo.BasicLoginRepo;
import shupship.repo.IScheduleRepository;
import shupship.repo.UserRepo;
import shupship.service.IReportService;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;
import shupship.util.exception.HieuDzException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
            } else throw new HieuDzException( "Nhân viên không tồn tại!");

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
