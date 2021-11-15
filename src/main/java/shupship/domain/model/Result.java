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
@NamedQuery(name = "Result.findAll", query = "SELECT a FROM Result a")
@Table(name = "result")
@Data
public class Result extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "result")
    private String result;

    @Column(name = "reason")
    private String reason;

    @Column(name = "proposal")    // đề xuất
    private String proposal;

    @Column(name = "district_percent")
    private Double districtPercent;

    @Column(name = "status")
    private Long status;

    private Long type;

    @Column(name = "in_province_price")  // giá nội tỉnh
    private Double inProvincePrice;

    @Column(name = "out_province_price")  // giá ngoại tỉnh
    private Double outProvincePrice;

    @Column(name = "refund_percent")  // phần trăm hoàn lại
    private Double refundPercent;

    @Column(name = "customer_code")
    private String customerCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "discount")      // chiếu khấu
    private Double discount;

    @OneToOne(mappedBy = "result")
    private Schedule schedule;

}
