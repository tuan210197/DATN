package shupship.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.dto.ReportAllDepts;
import shupship.dto.ReportMonthlyDeptDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportDao {

    @Qualifier("postgresEntityManager")
    @Autowired
    private EntityManager entityManager;

//    @Autowired
//    private EntityManager entityManager;


    public List<ReportMonthlyDeptDto> reportAllCrm(Timestamp startDate, Timestamp endDate) {

        String query = "SELECT d.deptcode, a.postcode," +
                "                 (select count(*) from users e where a.postcode = e.post_code and e.is_active = 1) as employees," +
                "                 sum(case when la.lead_id is not null then 1 else 0 end)                             as total_assigns," +
                "                 sum(case when la.status = 2 then 1 else 0 end)                                      as contacting," +
                "                 sum(case when la.status = 3 then 1 else 0 end)                                      as successes," +
                "                 sum(case when la.status = 4 then 1 else 0 end)                                      as fails," +
                "                 (select count(*)" +
                "                 from (select e1.emp_system_id from users e1 where e1.is_active = 1 and e1.post_code = a.postcode and e1.emp_system_id not in" +
                "                 (select user_recipient_id from lead_assign la1 where la1.post_code = a.postcode)) as foo)   as employee_not_assigned," +
                "                 sum(case when la.user_assignee_id != la.user_recipient_id then 1 else 0 end)        as assigned" +
                "                 FROM post_office a" +
                "                     left join dept_office d on a.dept_office_id = d.id" +
                "                     left join lead_assign la on a.postcode = la.post_code and la.created_date between :startDate and :endDate " +
                "                 group by (d.deptcode, a.postcode) ";

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("endDate", endDate);

        List<Object[]> data = nativeQuery.getResultList();
        List<ReportAllDepts> results = new ArrayList<>();
        for (Object[] o : data) {
            ReportAllDepts report = new ReportAllDepts();

            report.setDeptCode((String) o[0]);
            report.setPostCode((String) o[1]);
            report.setEmployees((BigInteger) o[2]);
            report.setTotalAssigns((BigInteger) o[3]);
            report.setContacting((BigInteger) o[4]);
            report.setSuccesses((BigInteger) o[5]);
            report.setFails((BigInteger) o[6]);
            report.setEmployeeNotAssigned((BigInteger) o[7]);
            report.setAssigned((BigInteger) o[8]);

            results.add(report);
        }

        Map<String, List<ReportAllDepts>> map = results.stream().collect(Collectors.groupingBy(ReportAllDepts::getDeptCode));

        List<ReportMonthlyDeptDto> response = new ArrayList<>();
        for (final Map.Entry<String, List<ReportAllDepts>> entry : map.entrySet()) {
            ReportMonthlyDeptDto res = new ReportMonthlyDeptDto();
            res.setDeptCode(entry.getKey());
            res.setReportMonthlyPostOfficeDtos(ReportMonthlyDeptDto.convertList(entry.getValue()));

            response.add(res);
        }
        return response;
    }

    public List<ReportMonthlyEmployeeDto> reportByPost(Timestamp startDate, Timestamp endDate, String postCode) {

        String query = "";
        if (StringUtils.isNotEmpty(postCode)) {
            query = "SELECT e.emp_system_id, e.employee_code, e.full_name, " +
                    " sum(case when la.lead_id is not null then 1 else 0 end)                       as total_assigns, " +
                    " sum(case when la.status = 2 then 1 else 0 end)                                as contacting, " +
                    " sum(case when la.status = 3 then 1 else 0 end)                                as successes, " +
                    " sum(case when la.status = 4 then 1 else 0 end)                                as fails, " +
                    " sum(case when la.user_assignee_id != la.user_recipient_id then 1 else 0 end)  as assigned, " +
                    " (select sum (l.expected_revenue) as expected_revenue) " +
                    " FROM users e left join lead_assign la on e.emp_system_id = la.user_recipient_id and " +
                    " la.created_date between :startDate and :endDate " +
                    " left join lead l on la.lead_id = l.id " +
                    " where e.post_code = :postCode and e.is_active = 1 " +
                    " group by (e.emp_system_id, e.employee_code, e.full_name)";
        } else {
            query = "SELECT e.emp_system_id, e.employee_code, e.full_name, " +
                    " sum(case when la.lead_id is not null then 1 else 0 end)                       as total_assigns, " +
                    " sum(case when la.status = 2 then 1 else 0 end)                                as contacting, " +
                    " sum(case when la.status = 3 then 1 else 0 end)                                as successes, " +
                    " sum(case when la.status = 4 then 1 else 0 end)                                as fails, " +
                    " sum(case when la.user_assignee_id != la.user_recipient_id then 1 else 0 end)  as assigned, " +
                    " (select sum (l.expected_revenue) as expected_revenue) " +
                    " FROM users e left join lead_assign la on e.emp_system_id = la.user_recipient_id and " +
                    " la.created_date between :startDate and :endDate " +
                    " left join lead l on la.lead_id = l.id " +
                    " where e.is_active = 1 " +
                    " group by (e.emp_system_id, e.employee_code, e.full_name)";
        }
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("endDate", endDate);
        if (StringUtils.isNotEmpty(postCode))
            nativeQuery.setParameter("postCode", postCode);

        List<Object[]> data = nativeQuery.getResultList();
        List<ReportMonthlyEmployeeDto> results = new ArrayList<>();
        for (Object[] o : data) {
            ReportMonthlyEmployeeDto report = new ReportMonthlyEmployeeDto();

            report.setEmpSystemId((BigInteger) o[0]);
            report.setEmpCode((String) o[1]);
            report.setFullName((String) o[2]);
            report.setTotalAssigns((BigInteger) o[3]);
            report.setContacting((BigInteger) o[4]);
            report.setSuccesses((BigInteger) o[5]);
            report.setFails((BigInteger) o[6]);
            report.setAssigned((BigInteger) o[7]);
            if (o[8] != null)
                report.setExpectedRevenue(BigDecimal.valueOf((Double) o[8]) );
            else
                report.setExpectedRevenue(BigDecimal.ZERO);
            results.add(report);
        }

        return results;
    }


    public List<ReportMonthlyEmployeeDto> reportAllEmpsInDept(Timestamp startDate, Timestamp endDate, String deptCode) {

        String query = "SELECT e.emp_system_id, " +
                "       e.employee_code, " +
                "       e.full_name, " +
                "       po.postcode, " +
                "       sum(case when la.lead_id is not null then 1 else 0 end)                      as total_assigns, " +
                "       sum(case when la.status = 2 then 1 else 0 end)                               as contacting, " +
                "       sum(case when la.status = 3 then 1 else 0 end)                               as successes, " +
                "       sum(case when la.status = 4 then 1 else 0 end)                               as fails, " +
                "       sum(case when la.user_assignee_id != la.user_recipient_id then 1 else 0 end) as assigned, " +
                "       (select sum(l.expected_revenue) as expected_revenue) " +
                "   FROM users e " +
                "         left join lead_assign la " +
                "                   on e.emp_system_id = la.user_recipient_id and la.created_date between :startDate and :endDate " +
                "         left join lead l on la.lead_id = l.id and l.deleted_status = 0 " +
                "         left join post_office po on e.post_code = po.postcode " +
                "         left join dept_office d on po.dept_office_id = d.id " +
                "         where d.deptcode = :deptCode " +
                "   and e.is_active = 1 " +
                "   group by (e.emp_system_id, e.employee_code, e.full_name, po.postcode) " +
                "   order by po.postcode ";

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("endDate", endDate);
        nativeQuery.setParameter("deptCode", deptCode);

        List<Object[]> data = nativeQuery.getResultList();
        List<ReportMonthlyEmployeeDto> results = new ArrayList<>();
        for (Object[] o : data) {
            ReportMonthlyEmployeeDto report = new ReportMonthlyEmployeeDto();

            report.setEmpSystemId((BigInteger) o[0]);
            report.setEmpCode((String) o[1]);
            report.setFullName((String) o[2]);
            report.setPostCode((String) o[3]);
            report.setTotalAssigns((BigInteger) o[4]);
            report.setContacting((BigInteger) o[5]);
            report.setSuccesses((BigInteger) o[6]);
            report.setFails((BigInteger) o[7]);
            report.setAssigned((BigInteger) o[8]);
            if (o[9] != null)
                report.setExpectedRevenue(BigDecimal.valueOf((Double) o[9]));
            else
                report.setExpectedRevenue(BigDecimal.ZERO);
            results.add(report);
        }

        return results;
    }

}
