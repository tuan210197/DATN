package shupship.service.impl;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shupship.domain.model.*;
import shupship.enums.LeadStatus;
import shupship.enums.LeadType;
import shupship.repo.*;
import shupship.request.LeadAssignRequest;
import shupship.request.LeadAssignRequestV2;
import shupship.response.LeadAssignExcelResponse;
import shupship.response.LeadAssignHisResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadAssignExcelService;
import shupship.service.ILeadAssignService;
import shupship.service.MailSenderService;
import shupship.util.exception.HieuDzException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class LeadAssignServiceImpl implements ILeadAssignService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BasicLoginRepo basicLoginRepo;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    ILeadAssignRepository leadAssignRepo;

    @Autowired
    ILeadAssgnHisRepository leadAssgnHisRepository;

    @Autowired
    ILeadAssignExcelService leadAssignExcelService;

    @Autowired
    ILeadRepository leadRepository;

    @Override
    public LeadAssign createLeadAssign(Users user, LeadAssignRequest leadAssginReq) {
        Lead lead = iLeadRepository.findLeadById(leadAssginReq.getLeadId());
        if (lead == null)
            throw new HieuDzException("Khách hàng không tồn tại.");
        Users users = userRepo.getUserById(leadAssginReq.getUserRecipientId());
        if (users == null)
            throw new HieuDzException("Bưu tá không tồn tại.");

        LeadAssign findLeadAssignById = leadAssignRepo.findLeadAssignByLeadIdAndEmpId(leadAssginReq.getLeadId(), leadAssginReq.getUserRecipientId());
        if (findLeadAssignById != null && !findLeadAssignById.getUserAssigneeId().equals(findLeadAssignById.getUserRecipientId()))
            throw new HieuDzException("Đã giao tiếp xúc cho bưu tá.");

        if (lead.getIsFromEVTP() != null && lead.getIsFromEVTP().equals(1L)) {
            lead.setType(LeadType.DUOC_GIAO.getType());
            if (lead.getStatus() == 1)
                lead.setStatus(5L);
        }

        LeadAssign assign = leadAssignRepo.findLeadAssignedForPostCode(leadAssginReq.getPostCode(), leadAssginReq.getLeadId());
        if (assign != null) {
            if (assign.getUsers() == null) {
                assign.setUserAssigneeId(leadAssginReq.getUserAssigneeId());
                assign.setUserRecipientId(leadAssginReq.getUserRecipientId());
                assign.setStatus(5L);
                assign.setUsers(users);
                assign = leadAssignRepo.save(assign);
            } else {
                LeadAssign data = LeadAssignRequest.leadAssignDtoToModel(leadAssginReq);
                data.setStatus(5L);
                data.setLeads(lead);
                data.setUsers(users);
                assign = leadAssignRepo.save(data);
            }
        } else {
            try {
                LeadAssign data = LeadAssignRequest.leadAssignDtoToModel(leadAssginReq);
                data.setStatus(5L);
                data.setLeads(lead);
                data.setUsers(users);
                data.setCreatedBy(user.getEmpSystemId());
                assign = leadAssignRepo.save(data);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return assign;
    }

    @Override
    public List<LeadAssign> createLeadAssignV2(Users user, LeadAssignRequestV2 leadAssginReq) {

        List<LeadAssign> assigns = new ArrayList<>();
        if (CollectionUtils.isEmpty(leadAssginReq.getLeadIds())) {
            throw new HieuDzException("Illegal Arguments");
        }

        for (Long leadId : leadAssginReq.getLeadIds()) {
            LeadAssign assign = null;
            Lead lead = iLeadRepository.findLeadById(leadId);
            if (lead == null)
                throw new HieuDzException("Khách hàng không tồn tại");

            if (leadAssginReq.getUserRecipientId() == null || StringUtils.isEmpty(String.valueOf(leadAssginReq.getUserRecipientId())))
                throw new HieuDzException("Chưa chọn nhân viên đi tiếp xúc");

            Users employee = userRepo.getUserById(leadAssginReq.getUserRecipientId());
            if (employee == null)
                throw new HieuDzException("Nhân viên không tồn tại");

            if (employee.getIsActive() != null && employee.getIsActive() == 0)
                throw new HieuDzException("Nhân viên đã bị khóa");


            if (lead.getStatus() == 1) {

                LeadAssign leadAssign = leadAssignRepo.findLeadAssignByLeadIdAndEmpId(leadId, leadAssginReq.getUserRecipientId());
                if (leadAssign != null)
                    throw new HieuDzException("Đã giao tiếp xúc cho nhân viên");

                if (lead.getIsFromEVTP() != null && lead.getIsFromEVTP() == 1) {
                    lead.setType(1L);
                    if (lead.getStatus() == 1)
                        lead.setStatus(5L);
                }

                if (CollectionUtils.isNotEmpty(lead.getLeadAssigns())) {
                    Collection<LeadAssign> leadAssigns = lead.getLeadAssigns();

                    List<LeadAssign> listAss = leadAssigns.stream().filter(e -> (e.getStatus().equals(LeadStatus.NEW.getType()) && e.getDeletedStatus() == 0)).collect(Collectors.toList());
                    if (listAss.size() > 0) {
                        assign = listAss.get(0);
                    }

                    if (assign != null) {

                        assign.setDeptCode(leadAssginReq.getDeptCode());
                        assign.setPostCode(leadAssginReq.getPostCode());
                        assign.setUserAssigneeId(leadAssginReq.getUserAssigneeId());
                        assign.setUserRecipientId(leadAssginReq.getUserRecipientId());
                        assign.setStatus(5L);
                        assign.setUsers(employee);
                        assign.setCreatedBy(user.getCreatedBy());
                        assign = leadAssignRepo.save(assign);
                    } else {
                        LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                        data.setStatus(1L);
                        data.setLeads(lead);
                        data.setUsers(employee);
                        data.setCreatedBy(user.getCreatedBy());
                        assign = leadAssignRepo.save(data);
                    }
                } else {
                    assign = leadAssignRepo.findLeadAssignedForPostCode(leadAssginReq.getPostCode(), leadId);
                    if (assign != null) {
                        if (assign.getUsers() == null) {
                            assign.setUserAssigneeId(leadAssginReq.getUserAssigneeId());
                            assign.setUserRecipientId(leadAssginReq.getUserRecipientId());
                            assign.setStatus(5L);
                            assign.setUsers(employee);
                            assign.setCreatedBy(user.getCreatedBy());
                            assign = leadAssignRepo.save(assign);

                        } else {
                            LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                            data.setStatus(5L);
                            data.setLeads(lead);
                            data.setUsers(employee);
                            data.setCreatedBy(user.getCreatedBy());
                            assign = leadAssignRepo.save(data);

                        }
                    } else {
                        LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                        data.setStatus(5L);
                        data.setLeads(lead);
                        data.setUsers(employee);
                        data.setCreatedBy(user.getCreatedBy());
                        assign = leadAssignRepo.save(data);
                    }
                }
                assigns.add(assign);
            } else {
                LeadAssign findLeadAssignById = leadAssignRepo.getLeadAssignById(leadId);
                if (findLeadAssignById != null) {
                    findLeadAssignById.setDeletedStatus(1L);
                    leadAssignRepo.save(findLeadAssignById);
                    sendEmailAssign(findLeadAssignById, leadAssginReq.getUserRecipientId(), leadAssginReq.getNote());
                }

                if (lead.getIsFromEVTP() == null || lead.getIsFromEVTP() != null && lead.getIsFromEVTP() == 1) {
                    lead.setType(1L);
                    lead.setStatus(5L);
                }

                // chuyển tiếp xúc (thêm tiếp xúc mới)
                LeadAssign data = LeadAssignRequestV2.leadAssignDtoToModel(leadAssginReq);
                data.setStatus(5L);
                data.setLeads(lead);
                data.setUsers(employee);
                data.setCreatedBy(user.getCreatedBy());
                assign = leadAssignRepo.save(data);

                assigns.add(assign);
            }
        }
        return assigns;
    }

    @Override
    public String checkFileInput(MultipartFile multipartFile) {
        String message = null;
        if (multipartFile.isEmpty()) {
            message = "file empty";
        }
        return message;
    }

    @Override
    public LeadAssignHisResponse importFileLeadAssign(Users user, MultipartFile reapExcelDataFile) throws Exception {
        String ranName = String.valueOf(System.currentTimeMillis());
        LeadAssignHis fileLeadAssign = new LeadAssignHis();
        //bc1.2 lưu thông tin db
        fileLeadAssign.setFileName(reapExcelDataFile.getOriginalFilename());
        fileLeadAssign.setFileCreator(userRepo.getUserById(user.getEmpSystemId()));
        LeadAssignHis his = leadAssgnHisRepository.save(fileLeadAssign);

        //bc2
        List<LeadAssignExcel> listRes = leadAssignExcelService.importFile(user, reapExcelDataFile, his.getId());
        long numValid = 0;
        long numInValid = 0;
        if (CollectionUtils.isNotEmpty(listRes)) {
            his.setTotal((long) listRes.size());
            for (LeadAssignExcel i : listRes) {
                if (i.getStatus() == 0) {
                    numValid += 1;
                } else {
                    numInValid += 1;
                }
            }
        } else throw new HieuDzException("File tải lên không có dữ liệu");

        //bc3 update file info
        his.setTotalValid(numValid);
        his.setTotalInvalid(numInValid);
        his.setLeadAssignExcels(listRes);
        his.setCreatedBy(user.getEmpSystemId());
        leadAssgnHisRepository.save(his);
        return LeadAssignHisResponse.leadAssignHisToDto(his);
//        }
    }

    @Override
    public LeadAssign assignLeadForPostCode(Users users, LeadAssignRequest leadAssignRequest) throws IOException {
        Lead lead = leadRepository.findLeadById(leadAssignRequest.getLeadId());
        if (lead == null) {
            throw new HieuDzException("Không tìm thấy khách hàng");
        }
        LeadAssign data = new LeadAssign();
        data.setUserAssigneeId(users.getEmpSystemId());
        data.setDeptCode(leadAssignRequest.getDeptCode());
        data.setPostCode(leadAssignRequest.getPostCode());
        data.setStatus(1L);
        data.setLeads(lead);
        data.setCreatedBy(users.getEmpSystemId());
        return leadAssignRepo.save(data);
    }

    @Override
    public PagingRs getLeadAssignHis(Users users, Pageable pageable) {
        Page<LeadAssignHis> list = leadAssgnHisRepository.findAll(pageable);
        Page<LeadAssignHisResponse> leadAssignHisResponses = list.map(LeadAssignHisResponse::leadAssignHisToDto);
        PagingRs pagingRs = new PagingRs();
        pagingRs.setData(leadAssignHisResponses.getContent());
        pagingRs.setTotalItem(list.getTotalElements());
        return pagingRs;
    }

    @Override
    public LeadAssignHisResponse getDetailFile(Long fileId) {
        LeadAssignHis leadAssignHis = leadAssgnHisRepository.findLeadAssignHisById(fileId);
        LeadAssignHisResponse response = LeadAssignHisResponse.leadAssignHisToExcel(leadAssignHis);
        return response;
    }

    @Override
    public ByteArrayInputStream exportExcel(Collection<LeadAssignExcelResponse> list) throws IOException, InvalidFormatException {
        InputStream resource = new ClassPathResource("ChiTietFile.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(resource);
        Sheet sheet = workbook.getSheetAt(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int i = 3;
        int stt = 1;
        for (LeadAssignExcelResponse data : list) {
            Row row = sheet.createRow(i);
            row.createCell((short) 0).setCellValue(String.valueOf(stt));
            row.createCell((short) 1).setCellValue(String.valueOf(data.getCompanyName()) != null ? data.getCompanyName() : "");
            row.createCell((short) 2).setCellValue(Optional.ofNullable(data.getStatus()).isPresent() && data.getStatus() == 1 ? "Thất bại" : "Thành công");
            row.createCell((short) 3).setCellValue(String.valueOf(data.getRepresentation()) != null ? data.getRepresentation() : "");
            row.createCell((short) 4).setCellValue(String.valueOf(data.getTitle()) != null ? data.getTitle() : "");
            row.createCell((short) 5).setCellValue(String.valueOf(data.getPhone()) != null ? data.getPhone() : "");
            row.createCell((short) 6).setCellValue(String.valueOf(data.getAddress()) != null ? data.getAddress().getHomeNo() + " - " + data.getAddress().getFomatAddress() : "");
            row.createCell((short) 7).setCellValue(String.valueOf(data.getLeadSource()) != null && data.getLeadSource().equals("PRIVATE") ? "Cá nhân" : "Doanh nghiệp");
            row.createCell((short) 8).setCellValue(String.valueOf(data.getDeptCode()) != null ? data.getDeptCode() : "");
            row.createCell((short) 9).setCellValue(String.valueOf(data.getPostCode()) != null ? data.getPostCode() : "");
            i++;
            stt++;
        }

        workbook.write(out);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public File write(MultipartFile multipartFile, String uniqueName, String directoryName) throws IOException {
        if (StringUtils.isEmpty(directoryName))
            directoryName = System.getProperty("user.dir") + "/files/uploaded";

        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory directoryName including parents,
            // use directory.mkdirs(); here instead.
        }
        uniqueName = StringUtils.isEmpty(uniqueName) ? "" : uniqueName;
        File file = new File(directoryName + "/" + uniqueName + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

    public void sendEmailAssign(LeadAssign leadAssign, Long userAssigned, String reason) {
        Users users = userRepo.getUserById(leadAssign.getUserRecipientId());
        Users users1 = userRepo.getUserById(userAssigned);
        if (users == null || users1 == null)
            throw new HieuDzException("Nhân viên không tồn tại");
        BasicLogin basicLogin = basicLoginRepo.findByUserUid(users.getUid().trim());
        BasicLogin basicLogin1 = basicLoginRepo.findByUserUid(users1.getUid().trim());
        Lead lead = iLeadRepository.findLeadById(leadAssign.getLeads().getId());
        String sub = "THÔNG BÁO TIẾP XÚC";
        String content = "Hệ thống đã chuyển tiếp xúc khách hàng " + lead.getFullName() + "cho nhân viên khác. Lý do " + reason +
                "GIAO TIẾP XÚC THÌ ĐÉO ĐẶT LỊCH CÒN OM";
        mailSenderService.sendSimpleMessage(basicLogin.getEmail(), sub, content);

        String sub1 = "THÔNG BÁO TIẾP XÚC";
        String content1 = "Đồng chí vừa được chuyển tiếp xúc 1 khách hàng. Vui lòng kiểm tra lại. LO MÀ ĐẶT LỊCH KO THÌ BỐ XỬ";
        mailSenderService.sendSimpleMessage(basicLogin1.getEmail(), sub1, content1);
    }

}
