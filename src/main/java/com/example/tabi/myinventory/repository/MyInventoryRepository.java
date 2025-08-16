package com.example.tabi.myinventory.repository;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.myinventory.entity.MyInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyInventoryRepository extends JpaRepository<MyInventory,Long> {
}
