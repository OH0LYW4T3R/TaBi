package com.example.tabi.treasurehunt.treasurehuntstartlocation.service;

import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.entity.TreasureHuntStartLocation;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.repository.TreasureHuntStartLocationRepository;
import com.example.tabi.treasurehunt.treasurehuntstartlocation.vo.TreasureHuntStartLocationDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreasureHuntStartLocationServiceJpaImpl implements TreasureHuntStartLocationService {
    private final TreasureHuntStartLocationRepository treasureHuntStartLocationRepository;
    private final RestTemplate restTemplate;
    @Value("${google.maps.api.key}")
    private String GOOGLE_MAP_API_KEY;
    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    @Override
    @Transactional
    public TreasureHuntStartLocation createTreasureHuntStartLocation(TreasureHuntPost treasureHuntPost, double latitude, double longitude, double altitude) {
        TreasureHuntStartLocation treasureHuntStartLocation = new TreasureHuntStartLocation();
        String address = getAddressUsingGoogleMap(latitude, longitude);
        treasureHuntStartLocation.setActualLocation(address);
        treasureHuntStartLocation.setIndicateLocation(getIndicatingAddress(address));
        treasureHuntStartLocation.setLatitude(latitude);
        treasureHuntStartLocation.setLongitude(longitude);
        treasureHuntStartLocation.setAltitude(altitude);
        treasureHuntStartLocation.setTreasureHuntPost(treasureHuntPost);
        treasureHuntStartLocationRepository.save(treasureHuntStartLocation);

        return treasureHuntStartLocation;
    }

    public String getAddressUsingGoogleMap(double lat, double lng) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GEOCODING_API_URL)
                .queryParam("latlng", String.format("%s,%s", lat, lng))
                .queryParam("key", GOOGLE_MAP_API_KEY)
                .queryParam("language", "ko") // 결과를 한국어로 받기 위한 설정
                .build(true)
                .toUri();

        try {
            // 2. RestTemplate을 사용하여 API 호출 및 응답을 DTO로 매핑
            GeocodingResponse response = restTemplate.getForObject(uri, GeocodingResponse.class);

            // 3. 응답 결과 유효성 검사 및 주소 추출
            if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                log.info("Address not found");
                return "Error";
            }

            // 가장 첫 번째 결과의 formatted_address를 사용합니다.
            return Optional.ofNullable(response.getResults().get(0))
                    .map(GeocodingResult::getFormattedAddress)
                    .orElse("Error");

        } catch (Exception e) {
            log.info("Address Error");
            return "Error";
        }
    }

    public String getIndicatingAddress(String address) {
        if ("Error".equalsIgnoreCase(address)) {
            return "주소 오류";
        }

        String[] parts = address.split(" ");

        // 주소가 3단어보다 짧을 경우, 원본 주소를 그대로 반환
        if (parts.length < 3) {
            return address;
        }

        // 앞 3단어를 합쳐서 반환
        return parts[0] + " " + parts[1] + " " + parts[2];
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeocodingResponse {
        @JsonProperty("results")
        private List<GeocodingResult> results;

        public List<GeocodingResult> getResults() {
            return results;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeocodingResult {
        @JsonProperty("formatted_address")
        private String formattedAddress;

        public String getFormattedAddress() {
            return formattedAddress;
        }
    }

    public static TreasureHuntStartLocationDto treasureHuntStartLocationToTreasureHuntStartLocationDto(TreasureHuntStartLocation entity) {
        if (entity == null) return null;

        return new TreasureHuntStartLocationDto(
                entity.getTreasureHuntStartLocationId(),
                entity.getIndicateLocation(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getAltitude()
        );
    }
}
