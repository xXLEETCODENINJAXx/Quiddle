package com.quiddle.quiddleApplication.repositories;

import com.quiddle.quiddleApplication.models.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserClassRepository extends JpaRepository<UserClass, Long> {
    Optional<UserClass> findByStudentIdAndClassId(Long studentId, Long classId);
}
