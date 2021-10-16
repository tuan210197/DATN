package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.District;

@Transactional
public interface IDistrictRepository extends PagingAndSortingRepository<District, Long> {
    @Query("select a from District a where a.districtCode = ?1 ")
    District findDistrictByCode(String code);
}