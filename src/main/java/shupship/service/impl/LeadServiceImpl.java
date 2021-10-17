package shupship.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import shupship.domain.model.Lead;
import shupship.repo.ILeadRepository;
import shupship.response.PagingRs;
import shupship.service.ILeadService;

import java.util.List;

@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Override
    public PagingRs getListLead() throws ApplicationContextException {
        PagingRs listLeadResponse = new PagingRs();
        try {
            List<Lead> listLead = iLeadRepository.getListLead();
            listLeadResponse.setData(listLead);
            listLeadResponse.setTotalItem(listLead.size());
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return listLeadResponse;
    }
}

