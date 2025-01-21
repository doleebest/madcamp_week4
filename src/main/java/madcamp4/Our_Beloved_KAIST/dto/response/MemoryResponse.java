package madcamp4.Our_Beloved_KAIST.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.MemoryType; // 올바른 임포트
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoryResponse {
    private String id;  // Long -> String 변경
    private MemoryType type;
    private String content;
    private String createdAt;
    private String capsuleId;  // Long -> String 변경

    public static MemoryResponse from(Memory memory) {
        return MemoryResponse.builder()
                .id(memory.getId())
                .type(memory.getType())
                .content(memory.getContent())
                .createdAt(memory.getCreatedAt())  // 이미 String으로 저장된 값을 그대로 사용
                .capsuleId(memory.getTimeCapsuleId())  // memory.getTimeCapsule().getId() 대신 직접 timeCapsuleId 사용
                .build();
    }
}
