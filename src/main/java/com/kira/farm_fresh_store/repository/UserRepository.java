package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional<User> findByUserId(String userId);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findUserByEmail(String email);
}
