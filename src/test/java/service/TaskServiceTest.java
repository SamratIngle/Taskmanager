package service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Task;
import model.TaskStatus;
import model.User;
import repository.TaskRepository;
import repository.UserRepository;

public class TaskServiceTest {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);

    public TaskServiceTest(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(String title, String description, String status, Long userId, String timezone) {
        logger.debug("Creating task: title={}, description={}, status={}, userId={}, timezone={}", 
                     title, description, status, userId, timezone);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.valueOf(status));
        task.setAssignedTo(user);

        ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of(user.getTimeZone());
        task.setCreatedAt(ZonedDateTime.now(zoneId).withZoneSameInstant(ZoneId.of("UTC")));
        task.setUpdatedAt(ZonedDateTime.now(zoneId).withZoneSameInstant(ZoneId.of("UTC")));

        Task savedTask = taskRepository.save(task);
        logger.debug("Task created with id={}", savedTask.getId());
        return savedTask;
    }

    public Task updateTask(Long taskId, String title, String description, String status, String timezone) {
        logger.debug("Updating task: id={}, title={}, description={}, status={}, timezone={}", 
                     taskId, title, description, status, timezone);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.valueOf(status));

        ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of(task.getAssignedTo().getTimeZone());
        task.setUpdatedAt(ZonedDateTime.now(zoneId).withZoneSameInstant(ZoneId.of("UTC")));

        Task updatedTask = taskRepository.save(task);
        logger.debug("Task updated with id={}", updatedTask.getId());
        return updatedTask;
    }

    public void deleteTask(Long id) {
        logger.debug("Deleting task with id={}", id);

        taskRepository.deleteById(id);
        logger.debug("Task with id={} deleted", id);
    }
}
