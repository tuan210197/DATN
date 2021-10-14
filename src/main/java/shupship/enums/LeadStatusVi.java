package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeadStatusVi {
    NEW("Mới"),
    CONTACTING("Đang tiếp xúc"),
    SUCCESS("Thành công"),
    FAILED("Thất bại"),
    NOT_CONTACTED("Chưa tiếp xúc");

    private static final Map<String, LeadStatusVi> MY_MAP = new HashMap<>();

    static {
        for (LeadStatusVi myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private String type;

    LeadStatusVi(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static LeadStatusVi getByValue(String value) {
        return MY_MAP.get(value);
    }
}
