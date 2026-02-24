package hexlet.code.util;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.datafaker.Faker;
import org.instancio.Model;
import org.instancio.Select;


@Component
public class ModelGenerator {

    private Model<User> userModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;

    @Autowired
    private Faker faker;

    public Model<User> getUserModel() {
        return userModel;
    }

    public Model<TaskStatus> getTaskStatusModel() {
        return taskStatusModel;
    }

    public Model<Task> getTaskModel() {
        return taskModel;
    }

    public Model<Label> getLabelModel() {
        return labelModel;
    }

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field("id"))
                .ignore(Select.field("firstName"))
                .ignore(Select.field("lastName"))
                .supply(Select.field("email"), () -> faker.internet().emailAddress())
                .supply(Select.field("password"), () -> faker.internet().password(3, 64))
                .ignore(Select.field("tasks"))
                .toModel();


        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field("id"))
                .supply(Select.field("name"), () -> faker.name().title())
                .supply(Select.field("slug"), () -> faker.internet().slug())
                .ignore(Select.field("task"))
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field("id"))
                //.supply(Select.field("assignee"), () -> faker.number().numberBetween(0, 999))
                .supply(Select.field("index"), () -> faker.number().numberBetween(0, 999))
                .supply(Select.field("name"), () -> faker.name().title())
                .supply(Select.field("description"), () -> faker.lorem().characters(0, 200))
                .ignore(Select.field("taskStatus"))
                .ignore(Select.field("labelsUsed"))
                .ignore(Select.field("assignee"))
                .toModel();

        labelModel = Instancio.of(Label.class)
                .ignore(Select.field("id"))
                .supply(Select.field("name"), () -> faker.lorem().characters(3, 1000))
                .toModel();
    }
}
