package shupship.service;

import org.springframework.context.ApplicationContextException;
import shupship.response.PagingRs;


public interface ILeadService {
    PagingRs getListLead() throws ApplicationContextException;
}
