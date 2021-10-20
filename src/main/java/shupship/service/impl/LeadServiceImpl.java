package shupship.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shupship.domain.model.Lead;
import shupship.repo.ILeadRepository;
import shupship.request.LeadRequest;
import shupship.response.LeadResponse;
import shupship.response.PagingRs;
import shupship.service.ILeadService;



@Log4j2
@Service
public class LeadServiceImpl implements ILeadService {
    @Autowired
    ILeadRepository iLeadRepository;

    @Override
    public PagingRs getListLead(Pageable pageable) throws ApplicationContextException {

        Page<Lead> leadList = iLeadRepository.getListLead(pageable);
//        List<LeadResponse> leadResponses = new ArrayList<>();
//        for (Lead model : leadList){
//            LeadResponse laLeadResponse = LeadResponse.leadModelToDto(model);
//            leadResponses.add(laLeadResponse);
//        }
        Page<LeadResponse> leadResponses = leadList.map(LeadResponse::leadModelToDto);
        PagingRs listLeadResponse = new PagingRs();
        listLeadResponse.setTotalItem(leadList.getTotalElements());
        listLeadResponse.setData(leadResponses.getContent());
        return listLeadResponse;
    }

    @Override
    public PagingRs insertLead(LeadRequest leadRequest) throws ApplicationContextException {

        return null;
    }

}

