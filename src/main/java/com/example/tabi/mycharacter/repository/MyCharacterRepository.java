package com.example.tabi.mycharacter.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.mycharacter.entity.MyCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCharacterRepository extends JpaRepository<MyCharacter, Long> {
    MyCharacter findByAppUser(AppUser appUser);
    boolean existsByAppUser(AppUser appUser);
}
