package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.models.Role;
import com.ntndev.ecommercespringboot.repositories.RoleRepository;
import com.ntndev.ecommercespringboot.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
