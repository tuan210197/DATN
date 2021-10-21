package shupship.controller;


import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import shupship.auth.JwtTokenUtil;
import shupship.common.Const;
import shupship.domain.dto.JwtRequest;
import shupship.domain.dto.JwtResponse;
import shupship.domain.dto.UserInfoDTO;
import shupship.domain.dto.UserLoginDTO;
import shupship.domain.model.BasicLogin;
import shupship.service.JwtUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author tuandv
 */

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class AuthenController extends BaseController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * Authenticate for User to login to system
     *
     * @param authenticationRequest
     * @return Token and UserId
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request, @RequestBody JwtRequest authenticationRequest) {

        try {

            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
            UserLoginDTO userLoginDTO = userDetailsService.getBasicAuthByEmail(authenticationRequest.getEmail(), true);
            if (Objects.isNull(userLoginDTO)) {
                return toExceptionResult("USER_NOT_VERIFY_RESEND_OTP", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
            String token = jwtTokenUtil.generateToken(new User(userLoginDTO.getEmail(), userLoginDTO.getPassword(),
                    new ArrayList<>()));
            userDetailsService.loginFailRetryCount(authenticationRequest.getEmail(), false);
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(new JwtResponse(token, userLoginDTO.getUserUid()), "");
        } catch (IllegalArgumentException | InternalAuthenticationServiceException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (DisabledException e) {
            e.printStackTrace();
            return toExceptionResult("USER_DISABLED", Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            userDetailsService.loginFailRetryCount(authenticationRequest.getEmail(), true);
            return toExceptionResult("INVALID_CREDENTIALS", Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }


@PostMapping(value = "/check-email")
public ResponseEntity<?> checkEmailVerify(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO){
    try {
        String requestId = request.getHeader("request-id");
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " [" + request.getRequestURI() + "] #START " + requestId);
        if (userDetailsService.checkEmail(userLoginDTO)) {
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(null, "EMAIL_VERIFY");
        } else {
            return toExceptionResult("EMAIL_NOT_VERIFY", Const.API_RESPONSE.RETURN_CODE_ERROR);
        }
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
        return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
    } catch (Exception e) {
        e.printStackTrace();
        return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
    }
}
    @PostMapping(value = "/check-update")
    public ResponseEntity<?> checkUpdate(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO){
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.checkUpdate(userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "EMAIL_IS_UPDATED");
            } else {
                return toExceptionResult("EMAIL_IS_NOT_UPDATED", Const.API_RESPONSE.RETURN_CODE_SUCCESS);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }
    /**
     * Register an user
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.registerUser(userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "REGISTER_COMPLETED");
            } else {
                return toExceptionResult("REGISTER_FAILED", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }


    /**
     * User has forgotten their password. Request for changing password with email
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/password/forgot", method = RequestMethod.POST)
    public ResponseEntity<?> forgetPassword(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            userDetailsService.forgetPassword(userLoginDTO);
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(null, "SEND_OTP_SUCCESS");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * Change password
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/password/change", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.updatePassword(userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "PASSWORD_CHANGE_SUCCESS");
            } else {
                return toExceptionResult("TOKEN_INVALID", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * Update profile in app_user table
     *
     * @param userId
     * @param userLoginDTO
     * @return Success or fail
     */
    @RequestMapping(value = "/{uid}/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(HttpServletRequest request, @PathVariable("uid") String userId, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.updateUser(userId, userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "UPDATE_PROFILE_SUCCESS");
            } else {
                return toExceptionResult("UPDATE_PROFILE_FAIL", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }
    @PostMapping(value = "/delete/{uid}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request,@PathVariable("uid") String uid){
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.deleteUser(uid)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "DELETE_USER_SUCCESS");
            } else {
                return toExceptionResult("DELETE_USER_FAIL", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }
    /**
     * Get refresh token after token has expired
     *
     * @param request
     * @return new token
     */
    @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            // From the HttpRequest get the claims
            DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
            Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
            String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(new JwtResponse(token, null), "Successfully");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult("SYSTEM_ERROR", Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    /**
     * Verify user with token from email
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @PostMapping("/verify")
    public ResponseEntity<?> checkOtpToken(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.checkTokenForUser(userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "VERIFY_COMPLETED");
            } else {
                return toExceptionResult("TOKEN_INVALID", Const.API_RESPONSE.RETURN_CODE_ERROR);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * Resend token to User's email
     *
     * @param userLoginDTO
     * @return Success or fail
     */
    @PostMapping("/verify-code/resend")
    public ResponseEntity<?> resendOTP(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            userDetailsService.resendOTP(userLoginDTO);
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(null, "RESEND_OTP_COMPLETED");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }

    /**
     * get user information
     *
     * @param request
     * @param userUid
     * @return information of user
     */
    @GetMapping("/{userUid}")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request, @PathVariable("userUid") String userUid) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            UserInfoDTO userInfoDTO = userDetailsService.getUserInfo(userUid);
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(userInfoDTO, "COMPLETED");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            String tokenCode = jwtTokenUtil.extractJwtFromRequest(request);
            boolean result = userDetailsService.logout(tokenCode);
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(result, "");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }
}
