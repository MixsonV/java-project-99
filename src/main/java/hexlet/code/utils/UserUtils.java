package hexlet.code.utils;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.WrongCredentialException;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;
    private final String adminUserName;

    @Autowired
    private TaskRepository taskRepository;

    public UserUtils() {
        this.adminUserName = "hexlet@example.com";
    }

    public boolean isAssignee(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        User assignee = task.getAssignee();
        User currentUser = getCurrentUser();

        return currentUser != null && Objects.equals(currentUser.getId(), assignee.getId());
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        var email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new WrongCredentialException("No user found with this name"));
    }

    public boolean isAdmin() {
        var currentUserName = getCurrentUser().getUsername();
        return currentUserName.equals(adminUserName);
    }

    public boolean isCurrentUserId(Long id) {
        var currentUserId = getCurrentUser().getId();
        return Objects.equals(currentUserId, id);
    }

    public boolean isCurrentUserName(String name) {
        var currentUserName = getCurrentUser().getUsername();
        return Objects.equals(currentUserName, name);
    }
}
