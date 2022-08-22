package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void deleteTask(Integer id);

    void deleteAllTasks();

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    Subtask getSubtask(Integer id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getAllTasks();

    List<Subtask> getSubtasksByEpicId(int id);

    Status getStatus(Integer id);

    List<Task> getHistory();

    int getIdCounter();

    Set<Task> getPrioritizedTasks();

    void setStatus(Integer id, Status status);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);
}