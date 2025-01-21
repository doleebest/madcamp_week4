package madcamp4.Our_Beloved_KAIST.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;

import java.lang.management.MemoryType;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoryResponse {
    private Long id;
    private MemoryType type;
    private String content;
    private LocalDateTime createdAt;
    private Long capsuleId;

    public static MemoryResponse from(Memory memory) {
        return MemoryResponse.builder()
                .id(memory.getId())
                .type(memory.getType())
                .content(memory.getContent())
                .createdAt(memory.getCreatedAt())
                .capsuleId(memory.getTimeCapsule().getId())
                .build();
    }
}