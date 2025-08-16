package com.example.tabi.myinventory.entity;

import com.example.tabi.appuser.entity.AppUser;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MyInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myInventoryId;

    @Column(nullable = false)
    private Integer coins;

    @Column(nullable = false)
    private Integer uniqueCreditCard;

    @Column(nullable = false)
    private Integer normalCreditCard;

    // appUser와 1:1 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}
