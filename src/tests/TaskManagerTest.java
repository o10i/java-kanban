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

    protected static Task newTask() {
        idCounter++;
        return new Task("Test Task" + idCounter, NEW, "Test Task description" + idCounter, idCounter, LocalDateTime.of(2022, idCounter, 14 + idCounter, idCounter, 0));
    }

    protected static Epic newEpic() {
        idCounter++;
        return new Epic("Test Epic" + idCounter, "Test Epic description" + idCounter);
    }

    protected static Subtask newSubtask(int epicId) {
        idCounter++;
        return new Subtask("Test Subtasks" + idCounter, NEW, "Test Subtasks description" + idCounter, idCounter, LocalDateTime.of(2022, idCounter, idCounter, idCounter, 0), epicId);
    }

    @Test
    void getTasks() {
        Task task1 = newTask();
        taskManager.addTask(task1);

        Task task2 = newTask();
        taskManager.addTask(task2);

        Task task3 = newTask();
        taskManager.addTask(task3);

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают.");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают.");
    }

    @Test
    void getSubtasks() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask3);

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают.");
        assertEquals(subtask3, subtasks.get(2), "Подзадачи не совпадают.");
    }

    @Test
    void getEpics() {
        Epic epic1 = newEpic();
        taskManager.addEpic(epic1);

        Epic epic2 = newEpic();
        taskManager.addEpic(epic2);

        Epic epic3 = newEpic();
        taskManager.addEpic(epic3);

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают.");
        assertEquals(epic2, epics.get(1), "Эпики не совпадают.");
        assertEquals(epic3, epics.get(2), "Эпики не совпадают.");
    }

    @Test
    void getTaskById() {
        Task task = newTask();
        taskManager.addTask(task);

        Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getSubtaskById() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(epic.getId());
        taskManager.addSubtask(subtask);

        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void getEpicById() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void addNewTask() {
        Task task = newTask();
        taskManager.addTask(task);

        Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubtask() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(epic.getId());
        taskManager.addSubtask(subtask);

        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void updateTask() {
        Task task1 = newTask();
        taskManager.addTask(task1);

        Task savedTask1 = taskManager.getTaskById(task1.getId());

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(task1, savedTask1, "Задачи не совпадают.");

        Task task2 = newTask();
        task2.setId(task1.getId());

        taskManager.updateTask(task2);

        Task savedTask2 = taskManager.getTaskById(task1.getId());

        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateSubtask() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask savedSubtask1 = taskManager.getSubtaskById(subtask1.getId());

        assertNotNull(savedSubtask1, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают.");

        Subtask subtask2 = newSubtask(epic.getId());
        subtask2.setId(subtask1.getId());

        taskManager.updateSubtask(subtask2);

        Subtask savedSubtask2 = taskManager.getSubtaskById(subtask1.getId());

        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают.");

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask2, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void updateEpic() {
        Epic epic1 = newEpic();
        taskManager.addEpic(epic1);

        Epic savedEpic1 = taskManager.getEpicById(epic1.getId());

        assertNotNull(savedEpic1, "Эпик не найден.");
        assertEquals(epic1, savedEpic1, "Эпики не совпадают.");

        Epic epic2 = newEpic();
        epic2.setId(epic1.getId());

        taskManager.updateEpic(epic2);

        Epic savedEpic2 = taskManager.getEpicById(epic1.getId());

        assertNotNull(savedEpic2, "Эпик не найден.");
        assertEquals(epic2, savedEpic2, "Эпики не совпадают.");

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(savedEpic2, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void deleteTaskById() {
        Task task = newTask();
        taskManager.addTask(task);

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");

        taskManager.deleteTaskById(task.getId());

        List<Task> tasksAfterRemove = taskManager.getTasks();

        assertNotNull(tasksAfterRemove, "Задачи не возвращаются.");
        assertEquals(0, tasksAfterRemove.size(), "Неверное количество задач.");

        Task savedTask = taskManager.getTaskById(task.getId());

        assertNull(savedTask, "Задача не удалена.");
    }

    @Test
    void deleteSubtaskById() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(epic.getId());
        taskManager.addSubtask(subtask);

        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Epic> epics = taskManager.getEpics();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        taskManager.deleteSubtaskById(subtask.getId());

        List<Subtask> subtasksAfterRemove = taskManager.getSubtasks();
        List<Epic> epicsAfterRemove = taskManager.getEpics();

        assertNotNull(subtasksAfterRemove, "Подзадачи не возвращаются.");
        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertEquals(0, subtasksAfterRemove.size(), "Неверное количество подзадач.");
        assertEquals(1, epicsAfterRemove.size(), "Неверное количество эпиков.");

        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());
        Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNull(savedSubtask, "Подзадача не удалена.");
        assertNotNull(savedEpic, "Эпик удалён.");
    }

    @Test
    void deleteEpicWithSubtaskById() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(epic.getId());
        taskManager.addSubtask(subtask);

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();


        assertNotNull(epics, "Эпики не возвращаются.");
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");

        taskManager.deleteEpicById(epic.getId());

        List<Epic> epicsAfterRemove = taskManager.getEpics();
        List<Subtask> subtasksAfterRemove = taskManager.getSubtasks();

        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertNotNull(subtasksAfterRemove, "Подзадачи не возвращаются.");
        assertEquals(0, epicsAfterRemove.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasksAfterRemove.size(), "Неверное количество подзадач.");

        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());
        Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNull(savedEpic, "Эпик не удалён.");
        assertNull(savedSubtask, "Подзадача не удалена.");
    }

    @Test
    public void deleteAllTasks() {
        Task task1 = newTask();
        taskManager.addTask(task1);

        Task task2 = newTask();
        taskManager.addTask(task2);

        Task task3 = newTask();
        taskManager.addTask(task3);

        List<Task> tasks = taskManager.getTasks();

        assertEquals(3, tasks.size(), "Неверное количество задач.");

        taskManager.deleteAllTasks();

        List<Task> tasksAfterRemove = taskManager.getTasks();

        assertNotNull(tasksAfterRemove, "Задачи не возвращаются.");
        assertEquals(0, tasksAfterRemove.size(), "Неверное количество задач.");
    }

    @Test
    public void deleteAllSubtasks() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask3);

        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Epic> epics = taskManager.getEpics();

        assertEquals(3, subtasks.size(), "Неверное количество задач.");
        assertEquals(1, epics.size(), "Неверное количество задач.");

        taskManager.deleteAllSubtasks();

        List<Subtask> subtasksAfterRemove = taskManager.getSubtasks();
        List<Epic> epicsAfterRemove = taskManager.getEpics();

        assertNotNull(subtasksAfterRemove, "Подзадачи не возвращаются.");
        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertEquals(0, subtasksAfterRemove.size(), "Неверное количество подзадач.");
        assertEquals(1, epicsAfterRemove.size(), "Неверное количество эпиков.");
    }

    @Test
    public void deleteAllEpics() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество задач.");

        taskManager.deleteAllEpics();

        List<Epic> epicsAfterRemove = taskManager.getEpics();

        assertNotNull(epicsAfterRemove, "Эпики не возвращаются.");
        assertEquals(0, epicsAfterRemove.size(), "Неверное количество эпиков.");
    }

    @Test
    void determineEpicStatusForNull() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpicById(epic.getId());

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

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(NEW, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForNewAndDone() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(epic.getId());
        subtask2.setStatus(DONE);
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();

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

        Subtask subtask1 = newSubtask(epic.getId());
        subtask1.setStatus(IN_PROGRESS);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(epic.getId());
        subtask2.setStatus(IN_PROGRESS);
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(IN_PROGRESS, status, "Статус неверный.");
    }

    @Test
    void determineEpicStatusForDone() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(epic.getId());
        subtask1.setStatus(DONE);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(epic.getId());
        subtask2.setStatus(DONE);
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();

        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        Epic savedEpic = epics.get(0);

        taskManager.determineEpicStatus(savedEpic.getId());
        Status status = savedEpic.getStatus();

        assertNotNull(status, "Статус не установлен.");
        assertEquals(DONE, status, "Статус неверный.");
    }

    @Test
    void getZeroEpicSubtasks() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");

        List<Subtask> subtasks = taskManager.getEpicSubtasks(epic.getId());

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void getOneEpicSubtasks() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask = newSubtask(epic.getId());
        taskManager.addSubtask(subtask);

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");

        List<Subtask> returnedSubtasks = taskManager.getEpicSubtasks(epic.getId());

        assertNotNull(returnedSubtasks, "Подзадачи на возвращаются.");
        assertEquals(1, returnedSubtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, returnedSubtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getTwoEpicSubtasks() {
        Epic epic = newEpic();
        taskManager.addEpic(epic);

        Subtask subtask1 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = newSubtask(epic.getId());
        taskManager.addSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");

        List<Subtask> returnedSubtasks = taskManager.getEpicSubtasks(epic.getId());

        assertNotNull(returnedSubtasks, "Подзадачи на возвращаются.");
        assertEquals(2, returnedSubtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, returnedSubtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, returnedSubtasks.get(1), "Подзадачи не совпадают.");
    }
}