package com.woundeddragons.securitystarter.business.repository;

import com.woundeddragons.securitystarter.business.model.RoleByUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleByUserRepository extends JpaRepository<RoleByUser, Integer> {
    List<RoleByUser> findByUserId(int userId);
}
