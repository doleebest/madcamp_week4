package madcamp4.Our_Beloved_KAIST.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.lang.management.MemoryType;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Memory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemoryType type; // TEXT, IMAGE, VIDEO

    private String content; // 텍스트 내용 또는 Firebase 저장소 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id")
    private TimeCapsule timeCapsule;

    private LocalDateTime createdAt;
}