package shupship.repo;

import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Lead;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ILeadRepository extends PagingAndSortingRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    @Query(value = "select l from Lead l where l.deletedStatus <> 1")
    Page<Lead> getListLead(Pageable pageable);

    @Query("Select a from Lead a where a.id = :id and a.deletedStatus = 0")
    Lead findLeadById(Long id);

    @Query("select l from Lead l where l.deletedStatus = 0 and l.phone = :phone")
    List<Lead> findLeadWithPhoneOnWEB(String phone);

    @Query("select a from Lead a join LeadAssign b on a.id = b.leads where a.deletedStatus = 0 " +
            " and (COALESCE(:key) is null or ( a.customerCode = :key or a.phone = :key)) " +
            " and (COALESCE(:createBy) is null or ( a.createdBy = :createBy))")
    Lead searLead(String key, Long createBy);

    @Query(" Select distinct a from Lead a left join LeadAssign b on a.id = b.leads " +
            " where  a.deletedStatus = 0" +
            " and " +
            " (" +
            " ( COALESCE(:start) is null or a.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or a.createdDate <= :end)" +
            " )" +
            " and (COALESCE(:key) is null or (a.fullName = :key or a.customerCode = :key or a.companyName = :key) ) " +
            " and (COALESCE(:status) is null or a.status = :status) " +
            " and (COALESCE(:code) is null or (b.postCode = :code or b.deptCode = :code)) ")
    Page<Lead> findAllLeadbyCriteria(@Param("status") Long status,
                                     @Param("key") String key,
                                     @Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end,
                                     @Param("code") String code,
                                     Pageable pageRequest);


    @Query("  Select distinct a from Lead a left join LeadAssign b on a.id = b.leads  " +
            "             where a.deletedStatus = 0 and  " +
            "             (a.createdBy = :userId and  " +
            "             ( " +
            "             ( COALESCE(:start) is null or a.createdDate >= :start)  " +
            "             and  " +
            "             ( COALESCE(:end) is null or a.createdDate <= :end) " +
            "             ) " +
            "             and (COALESCE(:key) is null or ( a.customerCode = :key or a.phone = :key) )  " +
            "             and (COALESCE(:status) is null or a.status = :status )  " +
            "             and (COALESCE(:code) is null or (b.postCode = :code or b.deptCode = :code))) " +
            "             or ( " +
            "             ( " +
            "             ( COALESCE(:start) is null or a.createdDate >= :start)  " +
            "             and  " +
            "             ( COALESCE(:end) is null or a.createdDate <= :end) " +
            "             ) " +
            "             and (COALESCE(:key) is null or ( a.customerCode = :key or a.phone = :key) )  " +
            "             and (COALESCE(:status) is null or a.status = :status )  " +
            "             and (COALESCE(:code) is null or (b.postCode = :code or b.deptCode = :code)))")
    Page<Lead> findAllLeadbyCriteriaByCNBC(@Param("status") Long status,
                                           @Param("key") String key,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           @Param("userId") Long userId,
                                           @Param("code") String code,
                                           Pageable pageRequest);

    @Query("select distinct a from Lead a left join LeadAssign b on a.id = b.leads " +
            " where b.userRecipientId = :id and a.deletedStatus = 0" +
            " and (COALESCE(:key) is null or (a.fullName = :key or a.customerCode = :key or a.companyName = :key) )  " +
            " and " +
            " (" +
            " ( COALESCE(:start) is null or b.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or b.createdDate <= :end)" +
            " )")
    Page<Lead> findAllLeadbyCriteriaOnApp2(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           @Param("id") Long id,
                                           @Param("key") String key,
                                           Pageable pageable);

    @Query("Select distinct a from Lead a left join LeadAssign b on a.id = b.leads " +
            " where " +
            " (b.userRecipientId = :userId and a.deletedStatus = 0" +
            " and (COALESCE(:status) is null or a.status = :status)" +
            " and " +
            " ( COALESCE(:start) is null or b.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or b.createdDate <= :end)" +
            ")" +
            " or " +
            " (" +
            " (" +
            " ( COALESCE(:start) is null or a.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or a.createdDate <= :end)" +
            " ) " +
            " and a.deletedStatus = 0 and a.isFromEVTP is null" +
            " and (COALESCE(:key) is null or (a.fullName = :key or a.customerCode = :key or a.companyName = :key) )  " +
            " and a.createdBy = :userId" +
            " and (COALESCE(:status) is null or a.status = :status)" +
            " )")
    Page<Lead> findAllLeadbyCriteriaOnApp(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("userId") Long userId,
                                          @Param("status") Long status,
                                          @Param("key") String key,
                                          Pageable pageable);

    @Query("Select distinct a from Lead a left join LeadAssign b on a.id = b.leads " +
            " where " +
            " (b.userRecipientId = :userId" +
            " and ((COALESCE(:status) is null or a.status = :status) or (COALESCE(:status1) is null or a.status = :status1) )" +
            " and " +
            " ( COALESCE(:start) is null or b.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or b.createdDate <= :end)" +
            ")" +
            " or " +
            " (" +
            " (" +
            " ( COALESCE(:start) is null or a.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or a.createdDate <= :end)" +
            " ) " +
            " and a.deletedStatus = 0 and a.isFromEVTP is null" +
            " and (COALESCE(:key) is null or (a.fullName = :key or a.customerCode = :key or a.companyName = :key) )  " +
            " and a.createdBy = :userId" +
            " and ((COALESCE(:status) is null or a.status = :status) or (COALESCE(:status1) is null or a.status = :status1))" +
            " )")
    Page<Lead> findAllLeadbyCriteriaOnAppNew(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("userId") Long userId,
                                             @Param("status") Long status,
                                             @Param("status1") Long status1,
                                             @Param("key") String key,
                                             Pageable pageable);


    @Query("select l from Lead l where l.deletedStatus = 0 and l.phone = :phone")
    Lead findLeadWithPhoneByUser(String phone);
}
