package com.flynest.user_service.repository;

import com.flynest.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User,Long> {
}
