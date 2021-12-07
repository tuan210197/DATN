package shupship.service.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.dto.ReportMonthlyDeptDto;
import shupship.dto.ReportMonthlyPostOfficeExportDto;
import shupship.service.IExportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExportService implements IExportService {
    @Override
    public ByteArrayInputStream exportBuuCuc(List<ReportMonthlyDeptDto> list) throws IOException, InvalidFormatException {
        InputStream resource = new ClassPathResource("bao-cao-tiep-xuc-buu-cuc.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(resource);

        Sheet sheet = workbook.getSheetAt(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<ReportMonthlyPostOfficeExportDto> ll = new ArrayList<>();
        for (ReportMonthlyDeptDto rp : list) {
            rp.getReportMonthlyPostOfficeDtos().forEach(x -> {
                ReportMonthlyPostOfficeExportDto export = new ReportMonthlyPostOfficeExportDto();
                export.fromReportMonthlyPostOfficeDto(x, rp.getDeptCode());
                export.setDeptCode(rp.getDeptCode());
                ll.add(export);
            });
        }

        int i = 1;
        int stt = 1;
        for (ReportMonthlyPostOfficeExportDto model : ll) {
            Row row = sheet.createRow(i);
            row.createCell((short) 0).setCellValue(String.valueOf(model.getDeptCode()));
            row.createCell((short) 1).setCellValue(String.valueOf(model.getPostCode()));
            row.createCell((short) 2).setCellValue(Optional.ofNullable(model.getTotalEmployees()).isPresent() ? String.valueOf(model.getTotalEmployees()) : "");
            row.createCell((short) 3).setCellValue(Optional.ofNullable(model.getTotalAssigns()).isPresent() ? String.valueOf(model.getTotalAssigns()) : "");
            row.createCell((short) 4).setCellValue(Optional.ofNullable(model.getSuccesses()).isPresent() ? String.valueOf(model.getSuccesses()) : "");
            row.createCell((short) 5).setCellValue(Optional.ofNullable(model.getContacting()).isPresent() ? String.valueOf(model.getContacting()) : "");
            row.createCell((short) 6).setCellValue(Optional.ofNullable(model.getFails()).isPresent() ? String.valueOf(model.getFails()) : "");
            row.createCell((short) 7).setCellValue(Optional.ofNullable(model.getAssigned()).isPresent() ? String.valueOf(model.getAssigned()) : "");
            row.createCell((short) 8).setCellValue(Optional.ofNullable(model.getEmployeeNotAssigned()).isPresent() ? String.valueOf(model.getEmployeeNotAssigned()) : "");
            i++;
            stt++;
        }
        workbook.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());

    }
}
