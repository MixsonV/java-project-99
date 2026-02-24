package hexlet.code.dto.label;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
public class LabelDTO {
    private Long id;
    private String name;
    @CreatedDate
    private LocalDateTime createdAt;
}
