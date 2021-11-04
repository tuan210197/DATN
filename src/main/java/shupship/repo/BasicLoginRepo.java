package shupship.repo;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shupship.domain.model.BasicLogin;


@Repository
public interface BasicLoginRepo extends PagingAndSortingRepository<BasicLogin, String>, JpaSpecificationExecutor<BasicLogin> {

    @Query("select a from BasicLogin a where a.email = :email")
    BasicLogin findByEmail(String email);

    BasicLogin findByUserUid(String userUid);

    void deleteByEmail(String email);


}
