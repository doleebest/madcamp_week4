package madcamp4.Our_Beloved_KAIST.Domain;

public class Location {
    private String id;  // 파이어베이스 문서 ID
    private Double latitude;
    private Double longitude;
    private String name;

    // 파이어베이스는 빈 생성자가 필요합니다
    public Location() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}