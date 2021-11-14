package shupship.domain.model;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The persistent class for the reminder database table.
 */
@Entity
@Table(name = "schedule")
@Where(clause = "deleted_status=0")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class Schedule extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_time")
    private LocalDateTime fromDate;

    @Column(name = "to_time")
    private LocalDateTime toDate;

    @Column(name = "description")
    private String description;

    private Long status;

    @Column(name = "is_latest")
    private Integer isLatest;

    @Column(name = "is_latest_result")
    private Integer isLatestResult;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "result_id", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Lead lead;

    @Column(name = "next_schedule")
    private Long nextScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "emp_system_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Users user;


}
