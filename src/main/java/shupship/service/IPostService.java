package shupship.service;

import org.springframework.stereotype.Service;
import shupship.domain.model.PostOffice;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    List<PostOffice> getPostByDeptCode(String id);
}
