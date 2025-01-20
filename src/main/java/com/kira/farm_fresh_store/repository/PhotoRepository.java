package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, String> {
}
