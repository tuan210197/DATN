package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeadSource {
    PRIVATE("Cá nhân"),
    ENTERPRISE("Doanh nghiệp");



    private static final Map<String, LeadSource> MY_MAP = new HashMap<>();

    static {
        for (LeadSource myEnum : values()) {
            MY_MAP.put(myEnum.getValue(), myEnum);
        }
    }
    private final String value;

    LeadSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static LeadSource getByValue(String value) {
        return MY_MAP.get(value);
    }
}
