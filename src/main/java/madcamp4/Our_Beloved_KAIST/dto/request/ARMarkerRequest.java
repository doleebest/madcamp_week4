package madcamp4.Our_Beloved_KAIST.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ARMarkerRequest {
    private double latitude;
    private double longitude;
    private String markerData;
}