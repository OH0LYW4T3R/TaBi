package com.example.tabi.treasurehunt.treasurehuntstartlocation.service;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntLocation;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.vo.TreasureHuntLocationDto;

public interface TreasureHuntLocationService {
    TreasureHuntLocation createTreasureHuntLocation(TreasureHuntPost treasureHuntPost, double latitude, double longitude, double altitude);
    String getAddressUsingGoogleMap(double lat, double lng);
    String getIndicatingAddress(String address);
}
