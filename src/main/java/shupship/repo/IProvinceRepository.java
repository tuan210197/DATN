package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Province;

@Transactional
public interface IProvinceRepository extends PagingAndSortingRepository<Province, Long> {
    @Query("select a from Province a where a.provinceCode = ?1 ")
    Province findProvinceByCode(String code);
}
