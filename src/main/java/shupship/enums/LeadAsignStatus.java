package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeadAsignStatus {
    NEW(1),
    CONTACTING(2),
    SUCCESS(3),
    FAILED(4);

    private static final Map<Integer, LeadAsignStatus> MY_MAP = new HashMap<>();

    static {
        for (LeadAsignStatus myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private int type;

    LeadAsignStatus(Integer type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static LeadAsignStatus getByValue(long value) {
        return MY_MAP.get(value);
    }
}
