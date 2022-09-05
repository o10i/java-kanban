package tests;

import managers.task.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setUp() {
        taskManager = new FileBackedTasksManager(filename);
    }

    @Test
    void saveEmptyTasksFile() throws IOException {
        taskManager.save();
        String content = Files.readString(Path.of(filename));
        assertEquals("id,type,name,status,description,duration,startTime,epic\n\n", content, "Список задач не пуст.");
    }

    @Test
    void saveEmptyHistoryFile() throws IOException {
        addTasks();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Epic> epics = restoredManager.getEpics();
        final List<Subtask> subtasks = restoredManager.getSubtasks();
        final List<Task> history = restoredManager.getHistory();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подадачи на возвращаются.");
        assertNotNull(history, "История на возвращается.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(0, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    void saveOnlyEpicFile() throws IOException {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Epic> epics = restoredManager.getEpics();
        final List<Subtask> subtasks = restoredManager.getSubtasks();
        final List<Task> history = restoredManager.getHistory();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подадачи на возвращаются.");
        assertNotNull(history, "История на возвращается.");
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(1, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    void loadFromEmptyTasksFile() throws IOException {
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Epic> epics = restoredManager.getEpics();
        final List<Subtask> subtasks = restoredManager.getSubtasks();
        final List<Task> history = restoredManager.getHistory();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подадачи на возвращаются.");
        assertNotNull(history, "История на возвращается.");
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        assertEquals(0, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(0, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    void loadFromOnlyEpicFile() throws IOException {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Epic> epics = restoredManager.getEpics();
        final List<Subtask> subtasks = restoredManager.getSubtasks();
        final List<Task> history = restoredManager.getHistory();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подадачи на возвращаются.");
        assertNotNull(history, "История на возвращается.");
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(1, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    void loadFromEmptyHistoryFile() throws IOException {
        addTasks();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Epic> epics = restoredManager.getEpics();
        final List<Subtask> subtasks = restoredManager.getSubtasks();
        final List<Task> history = restoredManager.getHistory();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(epics, "Эпики на возвращаются.");
        assertNotNull(subtasks, "Подадачи на возвращаются.");
        assertNotNull(history, "История на возвращается.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(0, history.size(), "Неверное количество элементов в истории.");
    }
}