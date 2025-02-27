package madcamp4.Our_Beloved_KAIST.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SealCapsuleRequest {
    @NotNull(message = "개봉 날짜는 필수입니다")
    @Future(message = "개봉 날짜는 미래 날짜여야 합니다")
    private LocalDateTime openDate;

    // Firebase로 저장될 수 있도록 String 타입의 capsuleId 추가
    private String capsuleId;
}