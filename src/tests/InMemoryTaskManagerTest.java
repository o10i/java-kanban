package tests;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    void newManager() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void getTasks() {
    }

    @Test
    void getEpics() {
    }

    @Test
    void getSubtasks() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void getTask() {
    }

    @Test
    void getEpic() {
    }

    @Test
    void getSubtask() {
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void addNewSubtask() {
        Epic epic = new Epic("Test addNewSubtask", "Test addNewSubtask description");
        final int epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void getSubtasksByEpicId() {
    }

    @Test
    void getStatus() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void determineEpicStatusForNull() {
        Epic epic = new Epic("Test determineEpicStatusForNull", "Test determineEpicStatusForNull description");
        final int epicId = taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);
        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertEquals(0, taskManager.getSubtasks().size(), "Список подзадач не пуст.");

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNew() {
        addEpicAndSubtasks(NEW);

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        final Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForDone() {
        addEpicAndSubtasks(DONE);

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        final Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(DONE, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNewAndDone() {
        Epic epic = new Epic("Test determineEpicStatusForNewAndDone", "Test determineEpicStatusForNewAndDone description");
        final int epicId = taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Test determineEpicStatusForNewAndDone1", "Test determineEpicStatusForNewAndDone1 description", NEW, epicId);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test determineEpicStatusForNewAndDone2", "Test determineEpicStatusForNewAndDone2 description", DONE, epicId);
        taskManager.addSubtask(subtask2);

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        final Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(epicId);
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForInProgress() {
        addEpicAndSubtasks(IN_PROGRESS);

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        final Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    private void addEpicAndSubtasks(Status status) {
        Epic epic = new Epic("Test determineEpicStatus", "Test determineEpicStatus description");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Test determineEpicStatus1", "Test determineEpicStatus1 description", status, epic.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test determineEpicStatus2", "Test determineEpicStatus2 description", status, epic.getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Test determineEpicStatus3", "Test determineEpicStatus3 description", status, epic.getId());
        taskManager.addSubtask(subtask3);
    }

    @Test
    void getHistory() {
    }

    @Test
    void getHistoryManager() {
    }

    @Test
    void getIdCounter() {
    }
}