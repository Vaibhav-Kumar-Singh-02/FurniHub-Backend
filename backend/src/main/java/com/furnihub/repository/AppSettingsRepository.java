package com.furnihub.repository;

import com.furnihub.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {
    Optional<AppSettings> findFirstByOrderByIdAsc();
}
