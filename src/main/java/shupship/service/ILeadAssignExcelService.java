package shupship.service;

import org.springframework.web.multipart.MultipartFile;
import shupship.domain.model.LeadAssignExcel;
import shupship.domain.model.Users;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ILeadAssignExcelService {
    List<LeadAssignExcel> importFile(Users users, MultipartFile file, Long fileId) throws Exception;
}
