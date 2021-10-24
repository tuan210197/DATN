package shupship.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Lead;

import java.util.List;

@Repository
public interface ILeadRepository extends PagingAndSortingRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    @Query(value = "select l from Lead l ")
    Page<Lead> getListLead(Pageable pageable);

    @Query("Select a from Lead a where a.id = :id")
    Lead findLeadById(Long id);
    @Query("select l from Lead l where l.deletedStatus = 0 and l.phone = :phone")
    List<Lead> findLeadWithPhoneOnEVTP(String phone);
}
