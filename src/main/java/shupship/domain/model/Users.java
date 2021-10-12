package shupship.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NamedQuery(name = "Users.findAll", query = "SELECT l FROM Users l")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Users extends BaseObject {

    @Id
    @Column(name="user_uid")
    private String userUid;

    private String password;

    private String salutation;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    private Long type;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "manager_id")
    private Long manager_id;

    private String mobile;

    private String email;

    private String deparment;

    private String title;

    private String profileId;

    private String roleId;

    private String contactId;

    private String description;

    private String status;

    @Transient
    private String postCodeStr;

    @Transient
    private String roleCodeStr;
    @Column(name = "token_code")
    private String tokenCode;
    @Column(name = "expire_date")
    private LocalDateTime expireDate;
    @Column(name = "is_verified")
    private Integer isVerified;
    @Column(name = "retry_count")
    private Integer retryCount;




}
