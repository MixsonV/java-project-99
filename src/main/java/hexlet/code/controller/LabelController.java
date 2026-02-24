package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
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
@RequestMapping("/api/labels")
@AllArgsConstructor
public class LabelController {
    private final LabelService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<LabelDTO>> indexLabel() {
        var labels = service.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    LabelDTO createLabel(@Valid @RequestBody LabelCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    LabelDTO showLabel(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    LabelDTO updateLabel(@PathVariable Long id, @Valid @RequestBody LabelUpdateDTO dto) {
        return service.updateById(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLabel(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/label/{name}")
    @ResponseStatus(HttpStatus.OK)
    LabelDTO showLabelByName(@PathVariable String name) {
        return service.findByName(name);
    }
}
