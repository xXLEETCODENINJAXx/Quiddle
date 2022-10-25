package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> getRole(ERole roleEnum) {
        String roleName = roleEnum.name();

        return roleRepository.findByName(roleName);
    }

    @Override
    public Optional<Role> getRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Optional<Role> getDefaultRole()  {
        return getRole(ERole.USER);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
