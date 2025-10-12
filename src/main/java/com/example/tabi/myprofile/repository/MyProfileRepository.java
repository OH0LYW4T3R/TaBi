package com.example.tabi.myprofile.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.myprofile.entity.MyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyProfileRepository extends JpaRepository<MyProfile, Long> {
    boolean existsByNickName(String nickname);
    boolean existsByAppUser(AppUser appUser);

    Optional<MyProfile> findByAppUser(AppUser appUser);
    Optional<MyProfile> findByNickName(String nickname);

    List<MyProfile> findTop30ByNickNameStartingWithIgnoreCaseOrderByNickNameAsc(String prefix);
    List<MyProfile> findTop30ByNickNameStartingWithIgnoreCaseAndAppUserNotOrderByNickNameAsc(String nickNamePrefix, AppUser appUser);
    List<MyProfile> findTop30ByNickNameContainingIgnoreCaseOrderByNickNameAsc(String keyword);
}
