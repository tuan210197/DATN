package shupship.enums;

public enum LeadIndustry {
    LETTER("Thư, Hóa đơn, chứng từ"),
    BOOK("Sách, văn phòng phẩm"),
    ELECTRONIC_DEVICE("Thiết bị điện tử"),
    FASHION("Thời trang");

    private final String value;

    LeadIndustry(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
