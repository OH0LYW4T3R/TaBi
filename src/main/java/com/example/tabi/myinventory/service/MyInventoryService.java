package com.example.tabi.myinventory.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.myinventory.vo.MyInventoryDto;
import org.springframework.security.core.Authentication;

public interface MyInventoryService {
    void createMyInventory(AppUser appUser);
    MyInventoryDto getMyInventory(Authentication authentication);
}
