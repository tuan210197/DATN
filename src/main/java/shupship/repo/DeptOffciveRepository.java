package shupship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shupship.domain.model.DeptOffice;


import java.util.List;
@Repository
public interface DeptOffciveRepository extends JpaRepository<DeptOffice,Long> {

    @Query(value = "select do2.deptCode, do2.deptName from DeptOffice do2 ")
    List<DeptOffice> getAllDeptOffice();

}
