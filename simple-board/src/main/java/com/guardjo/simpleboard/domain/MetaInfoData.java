package com.guardjo.simpleboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class MetaInfoData {
    @CreatedBy
    @Column(nullable = false, updatable = false)
    @Setter
    protected String creator;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    protected LocalDateTime createTime;

    @LastModifiedBy
    @Column(nullable = false)
    protected String modifier;

    @LastModifiedDate
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    protected LocalDateTime modifiedTime;
}
