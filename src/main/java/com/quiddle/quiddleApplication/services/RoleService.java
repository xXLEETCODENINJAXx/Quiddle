package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoleService {

     Role create(Role role);

     Optional<Role> getRoleById(Long roleId) throws ApplicationException;

    Optional<Role> getRole(ERole admin) throws ApplicationException;

    Optional<Role> getDefaultRole() throws ApplicationException;

    List<Role> getAllRoles();
}
