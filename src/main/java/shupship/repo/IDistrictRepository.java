package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.District;

import java.util.List;

@Transactional
public interface IDistrictRepository extends PagingAndSortingRepository<District, Long> {
    @Query("select a from District a where a.districtCode = ?1 ")
    District findDistrictByCode(String code);

    @Query("select d from District d where d.provinceCode = :provinceCode ")
    List<District> getDistrictByProvinceCode(String provinceCode);

    @Query("select d from District d where d.districtName like %:district%")
    List<District> getDistrictByName(String district);

}