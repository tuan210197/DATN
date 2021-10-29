package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shupship.domain.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
