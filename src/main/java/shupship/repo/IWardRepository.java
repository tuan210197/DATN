package shupship.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.District;
import shupship.domain.model.Ward;

import java.util.List;

@Transactional
public interface IWardRepository extends PagingAndSortingRepository<Ward, Long> {
    @Query("select a from Ward a where a.wardCode = ?1 ")
    Ward findWardByCode(String code);

    @Query("Select w from Ward w where w.districtCode = :districtCode")
    List<Ward> getWardByDistrictCode(String districtCode);

    @Query("select d from Ward d where d.wardName like %:ward%")
    List<Ward> getWardByName(String ward);
}