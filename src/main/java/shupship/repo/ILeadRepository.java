package shupship.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Lead;
import java.util.List;

@Repository
public interface ILeadRepository extends PagingAndSortingRepository<Lead,Long>, JpaSpecificationExecutor<Lead> {
    @Query("select l from Lead l ")
    List<Lead> getListLead();
}
