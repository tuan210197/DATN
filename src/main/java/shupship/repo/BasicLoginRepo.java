package shupship.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shupship.domain.model.BasicLogin;

import java.util.List;

@Repository
public interface BasicLoginRepo extends JpaRepository<BasicLogin, String> {

    BasicLogin findByEmail(String email);

    BasicLogin findByUserUid(String userUid);



}
