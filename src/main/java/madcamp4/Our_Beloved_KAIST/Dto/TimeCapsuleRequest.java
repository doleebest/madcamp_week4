package madcamp4.Our_Beloved_KAIST.Dto;

import java.time.LocalDateTime;

public class TimeCapsuleRequest {
    private String name;
    private String ownerId;
    private LocalDateTime openDate;
    private String location;

    // Getters and Setters
    // Getters
    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

