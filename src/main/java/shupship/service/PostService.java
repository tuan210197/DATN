package shupship.service;

import org.springframework.stereotype.Service;
import shupship.domain.model.PostOffice;

import java.util.Optional;

@Service
public interface PostService {
    Optional<PostOffice> getPostByDeptId(Long id);
}
