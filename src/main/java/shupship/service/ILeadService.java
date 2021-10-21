package shupship.service;

import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shupship.domain.model.Lead;
import shupship.request.LeadRequest;
import shupship.response.PagingRs;

@Service
public interface ILeadService {

    PagingRs getListLead(Pageable pageable) throws ApplicationContextException;

    Lead insertLead(LeadRequest leadRequest) throws ApplicationContextException;
}
