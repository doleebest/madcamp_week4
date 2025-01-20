package madcamp4.Our_Beloved_KAIST.Domain;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TimeCapsule {
    private String id;
    private String name;
    private String creatorName;
    private String inviteCode;
    private String createdAt;      // LocalDateTime 대신 String 사용
    private String openDate;
    private boolean isSealed;
    private GeoPoint location;
    private List<String> memberIds;
    private List<Marble> marbles;

    // Firebase Realtime DB를 위한 기본 생성자
    public TimeCapsule() {
        this.memberIds = new ArrayList<>();
        this.marbles = new ArrayList<>();
    }

}