package shupship.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shupship.common.Const;
import shupship.domain.dto.UserInfoDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@SqlResultSetMapping(
        name = Const.ResultSetMapping.USER_INFO_DTO,
        classes = {
                @ConstructorResult(
                        targetClass = UserInfoDTO.class,
                        columns = {
                                @ColumnResult(name = "uid", type = String.class),
                                @ColumnResult(name = "isActive", type = Integer.class),
                                @ColumnResult(name = "isDeleted", type = Integer.class),
                                @ColumnResult(name = "email", type = String.class),
                                @ColumnResult(name = "avatar", type = String.class),
                                @ColumnResult(name = "birthday", type = LocalDate.class),
                                @ColumnResult(name = "fullName", type = String.class),
                                @ColumnResult(name = "gender", type = Integer.class),
                                @ColumnResult(name = "mobile", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                        }
                )
        }
)

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@Builder
public class Users extends AuditEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uid", columnDefinition = "CHAR(128) NOT NULL")
    private String uid;
    @Column(name = "name")
    private String name;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "is_deleted")
    private Integer isDeleted;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "mobile")
    private String mobile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "emp_system_id")
    private Long empSystemId;

    @Column(name = "status_update")
    private Integer status_update;



    private Boolean enabled;

    @OneToMany(mappedBy = "users")
    Collection<LeadAssign> leadAssigns;

    @OneToMany(mappedBy = "user")
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "fileCreator")
    private Collection<LeadAssignHis> leadsAssignHis;

    //    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
    @Column(name = "roles")
    private String roles;
//    = new HashSet<>();

//    public <E> Users(String email, String password, ArrayList<E> es) {
//    }

//    public void addRole(Role role) {
//        roles.add(role);
//        role.getUsers().add(this);
//    }

//    public void removeRole(Role role) {
//        roles.remove(role);
//        role.getUsers().remove(this);
//    }

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;
//    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
