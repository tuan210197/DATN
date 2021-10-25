package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Data
public class Lead extends AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salutation;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    private String phone;

    private String gender;

    private String description;

    private Long type;

    @Column(name = "quantity_month")
    private Double quantityMonth;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "quality")
    private String quality;

    @Column(name = "in_province_price")
    private Double inProvincePrice;

    @Column(name = "out_province_price")
    private Double outProvincePrice;

    @Column(name = "compensation")
    private String compensation;

    @Column(name = "payment")
    private String payment;

    @Column(name = "other")
    private String other;

    @Column(name = "customer_code")
    private String customerCode;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Schedule> schedules;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "num_of_emp")
    private Long numOfEmp;

    @Column(name = "annual_quantity")
    private Long annualQuantity;

    @Column(name = "lead_score")
    private Long leadScore;

    @Column(name = "lead_source")
    private String leadSource;

    @Column(name = "convert_status")
    private Long convertStatus;

    @Column(name = "status")
    private Long status;

    @Column(name = "representation")
    private String representation;

    @Column(name = "title")
    private String title;

    @Column(name = "expected_revenue")
    private Double expectedRevenue;

    @Column(name = "is_from_evtp")
    private Long isFromEVTP;

    @OneToMany(mappedBy = "leads")
    Collection<LeadAssign> leadAssigns;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "industry_detail",
            joinColumns = @JoinColumn(name = "related_to_id"),
            inverseJoinColumns = @JoinColumn(name = "industry_id")
    )
    private Collection<Industry> industries;

}
