import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import managers.task.FileBackedTasksManager;
import managers.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {
    private final Gson gson = new Gson();
    private TaskManager taskManager;
    private HttpTaskServer server;
    private HttpClient client;
    private Task taskByDefault;
    private Epic epicByDefault;
    private Subtask subtaskByDefault;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new FileBackedTasksManager("src/test/resources/history.csv");
        server = new HttpTaskServer(taskManager);
        client = HttpClient.newHttpClient();
        server.start();
    }

    private void addTasks() {
        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        taskManager.addTask(taskByDefault);
        epicByDefault = new Epic("Test epic", "Test epic description");
        taskManager.addEpic(epicByDefault);
        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        taskManager.addSubtask(subtaskByDefault);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(1, actual.size(), "Неверное количество задач");
        assertEquals(taskByDefault, actual.get(0), "Задачи не совпадают");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Эпики не возвращаются");
        assertEquals(1, actual.size(), "Неверное количество эпиков");
        assertEquals(epicByDefault, actual.get(0), "Эпики не совпадают");
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Подзадачи не возвращаются");
        assertEquals(1, actual.size(), "Неверное количество подзадач");
        assertEquals(subtaskByDefault, actual.get(0), "Подзадачи не совпадают");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Задача не возвращается");
        assertEquals(taskByDefault, actual, "Задача не совпадает");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Epic>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Задача не возвращается");
        assertEquals(epicByDefault, actual, "Задача не совпадает");
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Subtask>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Задача не возвращается");
        assertEquals(subtaskByDefault, actual, "Задача не совпадает");
    }

    @Test
    void getEpicSubtasksById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Подзадачи не возвращаются");
        assertEquals(1, actual.size(), "Неверное количество подзадач");
        assertEquals(subtaskByDefault, actual.get(0), "Подзадачи не совпадают");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        addTasks();

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "История не возвращается");
        assertEquals(3, actual.size(), "Неверное количество задач");
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Set<Task>>() {
        }.getType();
        Set<Task> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(2, actual.size(), "Неверное количество задач");
    }

    @Test
    void postTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");

        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        String json = gson.toJson(taskByDefault);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Неверное количество задач");
    }

    @Test
    void postEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");

        epicByDefault = new Epic("Test epic", "Test epic description");
        String json = gson.toJson(epicByDefault);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков");
    }

    @Test
    void postSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        epicByDefault = new Epic("Test epic", "Test epic description");
        taskManager.addEpic(epicByDefault);
        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        String json = gson.toJson(subtaskByDefault);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/task");

        taskByDefault.setName("Test updatedTask");
        String json = gson.toJson(taskByDefault);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals("Test updatedTask", taskManager.getTaskById(1).getName(), "Неверное название задачи");
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/epic");

        epicByDefault.setName("Test updatedEpic");
        String json = gson.toJson(epicByDefault);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals("Test updatedEpic", taskManager.getEpicById(2).getName(), "Неверное название эпика");
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        subtaskByDefault.setName("Test updatedSubtask");
        String json = gson.toJson(subtaskByDefault);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals("Test updatedSubtask", taskManager.getSubtaskById(3).getName(), "Неверное название подзадачи");
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(0, actual.size(), "Неверное количество задач");
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Эпики не возвращаются");
        assertEquals(0, actual.size(), "Неверное количество эпиков");
    }

    @Test
    void deleteSubtasks() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), type);
        assertNotNull(actual, "Подзадачи не возвращаются");
        assertEquals(0, actual.size(), "Неверное количество подзадач");
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        addTasks();

        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}