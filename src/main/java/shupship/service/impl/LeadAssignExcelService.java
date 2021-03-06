package shupship.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shupship.domain.model.*;
import shupship.repo.*;
import shupship.request.AddressRequest;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadRequest;
import shupship.service.ILeadAssignExcelService;
import shupship.service.ILeadAssignService;
import shupship.service.ILeadService;
import shupship.util.CommonUtils;
import shupship.util.exception.HieuDzException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LeadAssignExcelService implements ILeadAssignExcelService {

    @Autowired
    public ILeadAssgnHisRepository leadAssgnHisRepository;

    @Autowired
    public IProvinceRepository provinceRepository;

    @Autowired
    public IDistrictRepository districtRepository;

    @Autowired
    public IWardRepository wardRepository;

    @Autowired
    public IPostOfficeRepository postOfficeRepository;

    @Autowired
    public ILeadRepository leadRepository;

    @Autowired
    public ILeadService leadService;

    @Autowired
    public ILeadAssignService leadAssignService;

    @Autowired
    private ILeadAssignExcelRepository leadAssignExcelRepository;

    @Override
    public List<LeadAssignExcel> importFile(Users users, MultipartFile file, Long fileId) throws Exception {
        List<LeadAssignExcel> responses = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        XSSFRow rowCheck = worksheet.getRow(6);

        if (checkIfRowIsEmpty(rowCheck))
            throw new HieuDzException("File kh??ng ????ng ?????nh d???ng file m???u!");

        if (!dataFormatter.formatCellValue(rowCheck.getCell(0)).equals("C??ng ty/C???a h??ng(*)"))
            throw new HieuDzException("File kh??ng ????ng ?????nh d???ng file m???u!");

        if (!dataFormatter.formatCellValue(rowCheck.getCell(9)).equals("Giao B??u c???c (*)"))
            throw new HieuDzException("File kh??ng ????ng ?????nh d???ng file m???u!");

        for (int i = 7; i < worksheet.getPhysicalNumberOfRows(); i++) {

            StringBuilder err = new StringBuilder();
            LeadAssignExcel leadAssignExcel = new LeadAssignExcel();
            XSSFRow row = worksheet.getRow(i);

            if (checkIfRowIsEmpty(row)) break;

            if (StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(0)))) {
                leadAssignExcel.setStatus(1L);
                err.append("Ch??a nh???p tr?????ng C??ng ty/C???a h??ng");
            } else {
                leadAssignExcel.setFullName(dataFormatter.formatCellValue(row.getCell(0)));
                leadAssignExcel.setCompanyName(dataFormatter.formatCellValue(row.getCell(0)));
            }

            if (StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(1)))) {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng Ng?????i li??n h???");
                    }
                }
            } else leadAssignExcel.setRepresentation(dataFormatter.formatCellValue(row.getCell(1)));

            if (StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(2)))) {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng Ch???c danh");
                    }
                }
            } else leadAssignExcel.setTitle(dataFormatter.formatCellValue(row.getCell(2)));

            // check valid phone
            if (StringUtils.isNotBlank(dataFormatter.formatCellValue(row.getCell(3)))) {
                if (!CommonUtils.isValidPhone(dataFormatter.formatCellValue(row.getCell(3)))) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("S??? ??i???n tho???i kh??ng ????ng ?????nh d???ng: " + dataFormatter.formatCellValue(row.getCell(3)));
                            leadAssignExcel.setPhone(dataFormatter.formatCellValue(row.getCell(3)));
                        }
                    }
                } else
                    leadAssignExcel.setPhone(dataFormatter.formatCellValue(row.getCell(3)));
            } else {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng S??? ??i???n tho???i");
                    }
                }
            }
            // Kiem tra Dia chi cu the voi Cac truong dia chi khac
            String province = dataFormatter.formatCellValue(row.getCell(4));
            String district = dataFormatter.formatCellValue(row.getCell(5));
            String ward = dataFormatter.formatCellValue(row.getCell(6));
            Address address = new Address();
            List<Province> province1 = provinceRepository.getProvinceByName(province);
            List<District> district1 = districtRepository.getDistrictByName(district);
            List<Ward> ward1 = wardRepository.getWardByName(ward, district1.get(0).getDistrictCode());
            if (StringUtils.isNotBlank(province)) {
                if (province1.get(0) == null) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("T???nh kh??ng t???n t???i ho???c sai t??n T???nh/TP");
                        }
                    }
                } else address.setProvince(province1.get(0).getProvinceCode());
            } else {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng T???nh/TP");
                    }
                }
            }

            if (StringUtils.isNotBlank(district)) {
                if (district1.get(0) == null) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("Qu???n/huy???n kh??ng t???n t???i ho???c sai ?????nh d???ng Qu???n/huy???n");
                        }
                    }
                } else if (!(district1.get(0).getProvinceCode().equals(province1.get(0).getProvinceCode()))) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("Qu???n/huy???n kh??ng thu???c " + province1.get(0).getProvinceName());
                        }
                    }
                } else address.setDistrict(district1.get(0).getDistrictCode());
            } else {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng T???nh/TP");
                    }
                }
            }

            if (StringUtils.isNotBlank(ward)) {
                if (ward1.get(0) == null) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("Ph?????ng/x?? kh??ng t???n t???i ho???c sai ?????nh d???ng Ph?????ng/x??");
                        }
                    }
                } else if (!(ward1.get(0).getDistrictCode().equals(district1.get(0).getDistrictCode()))) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("Ph?????ng/x?? kh??ng thu???c " + district1.get(0).getDistrictName());
                        }
                    }
                } else address.setWard(ward1.get(0).getWardCode());
            } else {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng T???nh/TP");
                    }
                }
            }

            if (StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(7)))) {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng ?????a ch??? c??? th???");
                    }
                }
            } else address.setHomeNo(dataFormatter.formatCellValue(row.getCell(7)));

            address.setFomatAddress(ward1.get(0).getFormattedAddress());

            if (address.getProvince() != null && address.getDistrict() != null && address.getWard() != null) {
                leadAssignExcel.setAddress(address);
            }

            ///--------------- dia ch??? ---------------

            if (dataFormatter.formatCellValue(row.getCell(8)).equals("C?? nh??n")) {
                leadAssignExcel.setLeadSource("PRIVATE");
            } else if (dataFormatter.formatCellValue(row.getCell(8)).equals("Doanh nghi???p")) {
                leadAssignExcel.setLeadSource("ENTERPRISE");
            }

            if (StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(9)))) {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Ch??a nh???p tr?????ng Giao B??u c???c");
                    }
                }
            } else {
                PostOffice deptCode = postOfficeRepository.findPostOfficeByCode(dataFormatter.formatCellValue(row.getCell(9)));
                if (deptCode == null) {
                    if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                        if (leadAssignExcel.getStatus() == null) {
                            leadAssignExcel.setStatus(1L);
                            err.append("B??u c???c ???????c giao ti???p x??c kh??ng t???n t???i: " + dataFormatter.formatCellValue(row.getCell(9)));
                            leadAssignExcel.setPostCode(dataFormatter.formatCellValue(row.getCell(9)));
                        }
                    }
                } else {
                    leadAssignExcel.setPostCode(dataFormatter.formatCellValue(row.getCell(9)));
                    leadAssignExcel.setDeptCode(deptCode.getDeptOffice().getCode());
                }
            }

