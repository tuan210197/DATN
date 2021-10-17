package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Table(name = "lead_assign_his")
@NamedQuery(name = "LeadAssignHis.findAll", query = "SELECT l FROM LeadAssignHis l")
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted_status=0")
@Data
@Embeddable
public class LeadAssignHis extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    private Long total;

    @Column(name = "total_valid")
    private Long totalValid;

    @Column(name = "total_invalid")
    private Long totalInvalid;

    private Long status;

    private String note;

    @OneToMany(mappedBy = "his", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<LeadAssignExcel> leadAssignExcels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "emp_system_id", nullable = true, insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Users fileCreator;
}
