package hexlet.code.controller;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@AllArgsConstructor
public class TaskStatusController {
    private final TaskStatusService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<TaskStatusDTO>> indexTaskStatus() {
        var tasks = service.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    TaskStatusDTO createTaskStatus(@Valid @RequestBody TaskStatusCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskStatusDTO showTaskStatus(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/slug/{slug}")
    @ResponseStatus(HttpStatus.OK)
    TaskStatusDTO showTaskStatusBySlug(@PathVariable String slug) {
        return service.findBySlug(slug);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskStatusDTO updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateDTO dto) {
        return service.updateById(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTaskStatus(@PathVariable Long id) {
        service.deleteById(id);
    }
}
