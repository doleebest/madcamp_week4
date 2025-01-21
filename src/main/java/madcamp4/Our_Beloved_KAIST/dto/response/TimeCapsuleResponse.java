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
    private String id;
    private String name;
    private String creator;
    private String createdAt;  // LocalDateTime -> String
    private String openDate;   // LocalDateTime -> String
    private boolean sealed;
    private int memoryCount;

    public static TimeCapsuleResponse from(TimeCapsule capsule) {
        return TimeCapsuleResponse.builder()
                .id(capsule.getId())
                .name(capsule.getName())
                .creator(capsule.getCreator())
                .createdAt(capsule.getCreatedAt())
                .openDate(capsule.getOpenDate())
                .sealed(capsule.isSealed())
                .memoryCount(0)  // capsule.getMemories() 대신 일단 0으로 설정
                .build();
    }

    // 필요한 경우 날짜를 LocalDateTime으로 변환하는 메서드 추가
    public LocalDateTime getCreatedAtAsDateTime() {
        return createdAt != null ? LocalDateTime.parse(createdAt) : null;
    }

    public LocalDateTime getOpenDateAsDateTime() {
        return openDate != null ? LocalDateTime.parse(openDate) : null;
    }
}