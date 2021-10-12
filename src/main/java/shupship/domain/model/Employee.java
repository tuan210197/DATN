package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
@EqualsAndHashCode(callSuper = true)
@Data
public class Employee extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "emp_system_id")
    private Long empSystemId;

    private String status;

    private String email;

    private String username;

    private Long active;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @OneToMany(mappedBy = "employees")
    Collection<LeadAssign> leadAssigns;

    @OneToMany(mappedBy = "employee")
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "fileCreator")
    private Collection<LeadAssignHis> leadsAssignHis;

}
