package com.kira.farm_fresh_store.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.Date;
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "created_date")
    @CreatedDate
    private String createdDate = new Timestamp(new Date().getTime()).toString();

    @Column(name = "updated_date")
    @CreatedDate
    private String updatedDate = new Timestamp(new Date().getTime()).toString();
}