package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.user.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
}
