package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Ward;

@Transactional
public interface IWardRepository extends PagingAndSortingRepository<Ward, Long> {
    @Query("select a from Ward a where a.wardCode = ?1 ")
    Ward findWardByCode(String code);
}