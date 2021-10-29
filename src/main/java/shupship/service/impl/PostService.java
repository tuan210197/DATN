package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.PostOffice;
import shupship.repo.IPostOfficeRepository;
import shupship.service.IPostService;

import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostOfficeRepository postOfficeRepository;

    @Override
    public List<PostOffice> getPostByDeptId(Long id) {
        return postOfficeRepository.getPostOfficeByDeptCode(id);
    }
}

