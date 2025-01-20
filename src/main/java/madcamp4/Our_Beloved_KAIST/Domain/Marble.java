package madcamp4.Our_Beloved_KAIST.Domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Marble {
    private String id;
    private String userId;
    private String content;
    private List<String> mediaUrls;
    private LocalDateTime createdAt;
}