package shupship.domain.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import shupship.controller.BaseController;
import shupship.helper.InstantConverter;
import shupship.service.UserService;
import shupship.service.impl.UserServiceImpl;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass
public abstract class AuditEntity extends BaseController implements Serializable{
    @Column(name = "created_date")
//    @Convert(converter = InstantConverter.class)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
//    @Convert(converter = InstantConverter.class)
    private LocalDateTime lastModifiedDate;

    @Column(name = "created_by", nullable = true)
    private Long createdBy;

    @Column(name = "last_modified_by", nullable = true)
    private Long lastModifiedBy;

    @Column(name = "deleted_status", nullable = true)
    private Long deletedStatus;

    @Column(name = "deleted_by", nullable = true)
    private Long deletedBy;

    @Column(name = "deleted_date", nullable = true)
//    @Convert(converter = InstantConverter.class)
    private LocalDateTime deletedDate;

    public AuditEntity() {
    }

    public AuditEntity(LocalDateTime createdDate, LocalDateTime lastModifiedDate, Long createdBy, Long lastModifiedBy) {
        super();
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(Long deletedStatus) {
        this.deletedStatus = deletedStatus;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }


    @PrePersist
    void prePersit() {
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        this.createdDate = LocalDateTime.ofInstant(Instant.now(), zone);
//        try {
//            if (createdBy == null) {
//                this.createdBy = getCurrentUser().getEmpSystemId();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//                this.createdBy = -1L;
//            }
        this.deletedStatus = 0L;
        this.lastModifiedDate = createdDate;
        this.lastModifiedBy = createdBy;
    }

    @PreUpdate
    void preUpdate() throws Exception {
//        if (SecurityContextHolder.getContext() !=null && SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
//         this.lastModifiedBy = getCurrentUser().getEmpSystemId();
//            this.lastModifiedBy = -1L;
//        }else{
//            this.lastModifiedBy = -1L;
//        }
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        this.lastModifiedDate = LocalDateTime.ofInstant(Instant.now(), zone);
    }


}