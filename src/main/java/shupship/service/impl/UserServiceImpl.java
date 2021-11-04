package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shupship.domain.model.Users;
import shupship.repo.UserRepository;
import shupship.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;


//    @Override
//    public Page list(Pageable pageable) {
//        return repository.findAll(pageable);
//    }
//

    @Override
    public List<Users> searchUser(String keyword) {
        if (keyword != null) {
            return repository.search(keyword);
        }
        return repository.findAll();
    }
}
