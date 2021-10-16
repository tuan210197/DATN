package shupship.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import shupship.domain.dto.response.lead.ListLeadResponse;
import shupship.domain.model.Lead;
import shupship.repo.ILeadRepository;
import shupship.service.ILeadService;

import java.util.List;

@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Override
    public ListLeadResponse getListLead() throws ApplicationContextException {
    ListLeadResponse listLeadResponse = new ListLeadResponse();
    try{
        List<Lead> listLead = iLeadRepository.getListLead();
        listLeadResponse.setLeadList(listLead);
    }catch (Exception e){
    e.getLocalizedMessage();
    }
        return listLeadResponse;
    }
}

