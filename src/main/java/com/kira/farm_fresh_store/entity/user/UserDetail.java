package com.kira.farm_fresh_store.entity.user;

import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.utils.enums.EGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "php_user_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String phoneNumber;

    private String cccd;

}
