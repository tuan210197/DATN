package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum ScheduleStatus {
    NEW(1),
    CONTACTING(2),
    SUCCESS(3),
    FAILED(4);

    private static final Map<Long, ScheduleStatus> MY_MAP = new HashMap<>();

    static {
        for (ScheduleStatus myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private long type;

    ScheduleStatus(long type) {
        this.type = type;
    }

    public long getType() {
        return this.type;
    }

    public static ScheduleStatus getByValue(long value) {
        return MY_MAP.get(value);
    }
}
