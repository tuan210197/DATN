package shupship.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dept_office")
@Entity
public class DeptOffice {
    @Id
    @Column(columnDefinition = "serial")
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
