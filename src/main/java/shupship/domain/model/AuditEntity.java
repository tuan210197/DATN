package shupship.domain.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import shupship.helper.InstantConverter;
import shupship.service.UserService;
import shupship.service.impl.UserServiceImpl;
import shupship.util.CommonUtils;
import shupship.util.exception.ApplicationException;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class AuditEntity implements Serializable{
    @Column(name = "created_date")
    @Convert(converter = InstantConverter.class)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    @Convert(converter = InstantConverter.class)
    private Instant lastModifiedDate;

    @Column(name = "created_by", nullable = true)
    private Long createdBy;

    @Column(name = "last_modified_by", nullable = true)
    private Long lastModifiedBy;

    @Column(name = "deleted_status", nullable = true)
    private Long deletedStatus;

    @Column(name = "deleted_by", nullable = true)
    private Long deletedBy;

    @Column(name = "deleted_date", nullable = true)
    @Convert(converter = InstantConverter.class)
    private Instant deletedDate;

    public AuditEntity() {
    }

    public AuditEntity(Instant createdDate, Instant lastModifiedDate, Long createdBy, Long lastModifiedBy) {
        super();
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    @PrePersist
    void prePersit() {
        this.createdDate = Instant.now();
        try {
            if (createdBy == null) {
                this.createdBy = -1L;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.createdBy = -1L;
        }
        this.deletedStatus = 0L;
        this.lastModifiedDate = createdDate;
        this.lastModifiedBy = createdBy;
    }

    @PreUpdate
    void preUpdate() throws ApplicationException {
        if (SecurityContextHolder.getContext() !=null && SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
//            this.lastModifiedBy = CommonUtils.getCurrentUser().getEmpSystemId();
            this.lastModifiedBy = -1L;
        }else{
            this.lastModifiedBy = -1L;
        }

        this.lastModifiedDate = Instant.now();
    }
}