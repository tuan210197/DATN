package shupship.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the lead database table.
 */
@Entity
@Data
@Table(name = "ward")
@NoArgsConstructor
@AllArgsConstructor
public class Ward extends AuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ward_id")
    private String wardId;

    private String wardCode;

    private String wardName;

    private String provinceCode;

    private String districtCode;

    @Column(name = "format_address")
    private String formattedAddress;

    //kinh do
    @Column(name = "latitude")
    private Double lat;

    // vi do
    @Column(name = "longitude")
    private Double lng;

}
