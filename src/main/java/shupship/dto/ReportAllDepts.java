package shupship.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ReportAllDepts {

    private String deptCode;

    private String postCode;

    private BigInteger employees;

    private BigInteger totalAssigns;

    private BigInteger leads;

    private BigInteger successes;

    private BigInteger contacting;

    private BigInteger fails;

    private BigInteger employeeNotAssigned; // SL cá nhân chưa có tiếp xúc

    private BigInteger assigned;
}
