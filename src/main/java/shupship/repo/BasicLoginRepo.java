package shupship.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shupship.domain.dto.UserLoginDTO;
import shupship.domain.model.BasicLogin;

@Repository
public interface BasicLoginRepo extends JpaRepository<BasicLogin, String> {

    BasicLogin findByEmail(String email);

    BasicLogin findByUserUid(String userUid);

    void deleteByEmail(String email);


}
