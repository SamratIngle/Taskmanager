package service;


import model.Task;
import model.TaskStatus;
import model.User;
import repository.TaskRepository;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(String title, String description, String status, Long userId, String timeZone) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ZonedDateTime now = timeZone != null
            ? ZonedDateTime.now(ZoneId.of(timeZone)).withZoneSameInstant(ZoneId.of("UTC"))
            : ZonedDateTime.now(ZoneId.of(user.getTimeZone())).withZoneSameInstant(ZoneId.of("UTC"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.valueOf(status));
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        task.setAssignedTo(user);

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, String title, String description, String status, String timeZone) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.valueOf(status));
        task.setUpdatedAt(ZonedDateTime.now(ZoneId.of(timeZone != null ? timeZone : "UTC")));

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
