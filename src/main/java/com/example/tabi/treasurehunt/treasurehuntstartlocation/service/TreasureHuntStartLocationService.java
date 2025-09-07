package com.example.tabi.treasurehunt.treasurehuntstartlocation.service;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntStartLocation;

public interface TreasureHuntStartLocationService {
    TreasureHuntStartLocation createTreasureHuntStartLocation(TreasureHuntPost treasureHuntPost, double latitude, double longitude, double altitude);
    String getAddressUsingGoogleMap(double lat, double lng);
    String getIndicatingAddress(String address);
}
