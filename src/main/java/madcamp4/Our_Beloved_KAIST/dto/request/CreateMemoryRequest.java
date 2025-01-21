package madcamp4.Our_Beloved_KAIST.dto.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import madcamp4.Our_Beloved_KAIST.Domain.MemoryType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemoryRequest {
    @NotNull
    private MemoryType type;

    @NotBlank
    private String content;

    // Firebase로 저장될 수 있도록 String 타입의 capsuleId 추가
    private String capsuleId;
}