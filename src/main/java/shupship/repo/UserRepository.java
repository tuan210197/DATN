package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Users;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    @Query(value = "SELECT u FROM Users u WHERE u.fullName like %:keyword%"
            + " OR u.postCode like %:keyword%"
            + " OR u.deptCode like %:keyword%"
            + " OR u.employeeCode like %:keyword%"
            + " OR u.mobile like %:keyword%"
    )
    List<Users> search(@Param("keyword") String keyword);

    @Query("select a from Users a where a.isActive = 1 and a.postCode = :postCode")
    List<Users> getUsersByPostCode(String postCode);

    @Query(value = "SELECT u FROM Users u WHERE u.deptCode = :cn and "
            + " u.employeeCode like %:keyword% "
            + " OR u.mobile like %:keyword% "
    )
    List<Users> searchCN( String cn,@Param("keyword") String keyword);

    @Query(value = "SELECT u FROM Users u WHERE u.postCode = :bc AND "
            + " u.employeeCode like %:keyword% "
            + " OR u.mobile like %:keyword% "
    )
    List<Users> searchBC( String bc,@Param("keyword") String keyword);

}
