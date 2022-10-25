package com.quiddle.quiddleApplication.repositories;

import com.quiddle.quiddleApplication.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
