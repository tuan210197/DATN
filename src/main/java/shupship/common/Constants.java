package shupship.common;

public class Constants {
    // API Sync
    public final static String API_PROVINCE = "https://location.okd.viettelpost.vn/location/v1.0/place?type=PROVINCE&size=100&system=VTP";
    public final static String API_DISTRICT = "https://location.okd.viettelpost.vn/location/v1.0/place?type=DISTRICT&size=10000&system=VTP";
    public final static String API_WARD = "https://location.okd.viettelpost.vn/location/v1.0/place?type=WARD&size=100000&system=VTP";
    public final static String API_STREET = "https://location.okd.viettelpost.vn/location/v1.0/autocomplete?system=VTP&ctx=SUBWARD&ctx={wardId}";

    public final static String QUERY_POSTOFFICE = "SELECT BUUCUC, TINH, QUANHUYEN, PHUONGXA, NAME, LATITUDE, LONGITUDE, PHONE FROM VTP.DM_BUUCUCQUANLYVIEW";
}
