package com.example.tabi.quest.actions.hint.repository;

import com.example.tabi.quest.actions.hint.entity.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HintRepository extends JpaRepository<Hint,Long> {
}
