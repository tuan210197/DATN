package shupship.service;

import org.springframework.context.ApplicationContextException;
import shupship.domain.dto.response.lead.PagingRs;


public interface ILeadService {
    PagingRs getListLead() throws ApplicationContextException;
}
