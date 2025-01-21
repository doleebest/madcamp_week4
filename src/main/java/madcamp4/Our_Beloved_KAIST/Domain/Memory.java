package madcamp4.Our_Beloved_KAIST.Domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Memory {

    private String id;
    private MemoryType type;
    private String content;
    private String timeCapsuleId;
    private LocalDateTime createdAt;
    private TimeCapsule timeCapsule; // 이 필드가 있어야 함

    // 필요시 Getter를 명시적으로 정의
    public TimeCapsule getTimeCapsule() {
        return timeCapsule;
    }
}