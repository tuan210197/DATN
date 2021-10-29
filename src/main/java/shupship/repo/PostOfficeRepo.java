package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shupship.domain.model.PostOffice;

import java.util.Optional;

public interface PostOfficeRepo extends JpaRepository<PostOffice, Long> {
    @Query(value = "select p from PostOffice p where p.deptOffice.id = :id")
    Optional<PostOffice> findByDeptId(Long id);
}
