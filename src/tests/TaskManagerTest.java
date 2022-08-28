package tests;

import managers.task.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.enums.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected static int idCounter = 0;
    protected final String filename = "history.csv";
    protected T taskManager;

    protected static Task newTask(Status status) {
        idCounter++;
        return new Task("Test Task" + idCounter, "Test Task description" + idCounter, status, idCounter, LocalDateTime.of(2022, idCounter, 14 + idCounter, idCounter, 0));
    }

    protected static Epic newEpic() {
        idCounter++;
        return new Epic("Test Epic" + idCounter, "Test Epic description" + idCounter);
    }

    protected static Subtask newSubtask(Status status, int epicId) {
        idCounter++;
        return new Subtask("Test Subtasks" + idCounter, "Test Subtasks description" + idCounter, status, idCounter, LocalDateTime.of(2022, idCounter, idCounter, idCounter, 0), epicId);
    }

    @Test
    void getTasks() {
        Task task1 = newTask(NEW);
        taskManager.addTask(task1);

        Task task2 = newTask(NEW);
        taskManager.addTask(task2);

        Task task3 = newTask(NEW);
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
        Epic epic1 = newEpic();
        taskManager.addEpic(epic1);

        Epic epic2 = newEpic();
        taskManager.addEpic(epic2);

        Epic epic3 = newEpic();
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
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = newSubtask(NEW, epic.getId());
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
        Task task = newTask(NEW);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
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
        Task task = newTask(NEW);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.deleteAllTasks();

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(0, tasks.size(), "Задачи не удалены");
    }

    @Test
    void getTask() {
        Task task = newTask(NEW);
        taskManager.addTask(task);

        final Task savedTask = taskManager.getTasks().get(0);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getEpic() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpics().get(0);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void getSubtask() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtasks().get(0);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void addNewTask() {
        Task task = newTask(NEW);
        taskManager.addTask(task);

        final Task savedTask = taskManager.getTasks().get(0);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void addNewSubtask() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void updateTask() {
        Task task1 = newTask(NEW);
        taskManager.addTask(task1);

        Task savedTask1 = taskManager.getTask(task1.getId());

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(task1, savedTask1, "Задачи не совпадают.");

        Task task2 = newTask(NEW);
        task2.setId(task1.getId());

        taskManager.updateTask(task2);

        Task savedTask2 = taskManager.getTask(task1.getId());

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        Epic epic1 = newEpic();
        taskManager.addEpic(epic1);

        Epic savedEpic1 = taskManager.getEpic(epic1.getId());

        assertNotNull(savedEpic1, "Эпик не найден.");
        assertEquals(epic1, savedEpic1, "Эпики не совпадают.");

        Epic epic2 = newEpic();
        epic2.setId(epic1.getId());

        taskManager.updateEpic(epic2);

        Epic savedEpic2 = taskManager.getEpic(epic1.getId());

        assertNotNull(savedEpic2, "Эпик не найден.");
        assertEquals(epic2, savedEpic2, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(savedEpic2, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void updateSubtask() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask savedSubtask1 = taskManager.getSubtask(subtask1.getId());

        assertNotNull(savedSubtask1, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают.");

        Subtask subtask2 = newSubtask(NEW, epic.getId());
        subtask2.setId(subtask1.getId());
        taskManager.updateSubtask(subtask2);

        Subtask savedSubtask2 = taskManager.getSubtask(subtask1.getId());

        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask2, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void deleteTask() {
        Task task = newTask(NEW);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        taskManager.deleteTask(task.getId());
        taskManager.deleteTask(epic.getId());
        taskManager.deleteTask(subtask.getId());

        final List<Task> tasksAfterRemove = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(0, tasksAfterRemove.size(), "Неверное количество задач.");

        Task savedTask = taskManager.getTask(task.getId());
        Epic savedEpic = taskManager.getEpic(epic.getId());
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNull(savedTask, "Задача не удалена.");
        assertNull(savedEpic, "Эпик не удалён.");
        assertNull(savedSubtask, "Подзадача не удалена.");
    }

    @Test
    void getZeroSubtaskByEpicId() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void getOneSubtaskByEpicId() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getTwoSubtasksByEpicId() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask2);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают.");
    }

    @Test
    void getStatusForNew() {
        Task task = newTask(NEW);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        Status taskStatus = taskManager.getStatus(task.getId());
        Status epicStatus = taskManager.getStatus(epic.getId());
        Status subtaskStatus = taskManager.getStatus(subtask.getId());

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(NEW, taskStatus, "Неверный статус у задачи");
        assertEquals(NEW, epicStatus, "Неверный статус у эпика");
        assertEquals(NEW, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void getStatusForInProgress() {
        Task task = newTask(IN_PROGRESS);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        Status taskStatus = taskManager.getStatus(task.getId());
        Status epicStatus = taskManager.getStatus(epic.getId());
        Status subtaskStatus = taskManager.getStatus(subtask.getId());

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(IN_PROGRESS, taskStatus, "Неверный статус у задачи");
        assertEquals(IN_PROGRESS, epicStatus, "Неверный статус у эпика");
        assertEquals(IN_PROGRESS, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void getStatusForDone() {
        Task task = newTask(DONE);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(DONE, epic.getId());
        taskManager.addSubtask(subtask);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        Status taskStatus = taskManager.getStatus(task.getId());
        Status epicStatus = taskManager.getStatus(epic.getId());
        Status subtaskStatus = taskManager.getStatus(subtask.getId());

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(DONE, taskStatus, "Неверный статус у задачи");
        assertEquals(DONE, epicStatus, "Неверный статус у эпика");
        assertEquals(DONE, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void setStatusForNew() {
        Task task = newTask(DONE);
        taskManager.addTask(task);
        Epic epic = newEpic();
        taskManager.addEpic(epic);
        Subtask subtask = newSubtask(DONE, epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.setStatus(task.getId(), NEW);
        final Status taskStatus = taskManager.getStatus(task.getId());
        taskManager.setStatus(subtask.getId(), NEW);
        final Status epicStatus = taskManager.getStatus(epic.getId());
        final Status subtaskStatus = taskManager.getStatus(subtask.getId());

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(NEW, taskStatus, "Неверный статус у задачи");
        assertEquals(NEW, epicStatus, "Неверный статус у эпика");
        assertEquals(NEW, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void setStatusForInProgress() {
        Task task = newTask(NEW);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.setStatus(task.getId(), IN_PROGRESS);
        Status taskStatus = taskManager.getStatus(task.getId());

        taskManager.setStatus(subtask.getId(), IN_PROGRESS);
        Status subtaskStatus = taskManager.getStatus(subtask.getId());

        Status epicStatus = taskManager.getStatus(epic.getId());

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(IN_PROGRESS, taskStatus, "Неверный статус у задачи");
        assertEquals(IN_PROGRESS, epicStatus, "Неверный статус у эпика");
        assertEquals(IN_PROGRESS, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void setStatusForDone() {
        Task task = newTask(NEW);
        taskManager.addTask(task);

        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.setStatus(task.getId(), DONE);
        Status taskStatus = taskManager.getStatus(task.getId());

        taskManager.setStatus(subtask.getId(), DONE);
        Status subtaskStatus = taskManager.getStatus(subtask.getId());

        Status epicStatus = taskManager.getStatus(epic.getId());

        assertNotNull(taskStatus, "Отсутствует статус у задачи");
        assertNotNull(epicStatus, "Отсутствует статус у эпика");
        assertNotNull(subtaskStatus, "Отсутствует статус у подзадачи");
        assertEquals(DONE, taskStatus, "Неверный статус у задачи");
        assertEquals(DONE, epicStatus, "Неверный статус у эпика");
        assertEquals(DONE, subtaskStatus, "Неверный статус у подзадачи");
    }

    @Test
    void determineEpicStatusForNull() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpic(epic.getId());

        taskManager.determineEpicStatus(epic.getId());
        Status status = savedEpic.getStatus();

        assertEquals(0, taskManager.getSubtasks().size(), "Список подзадач не пуст.");

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNew() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForDone() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(DONE, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(DONE, epic.getId());
        taskManager.addSubtask(subtask2);

        final List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(DONE, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNewAndDone() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(DONE, epic.getId());
        taskManager.addSubtask(subtask2);

        final List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(epic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForInProgress() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask2);

        final List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    @Test
    void getIdCounter() {
        int idCounter1 = taskManager.getIdCounter();

        Task task = newTask(NEW);
        taskManager.addTask(task);
        int idCounter2 = taskManager.getIdCounter();

        Epic epic = newEpic();
        taskManager.addEpic(epic);
        int idCounter3 = taskManager.getIdCounter();

        Subtask subtask = newSubtask(NEW, epic.getId());
        taskManager.addSubtask(subtask);
        int idCounter4 = taskManager.getIdCounter();

        assertEquals(1, idCounter1, "Неверный счётчик");
        assertEquals(2, idCounter2, "Неверный счётчик");
        assertEquals(3, idCounter3, "Неверный счётчик");
        assertEquals(4, idCounter4, "Неверный счётчик");
    }
}