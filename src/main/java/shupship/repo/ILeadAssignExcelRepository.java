package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import shupship.domain.model.LeadAssignExcel;

public interface ILeadAssignExcelRepository extends JpaRepository<LeadAssignExcel, Long> {
}
