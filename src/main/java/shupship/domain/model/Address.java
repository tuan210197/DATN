package shupship.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the address database table.
 */
@Entity
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
@EqualsAndHashCode(callSuper = true)
@Data
public class Address extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "home_no")
    private String homeNo;

    private String street;

    private String ward;

    private String district;

    private String province;

    private String country;

    private String description;

    @Column(name = "object_name")
    private String objectName;

    private BigDecimal lat;

    private BigDecimal lng;

    private Long status;

}