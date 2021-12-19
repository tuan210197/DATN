package shupship.service.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import shupship.dao.ReportDao;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.domain.model.Users;
import shupship.dto.ReportMonthlyDeptDto;
import shupship.dto.ReportMonthlyPostOfficeExportDto;
import shupship.response.ReportResponse;
import shupship.service.IExportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExportService implements IExportService {

    @Autowired
    ReportDao reportDao;

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

    @Override
    public ByteArrayInputStream exportChiNhanh(List<ReportMonthlyDeptDto> list) throws IOException, InvalidFormatException {
        InputStream resource = new ClassPathResource("BaoCaoTXChiNhanh.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(resource);

        Sheet sheet = workbook.getSheetAt(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int i = 1;
        for (ReportMonthlyDeptDto model : list) {
            Row row = sheet.createRow(i);
            row.createCell((short) 0).setCellValue(String.valueOf(model.getDeptCode()));
            row.createCell((short) 1).setCellValue(Optional.ofNullable(model.getTotalPost()).isPresent() ? String.valueOf(model.getTotalPost()) : "");
            row.createCell((short) 2).setCellValue(Optional.ofNullable(model.getTotalCountEmp()).isPresent() ? String.valueOf(model.getTotalCountEmp()) : "");
            row.createCell((short) 3).setCellValue(Optional.ofNullable(model.getTotalAssigns()).isPresent() ? String.valueOf(model.getTotalAssigns()) : "");
            row.createCell((short) 4).setCellValue(Optional.ofNullable(model.getTotalSuccesses()).isPresent() ? String.valueOf(model.getTotalSuccesses()) : "");
            row.createCell((short) 5).setCellValue(Optional.ofNullable(model.getTotalContacting()).isPresent() ? String.valueOf(model.getTotalContacting()) : "");
            row.createCell((short) 6).setCellValue(Optional.ofNullable(model.getTotalFails()).isPresent() ? String.valueOf(model.getTotalFails()) : "");
            row.createCell((short) 7).setCellValue(Optional.ofNullable(model.getTotalLeadNotTX()).isPresent() ? String.valueOf(model.getTotalLeadNotTX()) : "");
            row.createCell((short) 8).setCellValue(Optional.ofNullable(model.getTotalEmployeeNotAssigned()).isPresent() ? String.valueOf(model.getTotalEmployeeNotAssigned()) : "");
            row.createCell((short) 9).setCellValue(Optional.ofNullable(model.getTyHT()).isPresent() ? String.valueOf(model.getTyHT()) : "");
            row.createCell((short) 10).setCellValue(Optional.ofNullable(model.getTyTX()).isPresent() ? String.valueOf(model.getTyTX()) : "");
            i++;
        }
        workbook.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportNhanVien(List<ReportMonthlyEmployeeDto> list) throws IOException, InvalidFormatException {
        InputStream resource = new ClassPathResource("BaoCaoTXNhanVien.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(resource);

        Sheet sheet = workbook.getSheetAt(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int i = 1;
        for (ReportMonthlyEmployeeDto model : list) {
            Row row = sheet.createRow(i);
            row.createCell((short) 0).setCellValue(String.valueOf(model.getEmpCode()));
            row.createCell((short) 1).setCellValue(String.valueOf(model.getFullName()));
            row.createCell((short) 2).setCellValue(Optional.ofNullable(model.getTotalAssigns()).isPresent() ? String.valueOf(model.getTotalAssigns()) : "");
            row.createCell((short) 3).setCellValue(Optional.ofNullable(model.getSuccesses()).isPresent() ? String.valueOf(model.getSuccesses()) : "");
            row.createCell((short) 4).setCellValue(Optional.ofNullable(model.getContacting()).isPresent() ? String.valueOf(model.getContacting()) : "");
            row.createCell((short) 5).setCellValue(Optional.ofNullable(model.getFails()).isPresent() ? String.valueOf(model.getFails()) : "");
            row.createCell((short) 6).setCellValue(Optional.ofNullable(model.getAssigned()).isPresent() ? String.valueOf(model.getAssigned()) : "");
            i++;
        }
        workbook.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportDataToExcel(Timestamp startTimestamp, Timestamp endTimestamp, Users users) throws IOException, InvalidFormatException {
        InputStream resource = new ClassPathResource("BaoCao.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(resource);

        Sheet sheet = workbook.getSheetAt(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<ReportResponse> reportResponses = reportDao.exportDataToExcelFile(startTimestamp, endTimestamp, users);
        int i = 1;
        int k = 0;
        for (ReportResponse model : reportResponses) {
            Row row = sheet.createRow(i);
            row.createCell((short) k).setCellValue(String.valueOf(model.getEmployeeCode()) != null ? String.valueOf(model.getEmployeeCode()) : "");
            row.createCell((short) ++k).setCellValue(String.valueOf(model.getPostCode()) != null ? String.valueOf(model.getPostCode()) : "");
            row.createCell((short) ++k).setCellValue(String.valueOf(model.getDeptCode()) != null ? String.valueOf(model.getDeptCode())  : "");
            row.createCell((short) ++k).setCellValue(String.valueOf(model.getCustomorCode()) != null ? String.valueOf(model.getCustomorCode()) : "");
            row.createCell((short) ++k).setCellValue(String.valueOf(model.getFullName()) != null ? String.valueOf(model.getFullName()) : "");
            row.createCell((short) ++k).setCellValue(model.getFomataddress() != null ? String.valueOf(model.getHomeNo()) + " " + String.valueOf(model.getFomataddress()): "");
            row.createCell((short) ++k).setCellValue(model.getRepresentation() != null ? String.valueOf(model.getRepresentation()) : "");
            row.createCell((short) ++k).setCellValue(model.getPhone() != null ? String.valueOf(model.getPhone()) : "");
            row.createCell((short) ++k).setCellValue(model.getLeadSource() != null ? getType(model.getLeadSource()) : "");
            row.createCell((short) ++k).setCellValue(model.getSchedulerDate() != null ? String.valueOf(model.getSchedulerDate()) : "");
            row.createCell((short) ++k).setCellValue(model.getResultDate() != null ? String.valueOf(model.getResultDate()) : "");
            row.createCell((short) ++k).setCellValue(Optional.ofNullable(model.getStatus()).isPresent() ? getStatus(model.getStatus()) : "");
            row.createCell((short) ++k).setCellValue(Optional.ofNullable(model.getDiscout()).isPresent() ? String.valueOf(model.getDiscout()) : "");
            row.createCell((short) ++k).setCellValue(Optional.ofNullable(model.getInProvincePrice()).isPresent() ? String.valueOf(model.getInProvincePrice()) : "");
            row.createCell((short) ++k).setCellValue(Optional.ofNullable(model.getOutProvincePrice()).isPresent() ? String.valueOf(model.getOutProvincePrice()) : "");
            row.createCell((short) ++k).setCellValue(model.getProposal() != null ? String.valueOf(model.getProposal()) : "");
            row.createCell((short) ++k).setCellValue(model.getLeadAssignTime() != null ? String.valueOf(model.getLeadAssignTime()) : "");
            i++;
            k = 0;
        }
        workbook.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public String getType(String type){
        if (type.equals("PRIVATE")){
            return "Cá nhân";
        } else return "Doanh nghiệp";
    }

    public String getStatus(Long status){
        if (status == 1)
            return "Gửi báo giá";
        else if (status == 3)
            return "Thành công";
        else return "Thất bại";
    }

}
