package shupship.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shupship.domain.model.Users;
import shupship.repo.UserRepository;
import shupship.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;

    @Override
    public Users save(Users entity) {
        return null;
    }

    @Override
    public Page list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Users> search(Specification<Users> specification, Pageable pageable) {
        return null;
    }

    @Override
    public Users get(String id) {
        return null;
    }

    @Override
    public void delete(Users entity) {

    }
}
