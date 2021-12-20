package shupship.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shupship.domain.dto.ReportMonthlyEmployeeDto;
import shupship.domain.model.Users;
import shupship.dto.ReportAllDepts;
import shupship.dto.ReportMonthlyDeptDto;
import shupship.response.ReportEmployeeOnApp;
import shupship.response.ReportResponse;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportDao {

    @Qualifier("postgresEntityManager")
    @Autowired
    private EntityManager entityManager;

//    @Autowired
//    private EntityManager entityManager;


    public List<ReportMonthlyDeptDto> reportAllCrm(Timestamp startDate, Timestamp endDate) {

        String query = "SELECT d.code, a.postcode," +
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
                "                     left join dept_office d on a.deptCode = d.code" +
                "                     left join lead_assign la on a.postcode = la.post_code and la.created_date between :startDate and :endDate " +
                "                 group by (d.code, a.postcode) ";

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("endDate", endDate);

        List<Object[]> data = nativeQuery.getResultList();
        List<ReportAllDepts> results = new ArrayList<>();
        for (Object[] o : data) {
            ReportAllDepts report = new ReportAllDepts();

            report.setDeptCode((String) o[0]);
            report.setPostCode((String) o[1]);
            report.setEmployees(Long.valueOf(String.valueOf((BigInteger) o[2])));
            report.setTotalAssigns(Long.valueOf(String.valueOf((BigInteger) o[3])));
            report.setContacting(Long.valueOf(String.valueOf((BigInteger) o[4])));
            report.setSuccesses(Long.valueOf(String.valueOf((BigInteger) o[5])));
            report.setFails(Long.valueOf(String.valueOf((BigInteger) o[6])));
            report.setEmployeeNotAssigned(Long.valueOf(String.valueOf((BigInteger) o[7])));
            report.setAssigned(Long.valueOf(String.valueOf((BigInteger) o[8])));

            results.add(report);
        }

        for (ReportAllDepts model : results) {
            if (model.getDeptCode() == null || StringUtils.isEmpty(model.getDeptCode()))
                System.out.println("NULL " + model);
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
                report.setExpectedRevenue(BigDecimal.valueOf((Double) o[8]));
            else
                report.setExpectedRevenue(BigDecimal.ZERO);
            results.add(report);
        }

        return results;
    }


    public List<ReportMonthlyEmployeeDto> reportAllEmpsInDept(Timestamp startDate, Timestamp endDate, String deptCode) {

        String query = null;
        if (StringUtils.isNotEmpty(deptCode)) {
            query = "SELECT e.emp_system_id, " +
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
                    "         left join dept_office d on po.deptCode = d.code " +
                    "         where d.code = :deptCode " +
                    "   and e.is_active = 1 " +
                    "   group by (e.emp_system_id, e.employee_code, e.full_name, po.postcode) " +
                    "   order by po.postcode ";
        } else {
            query = "SELECT e.emp_system_id, " +
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
                    "         left join dept_office d on po.deptCode = d.code " +
                    "         where e.is_active = 1  " +
                    "   group by (e.emp_system_id, e.employee_code, e.full_name, po.postcode) " +
                    "   order by po.postcode ";
        }

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("endDate", endDate);
        if (deptCode != null)
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

    public List<ReportEmployeeOnApp> reportOfEmployee(Timestamp startDate, Timestamp endDate, Long id) {

        String query = "SELECT e.emp_system_id, " +
                "       e.employee_code, " +
                "       e.full_name, " +
                "       po.postcode, " +
                "       sum(case when la.lead_id is not null then 1 else 0 end)                      as total_assigns, " +
                "       sum(case when la.status = 2 then 1 else 0 end)                               as contacting, " +
                "       sum(case when la.status = 3 then 1 else 0 end)                               as successes, " +
                "       sum(case when la.status = 4 then 1 else 0 end)                               as fails, " +
                "       sum(case when la.user_assignee_id != la.user_recipient_id then 1 else 0 end) as assigned, " +
                "       (select sum(l.expected_revenue) as expected_revenue), " +
                "       sum(case when l.is_from_evtp is null then 1 else 0 end)                      as tuNhap, " +
                "       sum(case when l.is_from_evtp = 1 then 1 else 0 end)                          as duocGiao" +
                "   FROM users e " +
                "         left join lead_assign la " +
                "                   on e.emp_system_id = la.user_recipient_id and la.created_date between :startDate and :endDate " +
                "         left join lead l on la.lead_id = l.id and l.deleted_status = 0 " +
                "         left join post_office po on e.post_code = po.postcode " +
                "         left join dept_office d on po.deptCode = d.code " +
                "         where e.emp_system_id = :id " +
                "   and e.is_active = 1 " +
                "   group by (e.emp_system_id, e.employee_code, e.full_name, po.postcode) " +
                "   order by po.postcode ";

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("endDate", endDate);
        nativeQuery.setParameter("id", id);

        List<Object[]> data = nativeQuery.getResultList();
        List<ReportEmployeeOnApp> results = new ArrayList<>();
        for (Object[] o : data) {
            ReportEmployeeOnApp report = new ReportEmployeeOnApp();

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
            report.setTuNhap((BigInteger) o[10]);
            report.setDuocGiao((BigInteger) o[11]);
            results.add(report);
        }

        return results;
    }


    @Transactional
    public List<ReportResponse> exportDataToExcelFile(Timestamp startTime, Timestamp endTime, Users user, String deptCode) throws IOException, InvalidFormatException {
        String query = "";
        Query nativeQuery = null;
        if (user.getRoles().equals("TCT")) {
            if (deptCode == null){
                query = "select e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a.home_no , a.fomataddress," +
                        "       l.title, l.phone, l.lead_source, s.created_date as schedu_create_date, r.created_date as result_craete_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                        "       r.proposal , la.created_date as la_createddate " +
                        " from lead l left join  lead_assign la on l.id = la.lead_id" +
                        "         left join users e on la.user_recipient_id = e.emp_system_id" +
                        "         left join post_office po on e.post_code = po.postcode" +
                        "         left join address a on l.address_id = a.id and a.deleted_status = 0" +
                        "         left join (" +
                        "    select s1.*" +
                        "    from (select * from schedule where result_id is not null) s1" +
                        "             LEFT JOIN (select * from schedule where result_id is not null) s2" +
                        "                       on (s1.lead_id = s2.lead_id and s1.created_date < s2.created_date)" +
                        "    WHERE s2.created_date is null" +
                        ") s on l.id = s.lead_id" +
                        "         left join (select * from result r3 where r3.created_date >= ?) r on s.result_id = r.id" +
                        "         left join address a2 on a2.id = r.address_id" +
                        " where la.deleted_status = 0 and la.created_date >= ? and  la.created_date <= ? " +
                        "group by (e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a.home_no , a.fomataddress," +
                        "       l.title, l.phone, l.lead_source, s.created_date, r.created_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                        "       r.proposal, la.created_date)";
            } else {
                query = "select e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a.home_no , a.fomataddress," +
                        "       l.title, l.phone, l.lead_source, s.created_date as schedu_create_date, r.created_date as result_craete_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                        "       r.proposal, la.created_date as la_createddate " +
                        " from lead l left join  lead_assign la on l.id = la.lead_id" +
                        "         left join users e on la.user_recipient_id = e.emp_system_id" +
                        "         left join post_office po on e.post_code = po.postcode" +
                        "         left join address a on l.address_id = a.id and a.deleted_status = 0" +
                        "         left join (" +
                        "    select s1.*" +
                        "    from (select * from schedule where result_id is not null) s1" +
                        "             LEFT JOIN (select * from schedule where result_id is not null) s2" +
                        "                       on (s1.lead_id = s2.lead_id and s1.created_date < s2.created_date)" +
                        "    WHERE s2.created_date is null" +
                        ") s on l.id = s.lead_id" +
                        "         left join (select * from result r3 where r3.created_date >= ?) r on s.result_id = r.id" +
                        "         left join address a2 on a2.id = r.address_id" +
                        " where la.deleted_status = 0 and la.created_date >= ? and  la.created_date <= ? and po.deptcode = ?" +
                        "group by (e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a.home_no , a.fomataddress," +
                        "       l.title, l.phone, l.lead_source, s.created_date, r.created_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                        "       r.proposal, la.created_date )";
            }


            nativeQuery = entityManager.createNativeQuery(query)
                    .setParameter(1, startTime)
                    .setParameter(2, startTime)
                    .setParameter(3, endTime);
            if (deptCode != null)
                nativeQuery.setParameter(4, deptCode);

        } else if (user.getRoles().equals("CN")) {
            String dept = user.getDeptCode();

            query = " select e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a2.home_no , a2.fomataddress," +
                    "       l.title, l.phone, l.lead_source, s.created_date as schedu_create_date, r.created_date as result_craete_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                    "       r.proposal, la.created_date as la_createddate " +
                    " from lead l left join ( select la1.* from lead_assign la1 LEFT JOIN lead_assign la2 on (la1.lead_id = la2.lead_id and la1.created_date < la2.created_date)" +
                    "    WHERE la2.created_date is null) la on l.id = la.lead_id" +
                    "         left join users e on la.user_recipient_id = e.emp_system_id" +
                    "         left join post_office po on e.post_code = po.postcode" +
                    "         left join address a on l.address_id = a.id and a.deleted_status = 0" +
                    "         left join industry_detail id on l.id = id.related_to_id" +
                    "         left join industry i on id.industry_id = i.id" +
                    "         left join (" +
                    "    select s1.*" +
                    "    from (select * from schedule where result_id is not null) s1" +
                    "             LEFT JOIN (select * from schedule where result_id is not null) s2" +
                    "                       on (s1.lead_id = s2.lead_id and s1.created_date < s2.created_date)" +
                    "    WHERE s2.created_date is null" +
                    " ) s on l.id = s.lead_id" +
                    "         left join (select * from result r3 where r3.created_date >= ?) r on s.result_id = r.id" +
                    "         left join address a2 on a2.id = r.address_id" +
                    " where la.deleted_status = 0 and la.created_date >= ? and  la.created_date <= ? and po.deptcode = ?  " +
                    " group by (e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a2.home_no , a2.fomataddress," +
                    "       l.title, l.phone, l.lead_source, s.created_date, r.created_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                    "       r.proposal, la.created_date ) ";

            nativeQuery = entityManager.createNativeQuery(query)
                    .setParameter(1, startTime)
                    .setParameter(2, startTime)
                    .setParameter(3, endTime)
                    .setParameter(4, dept);
        } else {
            String post = user.getPostCode();
            query = " select e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a2.home_no , a2.fomataddress," +
                    "       l.title, l.phone, l.lead_source, s.created_date as schedu_create_date, r.created_date as result_craete_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                    "       r.proposal, la.created_date as la_createddate " +
                    " from lead l left join ( select la1.* from lead_assign la1 LEFT JOIN lead_assign la2 on (la1.lead_id = la2.lead_id and la1.created_date < la2.created_date)" +
                    "    WHERE la2.created_date is null) la on l.id = la.lead_id" +
                    "         left join users e on la.user_recipient_id = e.emp_system_id" +
                    "         left join post_office po on e.post_code = po.postcode" +
                    "         left join address a on l.address_id = a.id and a.deleted_status = 0" +
                    "         left join industry_detail id on l.id = id.related_to_id" +
                    "         left join industry i on id.industry_id = i.id" +
                    "         left join (" +
                    "    select s1.*" +
                    "    from (select * from schedule where result_id is not null) s1" +
                    "             LEFT JOIN (select * from schedule where result_id is not null) s2" +
                    "                       on (s1.lead_id = s2.lead_id and s1.created_date < s2.created_date)" +
                    "    WHERE s2.created_date is null" +
                    " ) s on l.id = s.lead_id" +
                    "         left join (select * from result r3 where r3.created_date >= ?) r on s.result_id = r.id" +
                    "         left join address a2 on a2.id = r.address_id" +
                    " where la.deleted_status = 0 and la.created_date >= ? and  la.created_date <= ? and po.postcode = ?" +
                    " group by (e.employee_code, po.postcode, po.deptcode, l.customer_code, l.full_name, l.representation, a2.home_no , a2.fomataddress," +
                    "       l.title, l.phone, l.lead_source, s.created_date, r.created_date, r.status, r.discount, r.in_province_price, r.out_province_price," +
                    "       r.proposal, la.created_date )";

            nativeQuery = entityManager.createNativeQuery(query)
                    .setParameter(1, startTime)
                    .setParameter(2, startTime)
                    .setParameter(3, endTime)
                    .setParameter(4, post);
        }

        List<Object[]> rs = nativeQuery.getResultList();
        log.debug("get size :" + rs.size());
        List<ReportResponse> listData = new ArrayList<>();
        for (Object[] model : rs) {
            ReportResponse reportResponse = ReportResponse.leadModelToDto(model);
            listData.add(reportResponse);
        }
        return listData;

    }
}
