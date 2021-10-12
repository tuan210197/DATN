package shupship.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Data
@Table(name = "province")
public class Province extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "province_id")
    private Long provinceId;

    private String provinceCode;

    private String provinceName;

    @Column(name = "format_address")
    private String formattedAddress;

    //kinh do
    @Column(name = "latitude")
    private Double lat;

    // vi do
    @Column(name = "longitude")
    private Double lng;

//    @OneToMany(mappedBy = "provinces")
//    Collection<District> districts;


}
