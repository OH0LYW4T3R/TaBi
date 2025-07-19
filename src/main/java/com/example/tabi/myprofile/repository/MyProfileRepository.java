package com.example.tabi.myprofile.repository;

import com.example.tabi.myprofile.entity.MyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyProfileRepository extends JpaRepository<MyProfile, Long> {
}
