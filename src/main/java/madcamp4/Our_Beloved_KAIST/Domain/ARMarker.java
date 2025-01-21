package madcamp4.Our_Beloved_KAIST.Domain;

import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@IgnoreExtraProperties
public class ARMarker {
    private String id;
    private String capsuleId;
    private double latitude;
    private double longitude;
    private String markerData;  // AR 마커 관련 데이터
    private String createdAt;

    public ARMarker() {
        // Default constructor for Firebase
    }

    public void setCreatedAt(LocalDateTime dateTime) {
        this.createdAt = dateTime.toString();
    }

    public void setCreatedAtString(String createdAt) {
        this.createdAt = createdAt;
    }
}