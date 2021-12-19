package shupship.response;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class ReportResponse {
    // ngay khach hang duoc tiep xuc
    private Timestamp leadAssignTime;

    // thong tin nhan vien
    private String employeeCode;
    private String postCode;
    private String deptCode;

    private String fullName;
    private String representation;

    // dia chi khach hang
    private String homeNo;
    private String fomataddress;
    private Timestamp schedulerDate;
    private Timestamp resultDate;

    // thong tin chung
    private String title;
    private String phone;
    private String leadSource;

    private Double inProvincePrice;
    private Double outProvincePrice;

    // thoi gian dat lich
    private String proposal;
    private Double discout;
    private Long status;
    private String customorCode;

    public static ReportResponse leadModelToDto(Object[] model) {

        ReportResponse data = new ReportResponse();
        data.setLeadAssignTime(model[18] == null ? null : (Timestamp) model[18]);
        data.setSchedulerDate(model[11] == null ? null : (Timestamp) model[11]);
        data.setResultDate(model[12] == null ? null : (Timestamp) model[12]);
        data.setLeadAssignTime(model[18] == null ? null : (Timestamp) model[18]);
        data.setEmployeeCode(model[0] == null ? null : String.valueOf(model[0]));
        data.setPostCode(model[1] == null ? null : String.valueOf(model[1]));
        data.setDeptCode(model[2] == null ? null : String.valueOf(model[2]));
        data.setFullName(model[4] == null ? null : String.valueOf(model[4]));
        data.setRepresentation(model[5] == null ? null : String.valueOf(model[5]));
        data.setCustomorCode(model[3] == null ? null : String.valueOf(model[3]));
        // dia chi khach hang
        data.setHomeNo(model[6] == null ? null : String.valueOf(model[6]));
        // thong tin chung
        data.setTitle(model[8] == null ? null : String.valueOf(model[8]));
        data.setPhone(model[9] == null ? null : String.valueOf(model[9]));
        data.setLeadSource(model[10] == null ? null : String.valueOf(model[10]));
        data.setInProvincePrice(model[15] == null ? null : Double.valueOf(String.valueOf(model[15])));
        data.setOutProvincePrice(model[16] == null ? null : Double.valueOf(String.valueOf(model[16])));
        data.setDiscout(model[14] == null ? null : Double.valueOf(String.valueOf(model[14])));
        data.setStatus(model[13] == null ? null : Long.valueOf(String.valueOf(model[13])));
        data.setProposal(model[17] == null ? null : String.valueOf(model[17]));
        // thoi gian dat lich

        return data;
    }

}
