package shupship.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Table(name = "lock_employee")
@Getter
@Setter
public class LockEmployee extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_system_id")
    private Long employeeId;

    @Column(name = "employee_code")
    private String employeeCode;

    private String phone;

    private String username;

    @Column(name = "lockdate")
    private LocalDateTime lockdate;

    private String lockreason;

    private String unlockreason;

    private String executor;

    private String activity;

    private Long isLatest;

    @PrePersist
    void prePersist(){
        isLatest = 1L;
    }

}
