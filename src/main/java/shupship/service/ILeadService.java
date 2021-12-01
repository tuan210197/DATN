package shupship.service;

import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Pageable;
import shupship.domain.model.Lead;
import shupship.domain.model.Users;
import shupship.dto.LeadHadPhoneResponseDto;
import shupship.dto.LeadResponseWithDescriptionDto;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.util.exception.ApplicationException;

import java.sql.Timestamp;
import java.util.HashMap;

public interface ILeadService {

    PagingRs getListLead(Pageable pageable, Timestamp from, Timestamp to, Long status, Users users) throws ApplicationContextException;

    PagingRs getListLeadOnEmp(Pageable pageable, Timestamp from, Timestamp to, Long status, Users users, String key) throws ApplicationContextException;

    Lead insertLead(LeadRequest leadRequest, Users users) throws ApplicationContextException;

    Lead updateLead(Long id, LeadUpdateRequest leadRequest) throws ApplicationException;

    Lead deleteLeadOnWEB(Long leadId) throws Exception;

    LeadResponse detailLead(Long id) throws ApplicationException;

    LeadResponseWithDescriptionDto findLeadDetail(Long id) throws Exception;

    Lead createLeadWMO(LeadRequest inputData, Users users) throws Exception;

    Lead updateLeadWMO(Long id, LeadUpdateRequest inputData) throws Exception;

    Lead deleteLeadWMO(Long leadId) throws Exception;

    LeadHadPhoneResponseDto findLeadHadPhoneByUser(LeadRequest inputData, Long userId);

    Lead searchLead(String key);

    HashMap getCustomerByPhone(String phone);
}
