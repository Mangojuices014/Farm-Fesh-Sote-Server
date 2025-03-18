package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
