package controller;


import model.Task;
import service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestParam String title,
                                           @RequestParam(required = false) String description,
                                           @RequestParam String status,
                                           @RequestParam Long userId,
                                           @RequestParam(required = false) String timeZone) {
        Task task = taskService.createTask(title, description, status, userId, timeZone);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestParam String title,
                                           @RequestParam(required = false) String description,
                                           @RequestParam String status,
                                           @RequestParam(required = false) String timeZone) {
        return ResponseEntity.ok(taskService.updateTask(id, title, description, status, timeZone));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
