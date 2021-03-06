package shupship.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Lead;
import shupship.domain.model.Schedule;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface IScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select s from Schedule s where s.lead.id = :leadId and s.deletedStatus = 0")
    List<Schedule> getSchedulesByLeadId(Long leadId);

    @Query(value = "select s from Schedule s where s.createdBy = ?1 and s.deletedStatus = 0")
    List<Schedule> getSchedulesByUserId(Long sysId);

    @Query("Select a from Schedule a where a.deletedStatus = 0 and a.isLatestResult = 1 and a.createdBy = :userId")
    Schedule getScheduleLatestResultById(Long userId);

    @Query("select s from Schedule s where s.createdBy = ?1 and s.nextScheduleId = ?2 and s.deletedStatus = 0")
    Schedule getNextScheduleByUserIdAndScheduleId(Long userId, Long scheduleId);

    @Query(value = "SELECT * from schedule s where s.created_by = ?1 and s.lead_id = ?2 and s.is_latest = 1 and s.deleted_status = 0 order by s.to_time desc limit 1", nativeQuery = true)
    Schedule getLatestScheduleByUserIdAndLeadId(Long sysId, Long leadId);

    @Query("Select a from Schedule a where a.id = :id and a.deletedStatus = 0 and a.createdBy = :sysId")
    Schedule getScheduleById(Long id, Long sysId);

    @Query("Select a from Schedule a where a.fromDate >= :startDate and a.fromDate <=:endDate and a.deletedStatus = 0 and a.createdBy = :userId")
    Page<Schedule> findAll(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, Long userId);

    @Query("Select a from Schedule a where a.fromDate >= :startDate and a.fromDate <= :endDate and a.deletedStatus = 0 and a.createdBy = :userId")
    List<Schedule> findAllByUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId);

    @Query("update Schedule s set s.deletedStatus = 1 where s.id = :id and s.createdBy = :userId")
    @Modifying
    void deleteSchedule(long id, Long userId);

    @Query("Select a from Schedule a where a.id = :id and a.deletedStatus = 0")
    Schedule findScheduleById(Long id);

}
