package shupship.enums;

public enum LeadSource {
    PRIVATE("Cá nhân"),
    ENTERPRISE("Doanh nghiệp"),
    SYSTEM("Hệ thống");

    private final String value;

    LeadSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
