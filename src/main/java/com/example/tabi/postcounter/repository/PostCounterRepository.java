package com.example.tabi.postcounter.repository;

import com.example.tabi.postcounter.entity.PostCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCounterRepository extends JpaRepository<PostCounter, Long> {
}
