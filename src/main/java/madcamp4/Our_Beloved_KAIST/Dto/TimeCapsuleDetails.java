package madcamp4.Our_Beloved_KAIST.Dto;

import java.time.LocalDateTime;

public class TimeCapsuleDetails {
    private Long id;
    private String name;
    private LocalDateTime openDate;

    public TimeCapsuleDetails(Long id, String name, LocalDateTime openDate) {
        this.id = id;
        this.name = name;
        this.openDate = openDate;
    }

    // Getter
    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }
}