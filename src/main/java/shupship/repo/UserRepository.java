package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Address;
import shupship.domain.model.Users;

import java.time.LocalDate;
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

    Users findByUid(String uid);
    @Transactional
    @Modifying
    @Query(value = "UPDATE Users u set u.isActive = 0 where u.uid =:uid")
    void update( String uid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users u set u.isActive = 1 where u.uid =:uid")
    void update1( String uid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users u set u.birthday=:birthday , u.fullName=:fullName, u.gender=:gender,u.mobile=:mobile, u.address=:address, u.status_update=1 where u.uid =:uid")
    void updateUser(LocalDate birthday, String fullName, Integer gender, String mobile, Address address, String uid);

    @Query(value = "select u.empSystemId from Users u where u.empSystemId = :sysId")
    Long getSysId(Long sysId);


    @Query("Select e from Users e where e.empSystemId = :id")
    Users getUserById(Long id);
}
