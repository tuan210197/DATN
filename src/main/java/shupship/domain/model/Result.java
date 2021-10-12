package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * The persistent class for the reminder database table.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "result")
@Where(clause = "deleted_status=0")
@Data
public class Result extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "result")
    private String result;

    @Column(name = "reason")
    private Long reason;

    @Column(name = "proposal")
    private String proposal;

    @Column(name = "description")
    private String description;

    @Column(name = "policy")
    private String policy;

    @Column(name = "status")
    private Long status;

    @Column(name = "district_percent")
    private Double districtPercent;

    @Column(name = "in_province_percent")
    private Double inProvincePercent;

    @Column(name = "refund_percent")
    private Double refundPercent;

    @Column(name = "customer_code")
    private String customerCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;


    @OneToOne(mappedBy = "result")
    private Schedule schedule;

    @Column(name = "discount")
    private Double discount;

}
