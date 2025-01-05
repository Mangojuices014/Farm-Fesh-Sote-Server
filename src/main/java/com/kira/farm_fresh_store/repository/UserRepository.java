package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean findByUserName(String userName);

    String getByUserName(String userName);

    String getByEmail(@Email @NotNull String email);
}
