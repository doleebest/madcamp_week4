package madcamp4.Our_Beloved_KAIST.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeCapsuleResponse {
    private String id;  // Long -> String 변경
    private String name;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime openDate;
    private boolean sealed;
    private int memoryCount;

    public static TimeCapsuleResponse from(TimeCapsule capsule) {
        return TimeCapsuleResponse.builder()
                .id(capsule.getId())  // Long -> String으로 수정 시 id 값을 String으로 변환
                .name(capsule.getName())
                .creator(capsule.getCreator())
                .createdAt(capsule.getCreatedAt())
                .openDate(capsule.getOpenDate())
                .sealed(capsule.isSealed())
                .memoryCount(capsule.getMemories().size())
                .build();
    }
}
