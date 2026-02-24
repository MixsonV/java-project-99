package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
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
public class LabelControllerTest {

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
    private ObjectMapper om;
    @Autowired
    private LabelMapper mapper;
    @Autowired
    private LabelRepository repository;
    @Autowired
    private LabelService service;
    private JwtRequestPostProcessor token;
    private Label label;
    private Label testLabel;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        repository.save(testLabel);

        label = Instancio.of(modelGenerator.getLabelModel()).create();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/labels").with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<LabelDTO> labelDTOS = om.readValue(body, new TypeReference<>() {
        });

        var actual = labelDTOS.stream().map(mapper::map).toList();
        var expected = repository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/labels/" + testLabel.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    void testCreate() throws Exception {
        var request = post("/api/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(label));

        var response = mockMvc.perform(request.with(token))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(label.getName())
        );

        var id = om.readTree(body).path("id").asLong();
        assertThat(repository.existsById(id)).isTrue();
        assertThat(repository.existsByName(testLabel.getName())).isTrue();
    }

    @Test
    void testUpdate() throws Exception {
        var dto = new LabelUpdateDTO();
        dto.setName(JsonNullable.of("Documentation"));

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        assertThat(repository.findByName(dto.getName().get())).isPresent();
    }


    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(token))
                .andExpect(status().isNoContent());
        assertThat(repository.existsById(testLabel.getId())).isFalse();
    }

    @Test
    public void unauthorizedTest() throws Exception {

        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/labels/" + testLabel.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(testLabel)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(testLabel)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isUnauthorized());
    }
}
