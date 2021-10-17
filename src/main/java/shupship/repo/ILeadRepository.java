package shupship.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import shupship.domain.model.Lead;
import java.util.List;

public interface ILeadRepository extends PagingAndSortingRepository<Lead,Long>, JpaSpecificationExecutor<Lead> {
    @Query(value = "select  sc from Lead")
    List<Lead> getListLead();
}
