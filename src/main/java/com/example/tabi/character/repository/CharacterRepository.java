package com.example.tabi.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.tabi.character.entity.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    boolean existsByCharacterURL(String characterURL);
    Character findByCharacterNameAndRank(String characterName, Integer rank);
}
