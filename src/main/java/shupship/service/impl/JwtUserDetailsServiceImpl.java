package shupship.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.auth.JwtTokenUtil;
import shupship.common.Const;
import shupship.domain.dto.UserInfoDTO;
import shupship.domain.dto.UserLoginDTO;

import shupship.domain.model.BasicLogin;
import shupship.domain.model.Users;
import shupship.repo.UserRepo;
import shupship.repo.BasicLoginRepo;
import shupship.service.JwtUserDetailsService;
import shupship.service.MailSenderService;
import shupship.util.ValidateUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@Transactional
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {


    @Autowired
    private BasicLoginRepo basicLoginRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BasicLogin basicLogin = validateUserAuthen(email);
        return new User(basicLogin.getEmail(), basicLogin.getPassword(),
                new ArrayList<>());
    }

    @Override
    public UserLoginDTO getBasicAuthByEmail(String email, boolean forceClear) {
        BasicLogin basicLogin = validateUserAuthen(email);
        if (basicLogin.getIsVerified().equals(Const.COMMON_CONST_VALUE.NOT_VERIFIED)) {
            log.info("User not verify. Resend OTP for verifying");
            UserLoginDTO userLoginDTO = UserLoginDTO.builder().email(email).build();
            resendOTP(userLoginDTO);
            return null;
        }
        log.info("Start query Table user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Users user = userRepo.findByUid(basicLogin.getUserUid());
        log.info("End query Table user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.isTrue(user.getIsActive().equals(Const.COMMON_CONST_VALUE.ACTIVE), "USER_NOT_ACTIVE");
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        BeanUtils.copyProperties(basicLogin, userLoginDTO);
        return userLoginDTO;
    }


    private BasicLogin validateUserAuthen(String email) {
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
//        if (basicLogin == null) {
//            throw new UsernameNotFoundException("User not found with Email: " + email);
//        }
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        Assert.isTrue(basicLogin.getRetryCount() < Const.RETRY_TIMES, "USER_LOCKED");
        return basicLogin;
    }

    @Override
    public boolean checkEmail(UserLoginDTO user) {
        Assert.hasText(user.getEmail(), "EMAIL_EMPTY");
        Assert.isTrue(ValidateUtil.regexValidation(user.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin checkBasicLogin = basicLoginRepo.findByEmail(user.getEmail());

        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        int check = checkBasicLogin.getIsVerified();
        if (check == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUpdate(UserLoginDTO userLoginDTO) {
        Assert.hasText(userLoginDTO.getEmail(), "EMAIL_EMPTY");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin checkBasicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        Users user = userRepo.findByUid(checkBasicLogin.getUserUid());
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return user.getStatus_update();
    }

    @Override
    public boolean banUser(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String uid = userLoginDTO.getUserUid();

        if (uid != null) {
            Users users = userRepo.findByUid(uid);
            users.setIsActive(0);
            userRepo.save(users);
            return true;
        } else if (email != null) {
            BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
            basicLogin.setIsVerified(0);
            basicLoginRepo.save(basicLogin);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean registerUser(UserLoginDTO user) {
        Assert.hasText(user.getEmail(), "EMAIL_EMPTY");
//        Assert.hasText(user.getPassword(), "PASSWORD_EMPTY");
//        Assert.notNull(user.getBirthday(),"DATE_NOT_VALID");
        Assert.isTrue(ValidateUtil.regexValidation(user.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
//        Assert.isTrue(ValidateUtil.regexValidation(user.getPassword(), Const.VALIDATE_INPUT.regexPass), "PASS_WRONG_FORMAT");
//        Assert.isTrue(ValidateUtil.regexValidation(user.getMobile(), Const.VALIDATE_INPUT.regexPhone), "PHONE_WRONG_FORMAT");
//        Assert.isTrue(Const.VALIDATE_INPUT.phoneNum.contains(user.getMobile().substring(0, 3)),"PHONE_NOT_VALID");
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin checkBasicLogin = basicLoginRepo.findByEmail(user.getEmail());
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.isNull(checkBasicLogin, "EMAIL_REGISTERED");
        Users users = Users.builder()
                .avatar(user.getAvatar())
                .birthday(user.getBirthday())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .isActive(Const.COMMON_CONST_VALUE.ACTIVE)
                .isDeleted(Const.COMMON_CONST_VALUE.NOT_DELETED)
                .mobile(user.getMobile())
                .name(user.getName())
                .status_update(false)
                .roleName(user.getRoleName())
                .build();
        log.info("Start save Table user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        users = userRepo.save(users);
        log.info("End save Table user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = BasicLogin.builder()
                .email(user.getEmail())
//                .password(bcryptEncoder.encode(generatePassword(10)))
                .userUid(users.getUid())
                .build();
        sendOTP(basicLogin, "OTP Token for Register Account");
        sendPassword(basicLogin, "PASSWORD FOR YOUR EMAIL");
        return Objects.nonNull(users.getUid());
    }

    //send OTP to mail when change password or forgot password
    @Override
    public void forgetPassword(UserLoginDTO userLoginDTO) {
        Assert.hasText(userLoginDTO.getEmail(), "EMAIL_EMPTY");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getEmail(), Const.VALIDATE_INPUT.regexEmail), "EMAIL_WRONG_FORMAT");
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.notNull(basicLogin, "EMAIL_NOT_EXIST");
        sendOTP(basicLogin, "OTP Token to Forget Password");

    }

    public void sendOTP(BasicLogin basicLogin, String subject) {
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        String otpToken = generateOTPToken();
//        String password = generatePassword(10);
        basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.NOT_VERIFIED);
        basicLogin.setTokenCode(otpToken);
//        basicLogin.setPassword(bcryptEncoder.encode(password));
        basicLogin.setExpireDate(LocalDateTime.now().plusMinutes(15));
        basicLogin.setRetryCount(0);
        log.info("Start save Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        basicLoginRepo.save(basicLogin);
        log.info("End save Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        mailSenderService.sendSimpleMessage(basicLogin.getEmail(), subject, "Your OTP Token is: " + otpToken);
    }

    public void sendPassword(BasicLogin basicLogin, String subject) {
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
//        String otpToken= generateOTPToken();
        String password = generatePassword(10);
//        basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.NOT_VERIFIED);
//        basicLogin.setTokenCode(otpToken);
        basicLogin.setPassword(bcryptEncoder.encode(password));
//        basicLogin.setExpireDate(LocalDateTime.now().plusMinutes(15));
//        basicLogin.setRetryCount(0);
        log.info("Start save Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        basicLoginRepo.save(basicLogin);
        log.info("End save Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        mailSenderService.sendSimpleMessage(basicLogin.getEmail(), subject, "Your Password is: " + password);
    }

    // update password when change password or forgot password
    @Override
    public boolean updatePassword(UserLoginDTO userLoginDTO) {
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.notNull(basicLogin, "EMAIL_NOT_FOUND");
        Assert.hasText(userLoginDTO.getNewPassword(), "NEW_PASSWORD_NOT_FOUND");
        Assert.hasText(userLoginDTO.getReNewPassword(), "CONFIRM_PASSWORD_NOT_FOUND");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getNewPassword(), Const.VALIDATE_INPUT.regexPass), "NEW_PASS_WRONG_FORMAT");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getReNewPassword(), Const.VALIDATE_INPUT.regexPass), "CONFIRM_PASS_WRONG_FORMAT");

        if (userLoginDTO.isForGotPassword()) {
            Assert.hasText(userLoginDTO.getTokenCode(), "OTP_NOT_FOUND");
            Assert.isTrue(basicLogin.getExpireDate().isAfter(LocalDateTime.now()), "TOKEN_EXPIRED");
            Assert.isTrue(basicLogin.getRetryCount() < Const.RETRY_TIMES, "TRIED_TOO_MANY_TIMES");
            if (userLoginDTO.getTokenCode().equals(basicLogin.getTokenCode())) {
                basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.VERIFIED);
            } else {
                int retry = Objects.nonNull(basicLogin.getRetryCount()) ? basicLogin.getRetryCount() : 0;
                basicLogin.setRetryCount(++retry);
                log.info("Start save Table basic_login at time: "
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                basicLoginRepo.save(basicLogin);
                log.info("End save Table basic_login at time: "
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
                return false;
            }
        } else {
            Assert.hasText(userLoginDTO.getPassword(), "PASSWORD_NOT_FOUND");
            Assert.isTrue(bcryptEncoder.matches(userLoginDTO.getPassword(), basicLogin.getPassword()), "PASSWORD_NOT_MATCH");
            Assert.isTrue(userLoginDTO.getNewPassword().equals(userLoginDTO.getReNewPassword()), "NEW_RETYPE_PASSWORD_NOT_MATCH");
            basicLogin.setPassword(bcryptEncoder.encode(userLoginDTO.getNewPassword()));
            log.info("Start save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            basicLoginRepo.save(basicLogin);
            log.info("End save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            return true;
        }
        Assert.isTrue(userLoginDTO.getNewPassword().equals(userLoginDTO.getReNewPassword()), "NEW_RETYPE_PASSWORD_NOT_MATCH");
        basicLogin.setPassword(bcryptEncoder.encode(userLoginDTO.getNewPassword()));
        log.info("Start save Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        basicLoginRepo.save(basicLogin);
        log.info("End save Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return true;
    }

    //Update profile user
    @Override
    public boolean updateUser(String userId, UserLoginDTO userLoginDTO) {
        Users userCheck = userRepo.findByUid(userId);
        Assert.notNull(userCheck, "USER_NOT_FOUND");
        Assert.notNull(userLoginDTO.getBirthday(), "DATE_NOT_VALID");
        Assert.notNull(userLoginDTO.getName(), "NAME_NOT_VALID");
        Assert.notNull(userLoginDTO.getFullName(), "FULL_NAME_NOT_VALID");
        Assert.isTrue(ValidateUtil.regexValidation(userLoginDTO.getMobile(), Const.VALIDATE_INPUT.regexPhone), "PHONE_WRONG_FORMAT");
        Assert.isTrue(Const.VALIDATE_INPUT.phoneNum.contains(userLoginDTO.getMobile().substring(0, 3)), "PHONE_NOT_VALID");
        Assert.isTrue(userCheck.getIsActive().equals(1), "USER_NOT_ACTIVE");
        userCheck.setAvatar(userLoginDTO.getAvatar());
        userCheck.setBirthday(userLoginDTO.getBirthday());
        userCheck.setFullName(userLoginDTO.getFullName());
        userCheck.setGender(userLoginDTO.getGender());
        userCheck.setMobile(userLoginDTO.getMobile());
        userCheck.setName(userLoginDTO.getName());
        userCheck.setStatus_update(true);
        log.info("Start save Table user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        userRepo.save(userCheck);
        log.info("End save Table user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return true;
    }

    public String generateOTPToken() {
        String otpToken;
        int minNum = 100000;
        int maxNum = 999999;
        int randomNumber = (int) (Math.random() * (maxNum - minNum + 1) + minNum);
        otpToken = randomNumber + "";
        return otpToken;
    }

    public static String generatePassword(int len) {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        int len = 10;
        System.out.println(generatePassword(len));
    }

    @Override
    public boolean checkTokenForUser(UserLoginDTO userLoginDTO) {
        Assert.hasText(userLoginDTO.getEmail(), "EMAIL_EMPTY");
        Assert.hasText(userLoginDTO.getTokenCode(), "OTP_NOT_FOUND");
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        Assert.notNull(basicLogin, "USER_NOT_FOUND");
        Assert.isTrue(basicLogin.getIsVerified().equals(0), "USER_VERIFIED");
        Assert.isTrue(LocalDateTime.now().isBefore(basicLogin.getExpireDate()), "TOKEN_EXPIRED");
        Assert.isTrue(basicLogin.getRetryCount() < Const.RETRY_TIMES, "TRIED_TOO_MANY_TIMES");
        if (userLoginDTO.getTokenCode().equals(basicLogin.getTokenCode())) {
            basicLogin.setIsVerified(Const.COMMON_CONST_VALUE.VERIFIED);
            basicLogin.setRetryCount(0);
            log.info("Start save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            basicLoginRepo.save(basicLogin);
            log.info("End save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            return true;
        } else {
            int retry = Objects.nonNull(basicLogin.getRetryCount()) ? basicLogin.getRetryCount() : 0;
            basicLogin.setRetryCount(++retry);
            log.info("Start save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            basicLoginRepo.save(basicLogin);
            log.info("End save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            return false;
        }
    }

    @Override
    public boolean isLocked(String email) {
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return basicLogin.getRetryCount() < Const.RETRY_TIMES;
    }

    @Override
    public void resendOTP(UserLoginDTO userLoginDTO) {
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByEmail(userLoginDTO.getEmail());
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.isTrue(basicLogin.getIsVerified().equals(0), "USER_VERIFIED");
        sendOTP(basicLogin, "OTP Token for Register Account");
    }

    @Override
    public void loginFailRetryCount(String email, boolean loginFailed) {
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        if (Objects.nonNull(basicLogin)) {
            int retryCount = loginFailed ? basicLogin.getRetryCount() + 1 : 0;
            basicLogin.setRetryCount(retryCount);
            log.info("Start save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            basicLoginRepo.save(basicLogin);
            log.info("End save Table basic_login at time: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        }
    }

    @Override
    public UserInfoDTO getUserInfo(String userUid) {
        log.info("Start query Table app_user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Users user = userRepo.findByUid(userUid);
        log.info("End query Table app_user at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.notNull(user, "USER_NOT_FOUND");
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        log.info("Start query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        BasicLogin basicLogin = basicLoginRepo.findByUserUid(userUid);
        log.info("End query Table basic_login at time: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        userInfoDTO.setEmail(basicLogin.getEmail());
        return userInfoDTO;
    }


    @Override
    public boolean logout(String token) {
//        log.info("Start query Table basic_login at time: "
//                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
//        String email = jwtTokenUtil.getUsernameFromToken(token);
//        Assert.isTrue(StringUtils.isNotEmpty(email), "EMAIL_NOT_FOUND");
//        BasicLogin basicLogin = basicLoginRepo.findByEmail(email);
//        log.info("End query Table basic_login at time: "
//                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
//        Assert.notNull(basicLogin, "EMAIL_NOT_FOUND");
//        basicLogin.setDeviceUdid(null);
//        log.info("Start save Table basic_login at time: "
//                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
//        basicLoginRepo.save(basicLogin);
//        log.info("End save Table basic_login at time: "
//                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        return true;
    }


}
