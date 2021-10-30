package shupship.domain.model;


import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;


@Data
@Table(name = "dept_office")
@Entity
public class DeptOffice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deptCode;

    private String deptName;

    private String deptPhone;

    @OneToMany(mappedBy = "deptOffice")
    Collection<PostOffice> postOffice;

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
