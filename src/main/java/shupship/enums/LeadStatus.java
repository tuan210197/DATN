package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeadStatus {
    NEW(1),
    CONTACTING(2),
    SUCCESS(3),
    FAILED(4),
    NOT_CONTACTED(5);

    private static final Map<Long, LeadStatus> MY_MAP = new HashMap<>();

    static {
        for (LeadStatus myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private long type;

    LeadStatus(long type) {
        this.type = type;
    }

    public long getType() {
        return this.type;
    }

    public static LeadStatus getByValue(long value) {
        return MY_MAP.get(value);
    }
}
