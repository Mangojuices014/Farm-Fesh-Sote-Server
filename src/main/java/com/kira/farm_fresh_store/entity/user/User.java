package com.kira.farm_fresh_store.entity.user;

import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.Photo;
import com.kira.farm_fresh_store.utils.enums.ETypeAccount;
import com.kira.farm_fresh_store.utils.enums.ETypeUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profileId;

    private String userId;

    @Email
    @NotNull
    private String email;

    private String userName;

    private String password;

    private String lastName;

    private String firstName;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private ETypeUser typeUser;;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Photo photo;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private Address address = new Address();

    @Enumerated(EnumType.STRING)
    private ETypeAccount type_account;

}
