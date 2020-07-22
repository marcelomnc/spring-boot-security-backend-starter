package com.woundeddragons.securitystarter.business.security.service;

import com.woundeddragons.securitystarter.business.security.model.RoleByUser;
import com.woundeddragons.securitystarter.business.security.repository.RoleByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleByUserService {
    @Autowired
    private RoleByUserRepository roleByUserRepository;

    public List<RoleByUser> findByUserId(int userId) {
        return this.roleByUserRepository.findByUserId(userId);
    }
}
