package shupship.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="basic_login")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BasicLogin {
    @Id
    @Column(name="user_uid")
    private String userUid;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="token_code")
    private String tokenCode;
    @Column(name="expire_date")
    private LocalDateTime expireDate;
    @Column(name="is_verified")
    private Integer isVerified;
    @Column(name="retry_count")
    private Integer retryCount;


}
