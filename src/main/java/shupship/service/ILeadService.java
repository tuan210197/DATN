package shupship.service;

import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shupship.domain.model.Lead;
import shupship.request.LeadRequest;
import shupship.request.LeadUpdateRequest;
import shupship.response.PagingRs;
import shupship.util.exception.ApplicationException;

@Service
public interface ILeadService {

    PagingRs getListLead(Pageable pageable) throws ApplicationContextException;

    Lead insertLead(LeadRequest leadRequest) throws ApplicationContextException;
    Lead updateLead(Long id, LeadUpdateRequest leadRequest) throws ApplicationException;
    Lead deleteLeadOnEVTP(Long leadId) throws  ApplicationException;
}
