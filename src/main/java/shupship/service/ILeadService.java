package shupship.service;

import org.springframework.context.ApplicationContextException;
import shupship.response.ListLeadResponse;

public interface ILeadService {
    ListLeadResponse getListLead() throws ApplicationContextException;
}
