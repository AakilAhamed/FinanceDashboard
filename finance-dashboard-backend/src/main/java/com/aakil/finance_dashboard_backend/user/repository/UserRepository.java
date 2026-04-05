package com.aakil.finance_dashboard_backend.user.repository;

import com.aakil.finance_dashboard_backend.user.entity.User;
import com.aakil.finance_dashboard_backend.user.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    long countByStatus(UserStatus status);
}