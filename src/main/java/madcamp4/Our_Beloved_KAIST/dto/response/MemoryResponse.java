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
    private LocalDateTime createdAt;
    private String capsuleId;  // Long -> String 변경

    public static MemoryResponse from(Memory memory) {
        return MemoryResponse.builder()
                .id(memory.getId())  // Long -> String으로 수정 시 id 값을 String으로 변환
                .type(memory.getType())
                .content(memory.getContent())
                .createdAt(memory.getCreatedAt())
                .capsuleId(memory.getTimeCapsule().getId())  // Long -> String으로 수정 시
                .build();
    }
}
