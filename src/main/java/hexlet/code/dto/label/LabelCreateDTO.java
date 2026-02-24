package hexlet.code.dto.label;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabelCreateDTO {
    @NotNull
    @Size(min = 3, max = 1000)
    private String name;
}
