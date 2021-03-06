package com.woundeddragons.securitystarter.business.repository;

import com.woundeddragons.securitystarter.business.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNmId(Integer nmId);

    Optional<User> findByDsEmail(String dsEmail);

    boolean existsByEmail(String email, Integer userId);
}
