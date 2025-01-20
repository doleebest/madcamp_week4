package madcamp4.Our_Beloved_KAIST.Dto;

import madcamp4.Our_Beloved_KAIST.Domain.GeoPoint;

import java.time.LocalDateTime;

public class TimeCapsuleRequest {

    private GeoPoint location;
    private LocalDateTime openDate;

    // 기본 생성자
    public TimeCapsuleRequest() {}

    // 생성자
    public TimeCapsuleRequest(GeoPoint location, LocalDateTime openDate) {
        this.location = location;
        this.openDate = openDate;
    }

    // Getters and Setters
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    @Override
    public String toString() {
        return "TimeCapsuleRequest{" +
                "location=" + location +
                ", openDate=" + openDate +
                '}';
    }
}
