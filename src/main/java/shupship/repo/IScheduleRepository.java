package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Schedule;

import java.util.List;

@Transactional
public interface IScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query(value = "select * from Schedule s where s.lead_id = ?1 and s.deleted_status = 0", nativeQuery = true)
    List<Schedule> getSchedulesByLeadId(Long leadId);

}
