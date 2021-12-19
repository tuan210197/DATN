package shupship.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Users;

import java.util.List;

@Service
public interface UserService extends CrudService<Users, java.lang.String> {

    List<Users> searchUser(String role, String deptCode, String postCode, String keyword);

    List<Users> getUsersByPostCode(String postCode);

}
