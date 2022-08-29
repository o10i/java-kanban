package managers.task;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(Integer id);

    void deleteAllTasks();

    void determineEpicStatus(int id);

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    Subtask getSubtask(Integer id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getAllTasksSortedById();

    Set<Task> getPrioritizedTasks();

    List<Subtask> getSubtasksByEpicId(int id);

    Status getStatus(Integer id);

    void setStatus(Integer id, Status status);

    List<Task> getHistory();

    int getIdCounter();
}