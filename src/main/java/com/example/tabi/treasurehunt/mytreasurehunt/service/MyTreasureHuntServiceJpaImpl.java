package com.example.tabi.treasurehunt.mytreasurehunt.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.PostStatus;
import com.example.tabi.treasurehunt.mytreasurehunt.entity.MyTreasureHunt;
import com.example.tabi.treasurehunt.mytreasurehunt.repository.MyTreasureHuntRepository;
import com.example.tabi.treasurehunt.mytreasurehunt.vo.MyTreasureHuntDto;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyTreasureHuntServiceJpaImpl implements MyTreasureHuntService {
    private final MyTreasureHuntRepository myTreasureHuntRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

    public void createMyTreasureHunt(AppUser appUser, TreasureHuntPost treasureHuntPost) {
        MyTreasureHunt myTreasureHunt = new MyTreasureHunt();
        myTreasureHunt.setAppUser(appUser);
        myTreasureHunt.setTreasureHuntPost(treasureHuntPost);
        myTreasureHunt.setStatus(PostStatus.CREATED);

        myTreasureHuntRepository.save(myTreasureHunt);

    }

    public static MyTreasureHuntDto myTreasureHuntToMyTreasureHuntDto(MyTreasureHunt myTreasureHunt) {
        return new MyTreasureHuntDto(myTreasureHunt.getMyTreasureHuntId(), null, myTreasureHunt.getStatus(), myTreasureHunt.getCreatedAt());
    }
}