//            if (StringUtils.isNotEmpty(dataFormatter.formatCellValue(row.getCell(7)))) {
//                leadAssignExcel.setEmployeeCode(dataFormatter.formatCellValue(row.getCell(7)));
//                if (StringUtils.isNotEmpty(leadAssignExcel.getPostCode())) {
//                    List<Employee> emp = employeeRepository.getEmployeeByPostCodeAndEmpCode(leadAssignExcel.getEmployeeCode(), leadAssignExcel.getPostCode());
//                    if (CollectionUtils.isEmpty(emp) || emp.get(0) == null) {
//                        if (StringUtils.isEmpty(leadAssignExcel.getError())) {
//                            if (leadAssignExcel.getStatus() == null) {
//                                leadAssignExcel.setStatus(1L);
//                                err.appendap("B??u t?? ???????c giao ti???p x??c kh??ng t???n t???i trong b??u c???c: " + dataFormatter.formatCellValue(row.getCell(7)));
//                                leadAssignExcel.setEmployeeCode(dataFormatter.formatCellValue(row.getCell(7)));
//                            }
//                        }
//                    } else
//                        leadAssignExcel.setEmpSystemId(emp.get(0).getEmpSystemId());
//                }
//            }

            List<Lead> lstLead = leadRepository.findLeadWithPhoneOnWEB(leadAssignExcel.getPhone());
            if (CollectionUtils.isNotEmpty(lstLead)) {
                if (StringUtils.isEmpty(leadAssignExcel.getError())) {
                    if (leadAssignExcel.getStatus() == null) {
                        leadAssignExcel.setStatus(1L);
                        err.append("Kh??ch h??ng ???? t???n t???i tr??n h??? th???ng , ???????c t???o ng??y : " + lstLead.get(0).getCreatedDate() + " " + lstLead.get(0).getCreatedBy());
                    }
                }
            }

            leadAssignExcel.setCreatedBy(users.getEmpSystemId());
            leadAssignExcel.setError(err.toString().equals("") ? null : (err.length() > 800 ? "Qu?? nhi???u l???i ??? d??ng :" + i : err.toString()));

            // Neu khong loi -> set status (0 la hop le)
            if (StringUtils.isEmpty(leadAssignExcel.getError()))
                leadAssignExcel.setStatus(0L);

            LeadAssignHis leadAssignHis = leadAssgnHisRepository.findLeadAssignHisById(fileId);
            leadAssignExcel.setHis(leadAssignHis);

            // Tao Khach hang
            if (leadAssignExcel.getStatus() == null || leadAssignExcel.getStatus() != 1) {
                LeadRequest leadRequest = new LeadRequest();
                leadRequest.setFullName(leadAssignExcel.getFullName());
                leadRequest.setCompanyName(leadAssignExcel.getCompanyName());
                leadRequest.setRepresentation(leadAssignExcel.getRepresentation());
                leadRequest.setTitle(leadAssignExcel.getTitle());
                leadRequest.setPhone(leadAssignExcel.getPhone());
                leadRequest.setAddress(AddressRequest.addressModelToDto(leadAssignExcel.getAddress()));
                leadRequest.setLeadSource(leadAssignExcel.getLeadSource());
                leadRequest.setIndustry(new ArrayList<>());
                leadRequest.setQuantityMonth(0.0);
                leadRequest.setWeight(0.0);
                leadRequest.setExpectedRevenue(0.0);
                leadRequest.setInProvincePercent(0.0);
                leadRequest.setOutProvincePercent(0.0);

                Lead lead = leadService.insertLead(leadRequest, users);
                // Giao tiep xuc
                LeadAssignRequest leadAssignRequest = new LeadAssignRequest();
                leadAssignRequest.setLeadId(lead.getId());
                leadAssignRequest.setDeptCode(leadAssignExcel.getDeptCode());
                leadAssignRequest.setPostCode(leadAssignExcel.getPostCode());
//                leadAssignRequest.setUserRecipientId(leadAssignExcel.getEmpSystemId());
                leadAssignRequest.setUserAssigneeId(users.getEmpSystemId());
                leadAssignRequest.setLeadAssignExcel(leadAssignExcel);

                if (leadAssignExcel.getEmpSystemId() == null && StringUtils.isNotEmpty(leadAssignExcel.getPostCode())) {
                    LeadAssign leadAssign = leadAssignService.assignLeadForPostCode(users, leadAssignRequest);
                    leadAssignExcel.setLeadAssign(leadAssign);
                }
            }
            // save DB
            LeadAssignExcel assignExcel = leadAssignExcelRepository.save(leadAssignExcel);
            responses.add(assignExcel);
        }
        return responses;
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }
}
