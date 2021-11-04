package shupship.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.model.IndustryDetail;

@Transactional
public interface IndustryDetailRepository extends PagingAndSortingRepository<IndustryDetail, Long> {
    @Query("delete from IndustryDetail pd where pd.relatedToId = :relatedId and pd.industryId = :industryId")
    @Modifying
    void deleteByRelatedToIdAndIndustryId(long relatedId, long industryId);
}