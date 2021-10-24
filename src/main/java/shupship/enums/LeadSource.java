package shupship.enums;

import java.util.HashMap;
import java.util.Map;

public enum LeadSource {
    PRIVATE("Cá nhân"),
    ENTERPRISE("Doanh nghiệp");

    private final String value;

    LeadSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
