package madcamp4.Our_Beloved_KAIST.Domain;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.remoteconfig.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.xml.stream.Location;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TimeCapsule {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    private String inviteCode;
    private LocalDateTime openDate;
    private String songUrl;
    private Double latitude;
    private Double longitude;
    private boolean isOpened;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL)
    private List<Memory> memories = new ArrayList<>();
}
