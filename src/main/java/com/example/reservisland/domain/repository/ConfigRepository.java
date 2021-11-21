package com.example.reservisland.domain.repository;

import com.example.reservisland.domain.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Config findByKey(String key);
}
