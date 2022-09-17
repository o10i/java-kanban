package ru.yandex.practicum;

import ru.yandex.practicum.managers.task.TaskManager;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.tasks.enums.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected final String filename = "src/test/resources/history.csv";
    protected T taskManager;
    protected Task taskByDefault;
    protected Epic epicByDefault;
    protected Subtask subtaskByDefault;

    protected void addTasks() {
        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        taskManager.addTask(taskByDefault);
        epicByDefault = new Epic("Test epic", "Test epic description");
        taskManager.addEpic(epicByDefault);
        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        taskManager.addSubtask(subtaskByDefault);
    }

    @Test
    void getTasks() {
        addTasks();

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(taskByDefault, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getSubtasks() {
        addTasks();

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtaskByDefault, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getEpics() {
        addTasks();

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epicByDefault, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void getTaskById() {
        addTasks();

        Task savedTask = taskManager.getTaskById(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskByDefault, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getEpicById() {
        addTasks();

        Epic savedEpic = taskManager.getEpicById(2);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epicByDefault, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void getSubtaskById() {
        addTasks();

        Subtask savedSubtask = taskManager.getSubtaskById(3);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtaskByDefault, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void addNewTask() {
        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        int taskId = taskManager.addTask(taskByDefault);

        Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskByDefault, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(taskByDefault, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epicByDefault, savedEpic, "Эпики не совпадают.");

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epicByDefault, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void addNewSubtask() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        int subtaskId = taskManager.addSubtask(subtaskByDefault);

        Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtaskByDefault, savedSubtask, "Подзадачи не совпадают.");

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtaskByDefault, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void updateTask() {
        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        int taskByDefaultId = taskManager.addTask(taskByDefault);

        Task savedTask1 = taskManager.getTaskById(taskByDefaultId);

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(taskByDefault, savedTask1, "Задачи не совпадают.");

        Task task2 = new Task("Test task2", "Test task2 description", 20, LocalDateTime.now());
        task2.setId(taskByDefaultId);

        taskManager.updateTask(task2);

        Task savedTask2 = taskManager.getTaskById(taskByDefaultId);

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);

        Epic savedEpic1 = taskManager.getEpicById(epicByDefaultId);

        assertNotNull(savedEpic1, "Эпик не найден.");
        assertEquals(epicByDefault, savedEpic1, "Эпики не совпадают.");

        Epic epic2 = new Epic("Test epic2", "Test epic2 description");
        epic2.setId(epicByDefaultId);

        taskManager.updateEpic(epic2);

        Epic savedEpic2 = taskManager.getEpicById(epicByDefaultId);

        assertNotNull(savedEpic2, "Эпик не найден.");
        assertEquals(epic2, savedEpic2, "Эпики не совпадают.");

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(savedEpic2, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void updateSubtask() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        int subtaskByDefaultId = taskManager.addSubtask(subtaskByDefault);

        Subtask savedSubtask1 = taskManager.getSubtaskById(subtaskByDefaultId);

        assertNotNull(savedSubtask1, "Подзадача не найдена.");
        assertEquals(subtaskByDefault, savedSubtask1, "Подзадачи не совпадают.");

        Subtask subtask2 = new Subtask("Test subtask2", "Test subtask2 description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        subtask2.setId(subtaskByDefaultId);

        taskManager.updateSubtask(subtask2);

        Subtask savedSubtask2 = taskManager.getSubtaskById(subtaskByDefaultId);

        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают.");

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask2, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void deleteTaskById() {
        addTasks();

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

        taskManager.deleteTaskById(1);

        List<Task> tasksAfterRemove = taskManager.getTasks();

        assertNotNull(tasksAfterRemove, "Задачи не возвращаются.");
        assertEquals(0, tasksAfterRemove.size(), "Неверное количество задач.");

        Task savedTask = taskManager.getTaskById(1);

        assertNull(savedTask, "Задача не удалена.");
    }

    @Test
    void deleteEpicWithSubtaskById() {
        addTasks();

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();


        assertNotNull(epics, "Эпики не возвращаются.");
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");

        taskManager.deleteEpicById(2);

        List<Epic> epicsAfterRemove = taskManager.getEpics();
        List<Subtask> subtasksAfterRemove = taskManager.getSubtasks();

        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertNotNull(subtasksAfterRemove, "Подзадачи не возвращаются.");
        assertEquals(0, epicsAfterRemove.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasksAfterRemove.size(), "Неверное количество подзадач.");

        Subtask savedSubtask = taskManager.getSubtaskById(3);
        Epic savedEpic = taskManager.getEpicById(2);

        assertNull(savedEpic, "Эпик не удалён.");
        assertNull(savedSubtask, "Подзадача не удалена.");
    }

    @Test
    void deleteSubtaskById() {
        addTasks();

        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Epic> epics = taskManager.getEpics();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        taskManager.deleteSubtaskById(3);

        List<Epic> epicsAfterRemove = taskManager.getEpics();
        List<Subtask> subtasksAfterRemove = taskManager.getSubtasks();

        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertNotNull(subtasksAfterRemove, "Подзадачи не возвращаются.");
        assertEquals(1, epicsAfterRemove.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasksAfterRemove.size(), "Неверное количество подзадач.");

        Subtask savedSubtask = taskManager.getSubtaskById(3);
        Epic savedEpic = taskManager.getEpicById(2);

        assertNull(savedSubtask, "Подзадача не удалена.");
        assertNotNull(savedEpic, "Эпик удалён.");
    }

    @Test
    public void deleteAllTasks() {
        addTasks();

        List<Task> tasks = taskManager.getTasks();

        assertEquals(1, tasks.size(), "Неверное количество задач.");

        taskManager.deleteAllTasks();

        List<Task> tasksAfterRemove = taskManager.getTasks();

        assertNotNull(tasksAfterRemove, "Задачи не возвращаются.");
        assertEquals(0, tasksAfterRemove.size(), "Неверное количество задач.");
    }

    @Test
    public void deleteAllEpics() {
        addTasks();

        List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество задач.");

        taskManager.deleteAllEpics();

        List<Epic> epicsAfterRemove = taskManager.getEpics();

        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertEquals(0, epicsAfterRemove.size(), "Неверное количество эпиков.");
    }

    @Test
    public void deleteAllSubtasks() {
        addTasks();

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");

        taskManager.deleteAllSubtasks();

        List<Epic> epicsAfterRemove = taskManager.getEpics();
        List<Subtask> subtasksAfterRemove = taskManager.getSubtasks();

        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertNotNull(subtasksAfterRemove, "Подзадачи не возвращаются.");
        assertEquals(1, epicsAfterRemove.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasksAfterRemove.size(), "Неверное количество подзадач.");
    }

    @Test
    void determineEpicStatusForNull() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        Epic savedEpic = taskManager.getEpicById(epicId);

        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertEquals(0, taskManager.getSubtasks().size(), "Список подзадач не пуст.");

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNew() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        taskManager.addSubtask(subtaskByDefault);

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNewAndDone() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        taskManager.addSubtask(subtaskByDefault);

        Subtask subtask2 = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(2), epicByDefault.getId());
        subtask2.setStatus(DONE);
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForInProgress() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        subtaskByDefault.setStatus(IN_PROGRESS);
        taskManager.addSubtask(subtaskByDefault);

        Subtask subtask2 = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(2), epicByDefault.getId());
        subtask2.setStatus(IN_PROGRESS);
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForDone() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        subtaskByDefault.setStatus(DONE);
        taskManager.addSubtask(subtaskByDefault);

        Subtask subtask2 = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(2), epicByDefault.getId());
        subtask2.setStatus(DONE);
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(DONE, status, "Статус неверный.");
    }

    @Test
    void getZeroEpicSubtasks() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        List<Subtask> subtasks = taskManager.getEpicSubtasks(epicId);

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void getOneEpicSubtasks() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        taskManager.addSubtask(subtaskByDefault);

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");

        List<Subtask> returnedSubtasks = taskManager.getEpicSubtasks(epicId);

        assertNotNull(returnedSubtasks, "Подзадачи на возвращаются.");
        assertEquals(1, returnedSubtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtaskByDefault, returnedSubtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getTwoEpicSubtasks() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicId = taskManager.addEpic(epicByDefault);

        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        taskManager.addSubtask(subtaskByDefault);

        Subtask subtask2 = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(2), epicByDefault.getId());
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");

        List<Subtask> returnedSubtasks = taskManager.getEpicSubtasks(epicId);

        assertNotNull(returnedSubtasks, "Подзадачи на возвращаются.");
        assertEquals(2, returnedSubtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtaskByDefault, returnedSubtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, returnedSubtasks.get(1), "Подзадачи не совпадают.");
    }
}