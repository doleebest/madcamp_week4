package madcamp4.Our_Beloved_KAIST.Dto;

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
    private Long id;
    private String name;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime openDate;
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
                .memoryCount(capsule.getMemories().size())
                .build();
    }
}