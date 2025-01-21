package madcamp4.Our_Beloved_KAIST.Domain;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Exclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IgnoreExtraProperties
public class Memory {
    private String id;
    private MemoryType type;
    private String content;
    private String timeCapsuleId;
    private String createdAt;

    @Exclude  // Firebase에서 제외
    private TimeCapsule timeCapsule;

    public Memory() {
        // Default constructor required for Firebase
    }

    public void setCreatedAt(LocalDateTime dateTime) {
        this.createdAt = dateTime.toString();
    }

    // String을 직접 설정하는 메소드 추가
    public void setCreatedAtString(String createdAt) {
        this.createdAt = createdAt;
    }
}