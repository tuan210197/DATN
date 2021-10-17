package shupship.common;


import com.sun.istack.NotNull;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Const {
    private static final String LANGUAGE_DEFAULT = "vn";
    private static final Const instance = new Const();
    private static final String UTF8 = "UTF-8";
    private static final Map<Integer, String> msg = new HashMap<>();


    public static final Integer RETRY_TIMES = 5;
    public static final String MOCK_TOKEN = "F27F4F2FA85F9C52A9F1ED5EF1EC8";


    public static class API_RESPONSE {
        public static final int RETURN_CODE_SUCCESS = 200;
        public static final int RETURN_CODE_ERROR = 400;
        public static final int SYSTEM_CODE_ERROR = 500;
        public static final int RETURN_CODE_ERROR_NOTFOUND = 404;
        public static final Boolean RESPONSE_STATUS_TRUE = true;
        public static final Boolean RESPONSE_STATUS_FALSE = false;
        public static final String RETURN_DES_FAILURE_NOTFOUND = "Not Found";
        public static final String DESCRIPTION_DEFAULT = "error";
        public static final String INTERNAL_SERVER_ERROR = "internal server error";

        private API_RESPONSE() {
            throw new IllegalStateException();
        }
    }

    public static class COMMON_CONST_VALUE {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
        public static final Integer DELETED = 1;
        public static final Integer NOT_DELETED = 0;
        public static final Integer VERIFIED = 1;
        public static final Integer NOT_VERIFIED = 0;
        public static final Integer ACCEPTED = 1;
        public static final Integer NOT_ACCEPTED = 0;

    }

    public static class ResultSetMapping {

        public static final String USER_INFO_DTO = "UserInfoDTO";

    }

    public static class VALIDATE_INPUT {
        public static final List<String> phoneNum = List.of("032", "036", "056", "076",
                "081", "085", "090", "094", "099", "052", "096", "091", "086", "082", "077", "058", "037", "033",
                "034", "035", "038", "039", "059", "070", "078", "079", "083", "084", "088", "089", "092", "093", "097", "098");
        public static final String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        public static final String regexPhone = "^[0-9]{10}$";
        public static final String regexPass = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,255}$";
    }



    public static Const getInstance() {
        return instance;
    }


    public static ResourceBundle getBundle(String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("language_" + language );
        return bundle;
    }

    public static String getProperty(int code, String language) {
        String text = msg.get(code);
        getBundle(language).keySet();
        return getBundle(language).getString(text);

    }

    public static String getMessage(int code) {
        return getMsg(code, LANGUAGE_DEFAULT);
    }

    public static String getMessage(int code, String language) {
        if (StringUtils.isEmpty(language)) {
            language = LANGUAGE_DEFAULT;
        }

        return getMsg(code, language);
    }

    @NotNull
    private static String getMsg(int code, String language) {
        if (msg.containsKey(code)) {
            String message = getProperty(code, language);
            try {
                String msg;
                if (code == 0) {
                    msg = new String(message.getBytes(UTF8), UTF8);
                } else {
                    msg = "[ERR_" + code + "] " + new String(message.getBytes(UTF8), UTF8);
                }
                return msg;
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }

        return "";
    }

    public static void main(String[] strs) {
        System.out.println("" + getMessage(0, "vn"));
    }
}
