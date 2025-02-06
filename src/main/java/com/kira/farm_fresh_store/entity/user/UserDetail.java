package com.kira.farm_fresh_store.entity.user;

import com.kira.farm_fresh_store.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "detail_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "company", length = 100)
    private String company;

}
