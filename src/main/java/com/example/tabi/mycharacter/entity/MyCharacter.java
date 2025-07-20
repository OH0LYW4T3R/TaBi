package com.example.tabi.mycharacter.entity;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.character.entity.Character;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class MyCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myCharacterId;

    @OneToMany
    @JoinColumn(name = "my_character_id") // Character 테이블에 외래키
    private Set<Character> myCharacters;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}
