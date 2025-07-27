package com.example.tabi.treasurehunt.treasurehuntstartlocation.service;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpostImage.service.TreasureHuntPostImageService;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntLocation;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.repository.TreasureHuntLocationRepository;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.vo.TreasureHuntLocationDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TreasureHuntLocationServiceJpaImpl implements TreasureHuntLocationService {
    private final TreasureHuntLocationRepository treasureHuntLocationRepository;

    @Override
    @Transactional
    public TreasureHuntLocation createTreasureHuntLocation(TreasureHuntPost treasureHuntPost, double latitude, double longitude, double altitude) {
        TreasureHuntLocation treasureHuntLocation = new TreasureHuntLocation();
        String address = getAddressUsingGoogleMap(latitude, longitude);
        treasureHuntLocation.setActualLocation(address);
        treasureHuntLocation.setIndicateLocation(getIndicatingAddress(address));
        treasureHuntLocation.setLatitude(latitude);
        treasureHuntLocation.setLongitude(longitude);
        treasureHuntLocation.setAltitude(altitude);
        treasureHuntLocation.setTreasureHuntPost(treasureHuntPost);
        treasureHuntLocationRepository.save(treasureHuntLocation);

        return treasureHuntLocation;
    }

    public String getAddressUsingGoogleMap(double lat, double lng) {
        return "대전광역시 유성구 덕명동";
    }

    public String getIndicatingAddress(String address) {
        return "대전광역시";
    }


    public static TreasureHuntLocationDto treasureHuntLocationToTreasureHuntLocationDto(TreasureHuntLocation entity) {
        if (entity == null) return null;

        return new TreasureHuntLocationDto(
                entity.getTreasureHuntLocationId(),
                entity.getIndicateLocation(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getAltitude()
        );
    }
}
