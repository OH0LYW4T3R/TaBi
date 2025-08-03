package com.example.tabi.util;

public class GeoUtil {
    private static final double EARTH_RADIUS_KM = 6371.0;
    /**
     * 위도, 경도를 기준으로 반경 1km 이내에 있는지 판단
     *
     * @param lat1 기준 위도
     * @param lon1 기준 경도
     * @param lat2 대상 위도
     * @param lon2 대상 경도
     * @return 1km 이내면 true, 아니면 false
     */
    public static boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, Double base_radius) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceKm = EARTH_RADIUS_KM * c;

        return distanceKm <= base_radius;
    }
}
