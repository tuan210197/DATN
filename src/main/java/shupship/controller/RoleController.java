package shupship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shupship.common.Const;
import shupship.domain.dto.UserInfoDTO;
import shupship.domain.model.Role;
import shupship.repo.RoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/role")
public class RoleController extends  BaseController {
    @Autowired
    private RoleRepository repository;

    @GetMapping("/")
    public ResponseEntity<?> getRole(HttpServletRequest request){
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            List<Role> role = repository.findAll();
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(role, "SUCCESS");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

}
