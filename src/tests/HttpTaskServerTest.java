package tests;

import api.HttpTaskServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static tasks.enums.Status.NEW;

class HttpTaskServerTest {
    private static HttpTaskServer server;
    private static final String serverURL = "http://localhost:8080";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private static int idCounter = 0;

    private static Task newTask() {
        idCounter++;
        return new Task("Test Task" + idCounter, NEW, "Test Task description" + idCounter, idCounter, LocalDateTime.of(2022, idCounter, 14 + idCounter, idCounter, 0));
    }

    private static Epic newEpic() {
        idCounter++;
        return new Epic("Test Epic" + idCounter, "Test Epic description" + idCounter);
    }

    private static Subtask newSubtask(int epicId) {
        idCounter++;
        return new Subtask("Test Subtasks" + idCounter, NEW, "Test Subtasks description" + idCounter, idCounter, LocalDateTime.of(2022, idCounter, idCounter, idCounter, 0), epicId);
    }

    @BeforeEach
    void setUp() throws IOException {
        idCounter = 0;
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void postTask() throws IOException, InterruptedException {
        Task task = newTask();
        URI url = URI.create(serverURL + "/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void postEpic() throws IOException, InterruptedException {
        Epic epic = newEpic();
        URI url = URI.create(serverURL + "/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void postSubtask() throws IOException, InterruptedException {
        postEpic();
        Subtask subtask = newSubtask(1);
        URI url = URI.create(serverURL + "/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        postTask();
        Task task = newTask();
        task.setId(1);
        URI url = URI.create(serverURL + "/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        postEpic();
        Epic epic = newEpic();
        epic.setId(1);
        URI url = URI.create(serverURL + "/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        postSubtask();
        Subtask subtask = newSubtask(1);
        subtask.setId(2);
        URI url = URI.create(serverURL + "/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        postTask();
        URI url = URI.create(serverURL + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        postEpic();
        URI url = URI.create(serverURL + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        postSubtask();
        URI url = URI.create(serverURL + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        postTask();
        URI url = URI.create(serverURL + "/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        postEpic();
        URI url = URI.create(serverURL + "/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        postSubtask();
        URI url = URI.create(serverURL + "/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getEpicSubtasksById() throws IOException, InterruptedException {
        postSubtask();
        URI url = URI.create(serverURL + "/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        getTaskById();

        URI url = URI.create(serverURL + "/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        postTask();

        URI url = URI.create(serverURL + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        postTask();
        URI url = URI.create(serverURL + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        postEpic();
        URI url = URI.create(serverURL + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void deleteSubtasks() throws IOException, InterruptedException {
        postSubtask();
        URI url = URI.create(serverURL + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        postTask();
        URI url = URI.create(serverURL + "/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        postEpic();
        URI url = URI.create(serverURL + "/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        postSubtask();
        URI url = URI.create(serverURL + "/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}