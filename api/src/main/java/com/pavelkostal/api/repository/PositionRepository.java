package com.pavelkostal.api.repository;

import com.pavelkostal.api.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT city FROM Position ")
    List<String> getAllCity();
}
