package shupship.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.LeadAssign;

@Transactional
public interface ILeadAssignRepository extends PagingAndSortingRepository<LeadAssign, Long>, JpaSpecificationExecutor<LeadAssign> {

    @Query("Select la from LeadAssign la where la.leads.id = ?1 and la.users.empSystemId = ?2 and la.deletedStatus = 0")
    LeadAssign findLeadAssignByLeadIdAndEmpId(Long leadId, Long empId);

    @Query("Select a from LeadAssign a where a.postCode = :postCode and a.leads.id = :leadId and a.deletedStatus = 0 and a.status = 1")
    LeadAssign findLeadAssignedForPostCode(String postCode, long leadId);

    @Query("delete from LeadAssign la where la.leads.id = :leadId")

    @Modifying
    void deleteAssign(long leadId);
}
