package shupship.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shupship.domain.dto.UserLoginDTO;
import shupship.domain.model.BasicLogin;

import javax.transaction.Transactional;

@Repository
public interface BasicLoginRepo extends JpaRepository<BasicLogin, String> {

    @Query("select a from BasicLogin a where a.email = :email")
    BasicLogin findByEmail(String email);

    BasicLogin findByUserUid(String userUid);

    void deleteByEmail(String email);


}
