package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Lead;
import shupship.domain.model.LeadAssignHis;

import java.util.List;


@Repository
public interface ILeadAssgnHisRepository extends JpaRepository<LeadAssignHis, Long> {

    @Query("select h from LeadAssignHis h where h.deletedStatus = 0 and h.id = :fileId")
    LeadAssignHis findLeadAssignHisById(Long fileId);

}
