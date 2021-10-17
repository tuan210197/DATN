package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeadType {
    DUOC_GIAO(1),

    TU_NHAP(2);

    private static final Map<Long, LeadType> MY_MAP = new HashMap<>();

    static {
        for (LeadType myEnum : values()) {
            MY_MAP.put(myEnum.getType(), myEnum);
        }
    }

    private long type;

    LeadType(long type) {
        this.type = type;
    }

    public long getType() {
        return this.type;
    }

    public static LeadType getByValue(long value) {
        return MY_MAP.get(value);
    }
}
