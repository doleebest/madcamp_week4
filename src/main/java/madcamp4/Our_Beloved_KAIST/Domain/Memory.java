package madcamp4.Our_Beloved_KAIST.Domain;

import com.google.firebase.remoteconfig.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

@Entity
public class Memory {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TimeCapsule timeCapsule;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    private String content;
    private String mediaUrl;
    private MediaType mediaType; // PHOTO, VIDEO, TEXT
}

