package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResultStatus {
    SENT_PRICE(1),

    SUCCESS(3),

    FAIL(5);

    private static final Map<Long, ResultStatus> MY_MAP = new HashMap<>();

    static {
        for (ResultStatus myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private long type;

    ResultStatus(long type) {
        this.type = type;
    }

    public long getType() {
        return this.type;
    }

    public static ResultStatus getByValue(long value) {
        return MY_MAP.get(value);
    }
}
