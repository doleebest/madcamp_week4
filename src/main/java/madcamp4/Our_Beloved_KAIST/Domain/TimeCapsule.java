package madcamp4.Our_Beloved_KAIST.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TimeCapsule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime openDate;
    private boolean sealed;

    @OneToMany(mappedBy = "timeCapsule", cascade = CascadeType.ALL)
    private List<Memory> memories = new ArrayList<>();
}