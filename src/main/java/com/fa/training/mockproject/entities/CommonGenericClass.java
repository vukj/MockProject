package com.fa.training.mockproject.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonGenericClass implements Serializable {

    private static final long serialVersionUID = 1l;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified;

    @LastModifiedBy
    private String modifiedBy;
}
