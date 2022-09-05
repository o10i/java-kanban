package tests;

import http.KVServer;
import managers.task.HTTPTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager>  {
    private final KVServer server = new KVServer();
    private final String serverURL = "http://localhost:8078";
    private final String key = "1";

    HTTPTaskManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        server.start();
        taskManager = new HTTPTaskManager(serverURL);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void saveEmptyTasksToServer() throws IOException, InterruptedException {
        taskManager.save();
        String content = taskManager.getClient().load(key);
        assertEquals("id,type,name,status,description,duration,startTime,epic\n\n", content, "Список задач не пуст.");
    }

    @Test
    void saveEmptyHistoryToServer() throws IOException, InterruptedException {
        addTasks();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, "1");
        String content = restoredManager.getClient().load(key);

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
    void saveOnlyEpicToServer() throws IOException, InterruptedException {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, "1");
        String content = restoredManager.getClient().load(key);

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
    void loadFromEmptyTasksFromServer() throws IOException, InterruptedException {
        taskManager.save();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, "1");
        String content = restoredManager.getClient().load(key);

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
    void loadFromOnlyEpicFromServer() throws IOException, InterruptedException {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, "1");
        String content = restoredManager.getClient().load(key);

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
    void loadFromEmptyHistoryFromServer() throws IOException, InterruptedException {
        addTasks();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, "1");
        String content = restoredManager.getClient().load(key);

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