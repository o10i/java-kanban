package tests;

import http.KVServer;
import managers.task.HTTPTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    private final KVServer server = new KVServer();
    private final String serverURL = "http://localhost:8078";
    private String key;

    HTTPTaskManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        server.start();
        taskManager = new HTTPTaskManager(serverURL);
        key = taskManager.getKey();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void saveEmptyTasksToServer() throws IOException, InterruptedException {
        taskManager.save();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, key);

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void saveEmptyHistoryToServer() throws IOException, InterruptedException {
        addTasks();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, key);

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void saveOnlyEpicToServer() throws IOException, InterruptedException {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, key);

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void loadFromEmptyTasksFromServer() throws IOException, InterruptedException {
        taskManager.save();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, key);

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void loadFromOnlyEpicFromServer() throws IOException, InterruptedException {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, key);

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void loadFromEmptyHistoryFromServer() throws IOException, InterruptedException {
        addTasks();

        HTTPTaskManager restoredManager = HTTPTaskManager.loadFromServer(serverURL, key);

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }
}