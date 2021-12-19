package shupship.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.dto.ReportMonthlyDeptDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface IExportService {

    ByteArrayInputStream exportBuuCuc(List<ReportMonthlyDeptDto> list) throws IOException, InvalidFormatException;

    ByteArrayInputStream exportChiNhanh(List<ReportMonthlyDeptDto> list) throws IOException, InvalidFormatException;
}
