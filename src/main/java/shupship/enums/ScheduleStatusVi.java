package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum ScheduleStatusVi {
    NEW("Mới"),
    CONTACTING("Đang tiếp xúc"),
    SUCCESS("Thành công"),
    FAILED("Thất bại");

    private static final Map<String, ScheduleStatusVi> MY_MAP = new HashMap<>();

    static {
        for (ScheduleStatusVi myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private String type;

    ScheduleStatusVi(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static ScheduleStatusVi getByValue(String value) {
        return MY_MAP.get(value);
    }
}
