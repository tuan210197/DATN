package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shupship.domain.model.PostOffice;
import shupship.repo.PostOfficeRepo;
import shupship.service.PostService;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostOfficeRepo postOfficeRepo;

    @Override
    public Optional<PostOffice> getPostByDeptId(Long id) {
        return postOfficeRepo.findByDeptId(id);
    }
}

