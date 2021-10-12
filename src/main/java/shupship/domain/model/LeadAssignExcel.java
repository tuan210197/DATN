package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lead_assign_excel")
@NamedQuery(name = "LeadAssignExcel.findAll", query = "SELECT l FROM LeadAssignExcel l")
//@Where(clause = "deleted_status=0")
@Data
public class LeadAssignExcel extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "full_name")
    private String fullName;

    private String representation;

    private String title;

    private String phone;

    @Column(name = "lead_source")
    private String leadSource;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "post_code")
    private String postCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    private Long status;

    private String note;

    private String error;

    @Column(name = "emp_system_id")
    private Long empSystemId;

    @Column(name = "employee_code")
    private String employeeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private LeadAssignHis his;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lead_assign_id", referencedColumnName = "id")
    private LeadAssign leadAssign;

}
