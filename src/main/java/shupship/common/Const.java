package shupship.common;


import java.util.List;

public class Const {

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


}
