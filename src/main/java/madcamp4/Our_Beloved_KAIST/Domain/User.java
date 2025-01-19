package madcamp4.Our_Beloved_KAIST.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    private String id;  // Google의 sub 값을 ID로 사용

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "creator")
    private List<TimeCapsule> timeCapsules = new ArrayList<>();

    @Builder
    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastLogin = LocalDateTime.now();
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
}