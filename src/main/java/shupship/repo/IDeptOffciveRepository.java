package shupship.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shupship.domain.model.DeptOffice;

@Repository
public interface IDeptOffciveRepository extends PagingAndSortingRepository<DeptOffice, Long> {

}
