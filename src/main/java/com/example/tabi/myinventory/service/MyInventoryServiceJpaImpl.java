package com.example.tabi.myinventory.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.myinventory.entity.MyInventory;
import com.example.tabi.myinventory.repository.MyInventoryRepository;
import com.example.tabi.myinventory.vo.MyInventoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyInventoryServiceJpaImpl implements MyInventoryService {
    private final MyInventoryRepository myInventoryRepository;
    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public void createMyInventory(AppUser appUser) {
        MyInventory myInventory = new MyInventory();
        myInventory.setAppUser(appUser);
        myInventory.setCoins(0);
        myInventory.setUniqueCreditCard(0);
        myInventory.setNormalCreditCard(0);

        myInventoryRepository.save(myInventory);
    }

    @Override
    public MyInventoryDto getMyInventory(Authentication authentication) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        return myInventoryToMyInventoryDto(appUser.getMyInventory());
    }

    public static MyInventoryDto myInventoryToMyInventoryDto(MyInventory myInventory) {
        return new MyInventoryDto(
                myInventory.getMyInventoryId(),
                myInventory.getCoins(),
                myInventory.getUniqueCreditCard(),
                myInventory.getNormalCreditCard()
        );
    }
}
