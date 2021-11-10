package shupship.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shupship.domain.model.Lead;

import java.time.Instant;
import java.util.List;

@Repository
public interface ILeadRepository extends PagingAndSortingRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    @Query(value = "select l from Lead l where l.deletedStatus <> 1")
    Page<Lead> getListLead(Pageable pageable);

    @Query("Select a from Lead a where a.id = :id and a.deletedStatus = 0")
    Lead findLeadById(Long id);

    @Query("select l from Lead l where l.deletedStatus = 0 and l.phone = :phone")
    List<Lead> findLeadWithPhoneOnWEB(String phone);

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
                                     @Param("start") Instant start,
                                     @Param("end") Instant end,
                                     @Param("code") String code,
                                     Pageable pageRequest);


    @Query(" Select distinct a from Lead a left join LeadAssign b on a.id = b.leads " +
            " where ( a.deletedStatus = 0" +
            " and (COALESCE(:status) is null or a.status = :status) " +
            " and (( COALESCE(:start) is null or a.createdDate >= :start) and ( COALESCE(:end) is null or a.createdDate <= :end))" +
            " and a.createdBy = -1 or a.createdBy = :userId )" +
            " or (" +
            " (" +
            " ( COALESCE(:start) is null or a.createdDate >= :start) " +
            " and " +
            " ( COALESCE(:end) is null or a.createdDate <= :end)" +
            " )" +
            " and (COALESCE(:key) is null or (a.fullName = :key or a.customerCode = :key or a.companyName = :key) ) " +
            " and (COALESCE(:status) is null or a.status = :status) " +
            " and (COALESCE(:code) is null or (b.postCode = :code or b.deptCode = :code))) ")
    Page<Lead> findAllLeadbyCriteriaByCNBC(@Param("status") Long status,
                                           @Param("key") String key,
                                           @Param("start") Instant start,
                                           @Param("end") Instant end,
                                           @Param("userId") Long userId,
                                           @Param("code") String code,
                                           Pageable pageRequest);

    @Query("select l from Lead l where l.deletedStatus = 0 and l.phone = :phone and l.createdBy = :userId")
    Lead findLeadWithPhoneByUser(String phone, Long userId);
}
