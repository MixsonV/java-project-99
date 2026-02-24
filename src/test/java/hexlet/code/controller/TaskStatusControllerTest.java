package hexlet.code.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.web.servlet.request
        .SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"command.line.runner.enabled=false", "application.runner.enabled=false"})
@AutoConfigureMockMvc
@Rollback
public class TaskStatusControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Mock
    private JwtDecoder jwtDecoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    TaskStatusRepository repository;
    @Autowired
    TaskStatusMapper mapper;
    @Autowired
    TaskStatusService service;
    @Autowired
    private ObjectMapper om;
    private TaskStatus newStatus;
    private TaskStatus testStatus;
    private JwtRequestPostProcessor token;

    @BeforeEach
   void setUp() {
        testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        repository.save(testStatus);

        newStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses").with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskStatusDTO> taskStatusDTOS = om.readValue(body, new TypeReference<List<TaskStatusDTO>>() {
        });

        var actual = taskStatusDTOS.stream().map(mapper::map).toList();
        var expected = repository.findAll();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses/" + testStatus.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testStatus.getName()),
                v -> v.node("slug").isEqualTo(testStatus.getSlug())
        );
    }

    @Test
    void testShowSlug() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses/slug/" + testStatus.getSlug()).with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testStatus.getName()),
                v -> v.node("slug").isEqualTo(testStatus.getSlug())
        );
    }

    @Test
    void testCreate() throws Exception {
        var request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newStatus));

        var response = mockMvc.perform(request.with(token))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(newStatus.getName()),
                v -> v.node("slug").isEqualTo(newStatus.getSlug())
        );

        var id = om.readTree(body).path("id").asLong();
        assertThat(repository.existsById(id)).isTrue();
        assertThat(repository.existsBySlug(testStatus.getSlug())).isTrue();
    }

    @Test
    void testUpdate() throws Exception {
        var dto = new TaskStatusUpdateDTO();
        dto.setName(JsonNullable.of("Progress"));
        dto.setSlug(JsonNullable.undefined());

        var request = put("/api/task_statuses/" + testStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request.with(token))
                .andExpect(status().isOk());

        var foundTask = repository.findById(testStatus.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task status with id %d not found", testStatus.getId())));

        assertThat(foundTask.getName()).isEqualTo(dto.getName().get());
        assertThat(foundTask.getSlug()).isEqualTo(testStatus.getSlug());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testStatus.getId()).with(token))
                .andExpect(status().isNoContent());
        assertThat(repository.existsById(testStatus.getId())).isFalse();
    }


    @Test
    void testUnauthorizedRights() throws Exception {
        var dto = new TaskStatusUpdateDTO();
        dto.setName(JsonNullable.of("Checking"));
        dto.setSlug(JsonNullable.of("checking"));

        var testTaskStatusId = testStatus.getId();

        var deleteRequest = delete("/api/task_statuses/" + testTaskStatusId);

        mockMvc.perform(deleteRequest).andExpect(status().isUnauthorized());
        assertThat(repository.existsById(testTaskStatusId)).isTrue();

        var postRequest = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newStatus));

        mockMvc.perform(postRequest).andExpect(status().isUnauthorized());
        assertThat(newStatus.getId()).isNull();

        var putRequest = put("/api/task_statuses/" + testTaskStatusId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(putRequest).andExpect(status().isUnauthorized());
        var foundStatus = repository.findById(testTaskStatusId);
        assertThat(foundStatus.isPresent()).isTrue();
        assertThat(foundStatus.get().getSlug()).isEqualTo(testStatus.getSlug());

    }
}
