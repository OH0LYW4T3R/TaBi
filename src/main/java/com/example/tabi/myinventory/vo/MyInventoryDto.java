package com.example.tabi.myinventory.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyInventoryDto {
    private Long myInventoryId;
    private Integer coins;
    private Integer uniqueCreditCard;
    private Integer normalCreditCard;
}
