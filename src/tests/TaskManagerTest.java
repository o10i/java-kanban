package tests;

import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void getTasks() {
        Task task1 = new Task("Test getTasks1", "Test getTasks1 description");
        taskManager.addTask(task1);
        Task task2 = new Task("Test getTasks2", "Test getTasks2 description");
        taskManager.addTask(task2);
        Task task3 = new Task("Test getTasks3", "Test getTasks3 description");
        taskManager.addTask(task3);

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают.");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают.");

    }

    @Test
    void getEpics() {
        Epic epic1 = new Epic("Test getEpics1", "Test getEpics1 description");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Test getEpics2", "Test getEpics2 description");
        taskManager.addEpic(epic2);
        Epic epic3 = new Epic("Test getEpics3", "Test getEpics3 description");
        taskManager.addEpic(epic3);

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают.");
        assertEquals(epic2, epics.get(1), "Эпики не совпадают.");
        assertEquals(epic3, epics.get(2), "Эпики не совпадают.");
    }

    @Test
    void getSubtasks() {
        Epic epic = new Epic("Test addNewSubtask", "Test addNewSubtask description");
        final int epicId = taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Test getSubtasks1", "Test getSubtasks1 description", epicId);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test getSubtasks2", "Test getSubtasks2 description", epicId);
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Test getSubtasks3", "Test getSubtasks3 description", epicId);
        taskManager.addSubtask(subtask3);

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают.");
        assertEquals(subtask3, subtasks.get(2), "Подзадачи не совпадают.");
    }

    @Test
    void getAllTasks() {
        Task task = new Task("Test getAllTasks", "Test getAllTasks description");
        taskManager.addTask(task);
        Epic epic = new Epic("Test getAllTasks", "Test getAllTasks description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test getAllTasks", "Test getAllTasks description", epic.getId());
        taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Подзадачи не совпадают.");
        assertEquals(epic, tasks.get(1), "Подзадачи не совпадают.");
        assertEquals(subtask, tasks.get(2), "Подзадачи не совпадают.");
    }

    @Test
    void deleteAllTasks() {
        Task task = new Task("Test deleteAllTasks", "Test deleteAllTasks description");
        taskManager.addTask(task);
        Epic epic = new Epic("Test deleteAllTasks", "Test deleteAllTasks description");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test deleteAllTasks", "Test deleteAllTasks description", epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.deleteAllTasks();

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(0, tasks.size(), "Задачи не удалены");
    }

    @Test
    void getTask() {
        Task task = new Task("Test deleteAllTasks", "Test deleteAllTasks description");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void getSubtask() {
        Epic epic = new Epic("Test addNewSubtask", "Test addNewSubtask description");
        final int epicId = taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
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
        final Task task1 = new Task("Test updateTask1", "Test updateTask1 description");
        final int taskId = taskManager.addTask(task1);
        final Task savedTask1 = taskManager.getTask(taskId);

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(task1, savedTask1, "Задачи не совпадают.");

        final Task task2 = new Task("Test updateTask2", "Test updateTask2 description", taskId);
        taskManager.updateTask(task2);
        final Task savedTask2 = taskManager.getTask(taskId);

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        final Epic epic1 = new Epic("Test updateEpic1", "Test updateEpic1 description");
        final int epicId = taskManager.addEpic(epic1);
        final Epic savedEpic1 = taskManager.getEpic(epicId);

        assertNotNull(savedEpic1, "Эпик не найден.");
        assertEquals(epic1, savedEpic1, "Эпики не совпадают.");

        final Epic epic2 = new Epic("Test updateEpic2", "Test updateEpic2 description", epicId);
        taskManager.updateEpic(epic2);
        final Epic savedEpic2 = taskManager.getEpic(epicId);

        assertNotNull(savedEpic2, "Эпик не найден.");
        assertEquals(epic2, savedEpic2, "Эпики не совпадают.");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Test updateSubtask", "Test updateSubtask description");
        final int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask("Test updateSubtask1", "Test updateSubtask1 description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask1);
        final Subtask savedSubtask1 = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask1, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают.");

        final Subtask subtask2 = new Subtask("Test updateSubtask2", "Test updateSubtask2 description", subtaskId, epicId);
        taskManager.updateSubtask(subtask2);
        final Subtask savedSubtask2 = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают.");
    }

    @Test
    void deleteTask() {
        Task task = new Task("Test getAllTasks", "Test getAllTasks description");
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test getAllTasks", "Test getAllTasks description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test getAllTasks", "Test getAllTasks description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        taskManager.deleteTask(taskId);
        taskManager.deleteTask(epicId);
        taskManager.deleteTask(subtaskId);

        final Task savedTask = taskManager.getTask(taskId);
        final Epic savedEpic = taskManager.getEpic(epicId);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNull(savedTask, "Задача найдена.");
        assertNull(savedEpic, "Эпик найден.");
        assertNull(savedSubtask, "Подзадача найдена.");
        assertNotEquals(task, savedTask, "Задачи не совпадают.");
        assertNotEquals(epic, savedEpic, "Эпики не совпадают.");
        assertNotEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void getZeroSubtaskByEpicId() {
        Epic epic = new Epic("Test getZeroSubtaskByEpicId", "Test getZeroSubtaskByEpicId description");
        final int epicId = taskManager.addEpic(epic);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void getOneSubtaskByEpicId() {
        Epic epic = new Epic("Test getSubtasksByEpicId", "Test getSubtasksByEpicId description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test getSubtasksByEpicId", "Test getSubtasksByEpicId description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getTwoSubtasksByEpicId() {
        Epic epic = new Epic("Test getTwoSubtasksByEpicId", "Test getTwoSubtasksByEpicId description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Test getTwoSubtasksByEpicId1", "Test getTwoSubtasksByEpicId1 description", epicId);
        final int subtaskId1 = taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test getTwoSubtasksByEpicId2", "Test getTwoSubtasksByEpicId2 description", epicId);
        final int subtaskId2 = taskManager.addSubtask(subtask2);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают.");
    }

    @Test
    void getStatusForNew() {
        Task task = new Task("Test getStatusForNew", "Test getStatusForNew description");
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test getStatusForNew", "Test getStatusForNew description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test getStatusForNew", "Test getStatusForNew description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        final Status taskStatus = taskManager.getStatus(taskId);
        final Status epicStatus = taskManager.getStatus(epicId);
        final Status subtaskStatus = taskManager.getStatus(subtaskId);

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(NEW, taskStatus, "Неверный статус у задачи");
        assertEquals(NEW, epicStatus, "Неверный статус у эпика");
        assertEquals(NEW, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void getStatusForInProgress() {
        Task task = new Task("Test getStatusForInProgress", "Test getStatusForInProgress description", IN_PROGRESS);
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test getStatusForInProgress", "Test getStatusForInProgress description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test getStatusForInProgress", "Test getStatusForInProgress description", IN_PROGRESS, epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        final Status taskStatus = taskManager.getStatus(taskId);
        final Status epicStatus = taskManager.getStatus(epicId);
        final Status subtaskStatus = taskManager.getStatus(subtaskId);

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(IN_PROGRESS, taskStatus, "Неверный статус у задачи");
        assertEquals(IN_PROGRESS, epicStatus, "Неверный статус у эпика");
        assertEquals(IN_PROGRESS, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void getStatusForDone() {
        Task task = new Task("Test getStatusForDone", "Test getStatusForDone description", DONE);
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test getStatusForDone", "Test getStatusForDone description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test getStatusForDone", "Test getStatusForDone description", DONE, epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        final Status taskStatus = taskManager.getStatus(taskId);
        final Status epicStatus = taskManager.getStatus(epicId);
        final Status subtaskStatus = taskManager.getStatus(subtaskId);

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(DONE, taskStatus, "Неверный статус у задачи");
        assertEquals(DONE, epicStatus, "Неверный статус у эпика");
        assertEquals(DONE, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void setStatusForNew() {
        Task task = new Task("Test setStatusForNew", "Test setStatusForNew description", DONE);
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test setStatusForNew", "Test setStatusForNew description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test setStatusForNew", "Test setStatusForNew description", DONE, epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        taskManager.setStatus(taskId, NEW);
        final Status taskStatus = taskManager.getStatus(taskId);
        taskManager.setStatus(subtaskId, NEW);
        final Status epicStatus = taskManager.getStatus(epicId);
        final Status subtaskStatus = taskManager.getStatus(subtaskId);

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(NEW, taskStatus, "Неверный статус у задачи");
        assertEquals(NEW, epicStatus, "Неверный статус у эпика");
        assertEquals(NEW, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void setStatusForInProgress() {
        Task task = new Task("Test setStatusForInProgress", "Test setStatusForInProgress description", DONE);
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test setStatusForInProgress", "Test setStatusForInProgress description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test setStatusForInProgress", "Test setStatusForInProgress description", DONE, epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        taskManager.setStatus(taskId, IN_PROGRESS);
        final Status taskStatus = taskManager.getStatus(taskId);
        taskManager.setStatus(subtaskId, IN_PROGRESS);
        final Status epicStatus = taskManager.getStatus(epicId);
        final Status subtaskStatus = taskManager.getStatus(subtaskId);

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(IN_PROGRESS, taskStatus, "Неверный статус у задачи");
        assertEquals(IN_PROGRESS, epicStatus, "Неверный статус у эпика");
        assertEquals(IN_PROGRESS, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void setStatusForDone() {
        Task task = new Task("Test setStatusForDone", "Test setStatusForDone description");
        final int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Test setStatusForDone", "Test setStatusForDone description");
        final int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test setStatusForDone", "Test setStatusForDone description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);

        taskManager.setStatus(taskId, DONE);
        final Status taskStatus = taskManager.getStatus(taskId);
        taskManager.setStatus(subtaskId, DONE);
        final Status epicStatus = taskManager.getStatus(epicId);
        final Status subtaskStatus = taskManager.getStatus(subtaskId);

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(DONE, taskStatus, "Неверный статус у задачи");
        assertEquals(DONE, epicStatus, "Неверный статус у эпика");
        assertEquals(DONE, subtaskStatus, "Неверный статус у подзадачи");
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
    void getIdCounter() {
        final int idCounter1 = taskManager.getIdCounter();

        Task task = new Task("Test getIdCounter", "Test getIdCounter description");
        final int taskId = taskManager.addTask(task);
        final int idCounter2 = taskManager.getIdCounter();

        Epic epic = new Epic("Test getIdCounter", "Test getIdCounter description");
        final int epicId = taskManager.addEpic(epic);
        final int idCounter3 = taskManager.getIdCounter();

        Subtask subtask = new Subtask("Test getIdCounter", "Test getIdCounter description", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final int idCounter4 = taskManager.getIdCounter();

        assertEquals(1, idCounter1, "Неверный счётчик");
        assertEquals(2, idCounter2, "Неверный счётчик");
        assertEquals(3, idCounter3, "Неверный счётчик");
        assertEquals(4, idCounter4, "Неверный счётчик");
    }
}