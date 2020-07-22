package com.woundeddragons.securitystarter.business.security.repository;

import com.woundeddragons.securitystarter.business.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(int id);
}