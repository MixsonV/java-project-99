package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    private Integer index;
    private String title;
    private String content;
    private String status;
    @CreatedDate
    private LocalDateTime createdAt;
    private Set<Long> taskLabelIds = new HashSet<>();

    public Set<Long> getTaskLabelIds() {
        return taskLabelIds;
    }

    public void setTaskLabelIds(Set<Long> taskLabelIds) {
        this.taskLabelIds = taskLabelIds;
    }
}
