package shupship.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.domain.model.Users;
import shupship.dto.ReportMonthlyDeptDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public interface IExportService {

    ByteArrayInputStream exportBuuCuc(List<ReportMonthlyDeptDto> list) throws IOException, InvalidFormatException;

    ByteArrayInputStream exportChiNhanh(List<ReportMonthlyDeptDto> list) throws IOException, InvalidFormatException;

    ByteArrayInputStream exportNhanVien(List<ReportMonthlyEmployeeDto> list) throws IOException, InvalidFormatException;

    ByteArrayInputStream exportDataToExcel(Timestamp startTimestamp, Timestamp endTimestamp, Users user) throws IOException, InvalidFormatException;
}
