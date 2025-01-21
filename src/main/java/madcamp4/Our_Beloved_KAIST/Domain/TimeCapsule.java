package madcamp4.Our_Beloved_KAIST.Domain;

import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@IgnoreExtraProperties
public class TimeCapsule {
    private String id;
    private String name;
    private String creator;
    private String createdAt;
    private String openDate;
    private boolean sealed;

    public TimeCapsule() {
        // Default constructor required for Firebase
    }

    public void setCreatedAt(LocalDateTime dateTime) {
        this.createdAt = dateTime.toString();
    }

    public void setCreatedAtString(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setOpenDate(LocalDateTime dateTime) {
        this.openDate = dateTime.toString();
    }

    public void setOpenDateString(String openDate) {
        this.openDate = openDate;
    }

    public LocalDateTime getOpenDateAsDateTime() {
        return openDate != null ? LocalDateTime.parse(openDate) : null;
    }
}
