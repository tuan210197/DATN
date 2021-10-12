package shupship.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shupship.domain.model.AppUser;


@Repository
public interface AppUserRepo extends JpaRepository<AppUser, String> {

    AppUser findByUid(String uid);

    @Query(value = "INSERT INTO APP_USER(UID, AVATAR, BIRTHDAY, FULL_NAME, GENDER, IS_ACTIVE, IS_DELETED, MOBILE, NAME) " +
            "VALUES(:#{#appUser.getUid()}, :#{#appUser.getAvatar()}, :#{#appUser.getBirthday()}, :#{#appUser.getFullName()}, " +
            ":#{#appUser.getGender()}, :#{#appUser.getIsActive()}, :#{#appUser.getIsDeleted()}, " +
            ":#{#appUser.getMobile()}, :#{#appUser.getName()});\n"
            , nativeQuery = true)
    AppUser insertAppUser(@Param("appUser") AppUser appUser);

}
