package madcamp4.Our_Beloved_KAIST.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TimeCapsule {
    private String id;  // Firebase에서 관리하는 String ID
    private String name;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime openDate;
    private boolean sealed;

    private List<Memory> memories = new ArrayList<>();
}
