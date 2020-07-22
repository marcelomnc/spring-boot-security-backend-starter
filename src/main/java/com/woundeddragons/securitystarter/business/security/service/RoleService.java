package com.woundeddragons.securitystarter.business.security.service;

import com.woundeddragons.securitystarter.business.security.model.Role;
import com.woundeddragons.securitystarter.business.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findById(int id) {
        return this.roleRepository.findById(id);
    }
}
