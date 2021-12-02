package shupship.controller;


import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import shupship.auth.JwtTokenUtil;
import shupship.common.Const;
import shupship.domain.dto.JwtRequest;
import shupship.domain.dto.JwtResponse;
import shupship.domain.dto.UserInfoDTO;
import shupship.domain.dto.UserLoginDTO;
import shupship.service.JwtUserDetailsService;
import shupship.util.exception.HieuDzException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author tuandv
 */
@Scope("session")
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
                return toExceptionResult("USER CHƯA XÁC THỰC, GỬI LẠI OTP", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException("USER CHƯA XÁC THỰC, GỬI LẠI OTP");
            }
            String uid = userLoginDTO.getUserUid();
            UserInfoDTO userInfoDTO = userDetailsService.getUserInfo(uid);
            String token = jwtTokenUtil.generateToken(userInfoDTO);
            userDetailsService.loginFailRetryCount(authenticationRequest.getEmail(), false);
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(new JwtResponse(token, userLoginDTO.getUserUid()), "");
        } catch (IllegalArgumentException | InternalAuthenticationServiceException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (DisabledException e) {
            e.printStackTrace();
            return toExceptionResult("USER BỊ KHÓA HÃY LIÊN HỆ VỚI QUẢN TRỊ VIÊN", Const.API_RESPONSE.RETURN_CODE_ERROR);

        } catch (BadCredentialsException e) {
            e.printStackTrace();
            userDetailsService.loginFailRetryCount(authenticationRequest.getEmail(), true);
            return toExceptionResult("MẬT KHẨU KHÔNG ĐÚNG", Const.API_RESPONSE.RETURN_CODE_ERROR);


        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }


    @PostMapping(value = "/check-email")
    public ResponseEntity<?> checkEmailVerify(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.checkEmail(userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "EMAIL ĐÃ XÁC NHẬN");
            } else {
                return toExceptionResult("EMAIL KHÔNG TỒN TẠI HOẶC CHƯA XÁC THỰC", Const.API_RESPONSE.RETURN_CODE_SUCCESS);
//                throw new HieuDzException("EMAIL KHÔNG TỒN TẠI HOẶC CHƯA XÁC THỰC");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        }

    }

    @PostMapping(value = "/check-update")
    public ResponseEntity<?> checkUpdate(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.checkUpdate(userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "EMAIL ĐÃ ĐƯỢC CẬP NHẬT");
            } else {
                return toExceptionResult("EMAIL CHƯA ĐƯỢC CẬP NHẬT", Const.API_RESPONSE.RETURN_CODE_SUCCESS);
//                throw new HieuDzException("EMAIL CHƯA ĐƯỢC CẬP NHẬT");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        }
    }

    /**
     * Register an user
     *
     * @param userLoginDTO
     * @return Success or fail
     */

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO, Authentication authentication) throws Exception {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TCT"))) {
            try {
                String requestId = request.getHeader("request-id");
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #START " + requestId);
                if (userDetailsService.registerUser(userLoginDTO)) {
                    log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + " [" + request.getRequestURI() + "] #END " + requestId);
                    return toSuccessResult(null, "ĐĂNG KÝ THÀNH CÔNG");
                } else {
                    return toExceptionResult("ĐĂNG KÝ LỖI", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                    throw new HieuDzException("ĐĂNG KÝ LỖI");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//                throw new HieuDzException(e.getMessage());
            }
        } else {
            return toExceptionResult("KHÔNG CÓ QUYỀN TRUY CẬP", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
//            throw new HieuDzException("KHÔNG CÓ QUYỀN TRUY CẬP");
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
            return toSuccessResult(null, "GỬI OTP THÀNH CÔNG");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
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
                return toSuccessResult(null, "THAY ĐỔI MẬT KHẨU THÀNH CÔNG");
            } else {
                return toExceptionResult("TOKEN_INVALID", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException("TOKEN_INVALID");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        }
    }

    @PostMapping("/ban-user")
    public ResponseEntity<?> banUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TCT"))) {
            try {
                String requestId = request.getHeader("request-id");
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #START " + requestId);
                if (userDetailsService.banUser(userLoginDTO)) {
                    log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + " [" + request.getRequestURI() + "] #END " + requestId);
                    return toSuccessResult(null, "BAN_SUCCESS");
                } else {
                    return toExceptionResult("BAN_FAILED", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                    throw new HieuDzException("BAN_FAILED");
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
//                throw new HieuDzException(e.getMessage());
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
            }

        } else {
            return toExceptionResult("KHÔNG PHẬN SỰ MIỄN VÀO", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
//            throw new HieuDzException("KHÔNG PHẬN SỰ MIỄN VÀO");
        }
    }

    @PostMapping("/unlock-user")
    public ResponseEntity<?> unlockUser(HttpServletRequest request, @RequestBody UserLoginDTO userLoginDTO) {
        UserDetails auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TCT"))) {
            try {
                String requestId = request.getHeader("request-id");
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #START " + requestId);
                if (userDetailsService.unlockUser(userLoginDTO)) {
                    log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + " [" + request.getRequestURI() + "] #END " + requestId);
                    return toSuccessResult(null, "UNLOCK_SUCCESS");
                } else {
                    return toExceptionResult("UNLOCK_FAILED", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                    throw new HieuDzException("UNLOCK_FAILED");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//                throw new HieuDzException(e.getMessage());
            }

        } else {
            return toExceptionResult("KHÔNG PHẬN SỰ MIỄN VÀO", Const.API_RESPONSE.RETURN_CODE_ERROR_NOTFOUND);
//            throw new HieuDzException("KHÔNG PHẬN SỰ MIỄN VÀO");
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
    public ResponseEntity<?> updateUser(HttpServletRequest request, @PathVariable("uid") String
            userId, @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String requestId = request.getHeader("request-id");
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #START " + requestId);
            if (userDetailsService.updateUser(userId, userLoginDTO)) {
                log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " [" + request.getRequestURI() + "] #END " + requestId);
                return toSuccessResult(null, "CẬP NHẬT THÔNG TIN THÀNH CÔNG");
            } else {
                return toExceptionResult("CẬP NHẬT THÔNG TIN THẤT BẠI", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException("CẬP NHẬT THÔNG TIN THẤT BẠI");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());

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
            DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
            Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
            String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
            log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + " [" + request.getRequestURI() + "] #END " + requestId);
            return toSuccessResult(new JwtResponse(token, null), "Successfully");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
//            throw new HieuDzException("SYSTEM_ERROR");

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
                return toSuccessResult(null, "XÁC THỰC THÀNH CÔNG");
            } else {
                return toExceptionResult("TOKEN_INVALID", Const.API_RESPONSE.RETURN_CODE_ERROR);
//                throw new HieuDzException("TOKEN_INVALID");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
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
//            return toSuccessResult(null, "GỬI LẠI OTP THÀNH CÔNG");
            throw new HieuDzException("GỬI LẠI OTP THÀNH CÔNG");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
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
//            throw new HieuDzException("COMPLETED");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.RETURN_CODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new HieuDzException(e.getMessage());
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
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
//            throw new HieuDzException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toExceptionResult(e.getMessage(), Const.API_RESPONSE.SYSTEM_CODE_ERROR);
//            throw new HieuDzException(e.getMessage());
        }
    }
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
    }



