package shupship.enums;

public enum LeadIndustry {
    LETTER("Thư, Hóa đơn, chứng từ"),
    BOOK("Sách, văn phòng phẩm"),
    ELECTRONIC_DEVICE("Thiết bị điện tử"),
    FASHION("Thời trang"),
    FASHION_ACCESSORIES("Phụ kiện thời trang"),
    COSMETICS("Mỹ phẩm"),
    HOUSEHOLD_APPLIANCES("Thiết bị gia dụng"),
    PLAYTHING("Đồ chơi"),
    AGRICULTURAL_PRODUCTS("Hàng nông sản"),
    INTERIOR_EQUIPMENT("Thiết bị nội thất"),
    MEDICINE("Dược phẩm"),
    VEHICLE("Ô tô, Xe máy,Phụ kiện"),
    HAND_LUGGAGE("Hành lý xách tay"),
    FURTHER("Khác");

    private final String value;

    LeadIndustry(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
