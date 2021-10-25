package shupship.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.Industry;

import java.util.List;

@Transactional
public interface IIndustryRepository extends PagingAndSortingRepository<Industry, Long> {

    @Query("Select a from  Industry a where a.id = :id and a.deletedStatus = 0")
    public Industry getProductById(Long id);

    @Query("Select a from  Industry a where a.deletedStatus = 0")
    public Page<Industry> findAll(Pageable pageable);

    Industry findByCode(String code);

    List<Industry> findIndustriesByCodeIn(List<String> code);

    @Query("Select a from  Industry a where a.deletedStatus = 0")
    public List<Industry> getAllIndustry();
}