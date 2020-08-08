package com.woundeddragons.securitystarter.business.service;

import com.woundeddragons.securitystarter.business.common.SecurityRole;
import com.woundeddragons.securitystarter.business.model.Role;
import com.woundeddragons.securitystarter.business.model.RoleByUser;
import com.woundeddragons.securitystarter.business.model.User;
import com.woundeddragons.securitystarter.business.repository.RoleByUserRepository;
import com.woundeddragons.securitystarter.business.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleByUserRepository roleByUserRepository;

    @Transactional
    public User signUp(User user) {
        User inserted = this.userRepository.save(user);

        //Logic to assign the user role
        RoleByUser roleByUser = new RoleByUser();
        Role role = new Role();
        role.setNmId(SecurityRole.ROLE_USER.getId());
        roleByUser.setNmRoleId(role);
        roleByUser.setNmUserId(inserted);
        List<RoleByUser> rolesByUser = new ArrayList<>();
        rolesByUser.add(roleByUser);
        inserted.setRolesByUserCollection(rolesByUser);

        this.roleByUserRepository.save(roleByUser);

        return inserted;
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public Optional<User> findById(Integer userId) {
        return this.userRepository.findByNmId(userId);
    }

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByDsEmail(email);
    }

    public boolean existsByEmail(String email, Integer userId) {
        return this.userRepository.existsByEmail(email, userId);
    }
}
