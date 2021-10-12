package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Table(name = "lead_assign")
@NamedQuery(name = "LeadAssign.findAll", query = "SELECT l FROM LeadAssign l")
@EqualsAndHashCode(callSuper = true)
//@Where(clause = "deleted_status=0")
@Data
@Embeddable
public class LeadAssign extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "user_assignee_id")
    private Long userAssigneeId;

    @Column(name = "user_recipient_id")
    private Long userRecipientId;

    @Column(name = "recall_status")
    private Long recallStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead leads;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_recipient_id", referencedColumnName = "emp_system_id", nullable = true, updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Employee employees;

    private String note;

    private Long status;

    @Column(name = "file_id")
    private Long fileId;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "leadAssign")
    private LeadAssignExcel leadAssignExcel;

    //For check logic and save error
    @Transient
    private String errorMsg;
}
