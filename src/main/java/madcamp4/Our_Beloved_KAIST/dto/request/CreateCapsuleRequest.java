package madcamp4.Our_Beloved_KAIST.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCapsuleRequest {
    @NotBlank(message = "캡슐 이름은 필수입니다")
    private String name;

    @NotBlank(message = "생성자 이름은 필수입니다")
    private String creator;
}
