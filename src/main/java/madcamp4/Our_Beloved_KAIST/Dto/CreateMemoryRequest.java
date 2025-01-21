package madcamp4.Our_Beloved_KAIST.Dto;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.lang.management.MemoryType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemoryRequest {
    @NotNull
    private MemoryType type;

    @NotBlank
    private String content;
}