package madcamp4.Our_Beloved_KAIST.Dto;

public class NearbyRequest {

    private double latitude;
    private double longitude;
    private double radiusInKm;

    // 기본 생성자
    public NearbyRequest() {}

    // 생성자
    public NearbyRequest(double latitude, double longitude, double radiusInKm) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusInKm = radiusInKm;
    }

    // Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadiusInKm() {
        return radiusInKm;
    }

    public void setRadiusInKm(double radiusInKm) {
        this.radiusInKm = radiusInKm;
    }

    @Override
    public String toString() {
        return "NearbyRequest{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", radiusInKm=" + radiusInKm +
                '}';
    }
}

