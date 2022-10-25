package com.quiddle.quiddleApplication.repositories;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    @Query("SELECT c FROM Class c WHERE c.school = :school")
    List<Class> findAllBySchool(@Param("school") School school);
}
