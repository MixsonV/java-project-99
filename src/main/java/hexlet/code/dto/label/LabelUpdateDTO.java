package hexlet.code.dto.label;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class LabelUpdateDTO {
    @NotNull
    @Size(min = 3, max = 1000)
    private JsonNullable<String> name;
}
