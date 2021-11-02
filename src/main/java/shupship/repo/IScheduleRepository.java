package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Schedule;

import java.util.List;

@Transactional
public interface IScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(value = "select * from Schedule s where s.lead_id = ?1 and s.deleted_status = 0", nativeQuery = true)
    List<Schedule> getSchedulesByLeadId(Long leadId);

    @Query(value = "select s from Schedule s where s.createdBy = ?1 and s.deletedStatus = 0")
    List<Schedule> getSchedulesByUserId(Long sysId);

    @Query(value = "SELECT * from schedule s where s.created_by = ?1 and s.lead_id = ?2 and s.is_latest = 1 and s.deleted_status = 0 order by s.to_time desc limit 1", nativeQuery = true)
    Schedule getLatestScheduleByUserIdAndLeadId(Long sysId, Long leadId);

}
