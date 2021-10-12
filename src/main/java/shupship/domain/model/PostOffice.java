package shupship.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "post_office")
public class PostOffice extends AuditEntity {

    @Id
    @Column(columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postCode;

    private String postName;

    private String postPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_office_id")
    private DeptOffice deptOffice;

    //kinh do
    @Column(name = "latitude")
    private Double lat;

    // vi do
    @Column(name = "longitude")
    private Double lng;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "district_code")
    private String districtCode;

    @Column(name = "ward_code")
    private String wardCode;

}
