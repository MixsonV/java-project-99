package hexlet.code.component;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskStatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {
    private final UserService userService;
    private final TaskStatusService taskStatusService;
    private final TaskStatusRepository taskStatusRepository;

    private final LabelService labelService;
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateDTO();
        userData.setFirstName("admin");
        userData.setLastName("hexlet");
        userData.setEmail("hexlet@example.com");
        userData.setPassword("qwerty");
        userService.create(userData);

        String[][] statuses = {
                {"Draft", "draft"},
                {"To Review", "to_review"},
                {"To Be fixed", "to_be_fixed"},
                {"To publish", "to_publish"},
                {"Published", "published"}
        };

        for (String[] status : statuses) {
            String name = status[0];
            String slug = status[1];

            if (!taskStatusRepository.existsBySlug(slug)) {
                var taskData = new TaskStatusCreateDTO();
                taskData.setName(name);
                taskData.setSlug(slug);
                taskStatusService.create(taskData);
            }
        }


        String[] labels = {"feature", "bug"};

        for (String label : labels) {
            if (!labelRepository.existsByName(label)) {
                var labelData = new LabelCreateDTO();
                labelData.setName(label);
                labelService.create(labelData);
            }
        }
    }
}
