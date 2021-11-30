package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.PostOffice;

import java.util.List;

@Transactional
public interface IPostOfficeRepository extends PagingAndSortingRepository<PostOffice, Long> {

    @Query("select a from PostOffice a where a.postCode = ?1 ")
    PostOffice findPostOfficeByCode(String code);

    @Query("select a from PostOffice a where a.deptCode = :deptCode ")
    List<PostOffice> getPostOfficeByDeptCode(String deptCode);

}