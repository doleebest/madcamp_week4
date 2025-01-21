package madcamp4.Our_Beloved_KAIST.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyCapsuleResponse {
    private String capsuleId;
    private String name;
    private double latitude;
    private double longitude;
    private double distance;  // 현재 위치로부터의 거리 (미터)
}