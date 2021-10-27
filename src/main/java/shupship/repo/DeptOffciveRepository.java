package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shupship.domain.model.DeptOffice;

import java.util.List;
@Repository
public interface DeptOffciveRepository extends JpaRepository<DeptOffice,Long> {

    @Query(value = "select do2.deptcode , do2.deptname from dept_office do2 ", nativeQuery = true)
    List<DeptOffice> getAllDeptOffice();
}
