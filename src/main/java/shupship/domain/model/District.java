package shupship.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Data
@Table(name = "district")
public class District extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "district_id")
    private Long districtId;

    private String districtCode;

    private String districtName;

    private String provinceCode;

    @Column(name = "format_address")
    private String formattedAddress;

    //kinh do
    @Column(name = "latitude")
    private Double lat;

    // vi do
    @Column(name = "longitude")
    private Double lng;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "province_id")
//    private Province provinces;

}
