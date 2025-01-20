package madcamp4.Our_Beloved_KAIST.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String name;
    private LocalDateTime lastLogin;

    @Builder
    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastLogin = user.getLastLogin();
    }
}
