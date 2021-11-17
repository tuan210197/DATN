package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.social.facebook.api.PageOperations;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Province;

import java.util.List;

@Transactional
public interface IProvinceRepository extends PagingAndSortingRepository<Province, Long> {
    @Query("select a from Province a where a.provinceCode = ?1 ")
    Province findProvinceByCode(String code);

    @Query("select p from Province p where p.provinceName like %:province%")
    List<Province> getProvinceByName(String province);

}
