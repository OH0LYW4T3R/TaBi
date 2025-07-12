package com.example.tabi.appuser.repository;

import com.example.tabi.appuser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByAppUserId(Long appUserId);
}
