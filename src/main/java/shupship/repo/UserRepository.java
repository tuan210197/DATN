package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import shupship.domain.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

}
