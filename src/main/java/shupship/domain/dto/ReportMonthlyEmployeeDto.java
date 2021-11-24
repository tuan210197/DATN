package shupship.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ReportMonthlyEmployeeDto {

    private BigInteger empSystemId;

    private String empCode;

    private String fullName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postCode;

    private BigInteger totalAssigns; // tong so luong tiep xuc (duoc giao + tu nhap)

    private BigInteger successes;

    private BigInteger fails;

    private BigInteger contacting;

    private BigInteger assigned;

    private BigDecimal expectedRevenue;

}
