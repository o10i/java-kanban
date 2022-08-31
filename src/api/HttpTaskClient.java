package api;

import com.google.gson.Gson;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskClient {
    private static final Gson gson = new Gson();
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI uTasksTask = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest gTasksTask = HttpRequest.newBuilder().uri(uTasksTask).GET().build();
        HttpResponse<String> rGTasksTask = client.send(gTasksTask, HttpResponse.BodyHandlers.ofString());

        String jTasksTask = gson.toJson(new Task("New Task", "Description New Task"));
        final HttpRequest.BodyPublisher bTasksTask = HttpRequest.BodyPublishers.ofString(jTasksTask);
        HttpRequest pTasksTask = HttpRequest.newBuilder().uri(uTasksTask).POST(bTasksTask).build();
        HttpResponse<String> rPTasksTask = client.send(pTasksTask, HttpResponse.BodyHandlers.ofString());

        URI uTasksTaskId = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest gTasksTaskId = HttpRequest.newBuilder().uri(uTasksTaskId).GET().build();
        HttpResponse<String> rGTasksTaskId = client.send(gTasksTaskId, HttpResponse.BodyHandlers.ofString());
    }
}
