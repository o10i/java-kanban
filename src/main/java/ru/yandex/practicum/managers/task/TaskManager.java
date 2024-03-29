package ru.yandex.practicum.managers.task;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    Task getTaskById(Integer id);
    Subtask getSubtaskById(Integer id);
    Epic getEpicById(Integer id);
    List<Task> getTasks();
    List<Subtask> getSubtasks();
    List<Epic> getEpics();
    int addTask(Task task);
    int addSubtask(Subtask subtask);
    int addEpic(Epic epic);
    void updateTask(Task task);
    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);
    void deleteTaskById(Integer id);
    void deleteSubtaskById(Integer id);
    void deleteEpicById(Integer id);
    void deleteAllTasks();
    void deleteAllSubtasks();
    void deleteAllEpics();
    void determineEpicStatus(int id);
    List<Subtask> getEpicSubtasks(int id);
    List<Task> getHistory();
    Set<Task> getPrioritizedTasks();
    boolean checkIntersection(Task task);
}