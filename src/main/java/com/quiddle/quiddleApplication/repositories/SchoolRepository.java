package com.quiddle.quiddleApplication.repositories;

import com.quiddle.quiddleApplication.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByName(String schoolName);
}
