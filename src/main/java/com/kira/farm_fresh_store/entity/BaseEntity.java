package com.kira.farm_fresh_store.entity;

import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // Kích hoạt Auditing
@Getter
@Setter
public abstract class BaseEntity {
    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate; // Sử dụng LocalDateTime thay vì String

    @Column(name = "updated_date")
    @LastModifiedDate // Sử dụng @LastModifiedDate thay vì @CreatedDate
    private LocalDateTime updatedDate; // Sử dụng LocalDateTime thay vì String
}