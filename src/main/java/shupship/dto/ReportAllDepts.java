package shupship.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ReportAllDepts {

    private String deptCode;

    private String postCode;

    private Long employees;

    private Long totalAssigns;

    private Long leads;

    private Long successes;

    private Long contacting;

    private Long fails;

    private Long employeeNotAssigned; // SL cá nhân chưa có tiếp xúc

    private Long assigned;
}
