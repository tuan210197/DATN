package shupship.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import shupship.domain.model.LeadAssignExcel;
import shupship.domain.model.Users;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ILeadAssignExcelService {
    List<LeadAssignExcel> importFile(Users users, File file, Long fileId) throws Exception;
}
