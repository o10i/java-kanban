package manager;

import tasks.Status;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    String getAllTasks();

    void deleteAllTasks();

    Task getTask(Integer id);

    void addTask(Task task);

    int getIdCounter();

    void updateTask(Task task, int id);

    void deleteTask(Integer id);

    String getEpicSubtasksByEpicId(int id);

    Status getStatus(Integer id);

    void setStatus(Integer id, Status status);

    List<Task> getHistory();
}
