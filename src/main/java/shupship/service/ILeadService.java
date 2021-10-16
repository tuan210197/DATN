package shupship.service;

import org.springframework.context.ApplicationContextException;
import shupship.domain.dto.response.lead.ListLeadResponse;

public interface ILeadService {
    ListLeadResponse getListLead() throws ApplicationContextException;
}
