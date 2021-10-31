package shupship.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Users;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface UserRepo extends JpaRepository<Users, String> {

    Users findByUid(String uid);
//    Users findByEmail(String email);
    @Query(value = "select u.role_name from users u where uid = ?1", nativeQuery = true)
    List<String> findRoleNameByUid(String uid);

    @Query(value = "select u.empSystemId from Users u where u.empSystemId = :sysId")
    Long getSysId(Long sysId);

//    @Query(value = "INSERT INTO APP_USER(UID, AVATAR, BIRTHDAY, FULL_NAME, GENDER, IS_ACTIVE, IS_DELETED, MOBILE, NAME) " +
//            "VALUES(:#{#appUser.getUid()}, :#{#appUser.getAvatar()}, :#{#appUser.getBirthday()}, :#{#appUser.getFullName()}, " +
//            ":#{#appUser.getGender()}, :#{#appUser.getIsActive()}, :#{#appUser.getIsDeleted()}, " +
//            ":#{#appUser.getMobile()}, :#{#appUser.getName()});\n"
//            , nativeQuery = true)
//    Users insertAppUser(@Param("user") Users user);

}
