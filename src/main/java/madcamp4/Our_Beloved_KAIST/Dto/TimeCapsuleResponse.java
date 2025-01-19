package madcamp4.Our_Beloved_KAIST.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;

import java.time.LocalDateTime;

public class TimeCapsuleResponse {
    private Long id;
    private String inviteCode;

    public TimeCapsuleResponse(Long id, String inviteCode) {
        this.id = id;
        this.inviteCode = inviteCode;
    }

    // Getters
    // Getters
    public Long getId() {
        return id;
    }

    public String getInviteCode() {
        return inviteCode;
    }
}