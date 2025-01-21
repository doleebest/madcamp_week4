package madcamp4.Our_Beloved_KAIST.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.ARMarker;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ARMarkerResponse {
    private String id;
    private String capsuleId;
    private double latitude;
    private double longitude;
    private String markerData;
    private String createdAt;

    public static ARMarkerResponse from(ARMarker marker) {
        return ARMarkerResponse.builder()
                .id(marker.getId())
                .capsuleId(marker.getCapsuleId())
                .latitude(marker.getLatitude())
                .longitude(marker.getLongitude())
                .markerData(marker.getMarkerData())
                .createdAt(marker.getCreatedAt())
                .build();
    }
}