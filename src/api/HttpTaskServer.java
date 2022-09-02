package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.task.FileBackedTasksManager;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final Gson gson = new Gson();
    private final TaskManager taskManager = new FileBackedTasksManager("history.csv");
    private final HttpServer server;

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
    }

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::tasks);
    }

    private void tasks(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                get(exchange);
            } else if ("POST".equals(method)) {
                post(exchange);
            } else if ("DELETE".equals(method)) {
                delete(exchange);
            } else {
                System.out.println("\n/tasks ждёт GET/POST/DELETE-запросы, а получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void get(HttpExchange exchange) throws IOException {
        String response;
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        if ("/tasks/task/".equals(path)) {
            if (query == null) {
                response = gson.toJson(taskManager.getTasks());
                System.out.println("\nВсе задачи успешно возвращены");
            } else if (query.contains("id=")) {
                int id = getId(query);
                Task task = taskManager.getTaskById(id);
                if (task == null) {
                    System.out.println("\nЗадача с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                response = gson.toJson(task);
                System.out.println("\nЗадача с id=" + id + " возвращена");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else if ("/tasks/subtask/".equals(path)) {
            if (query == null) {
                response = gson.toJson(taskManager.getSubtasks());
                System.out.println("\nВсе подзадачи успешно возвращены");
            } else if (query.contains("id=")) {
                int id = getId(query);
                Subtask subtask = taskManager.getSubtaskById(id);
                if (subtask == null) {
                    System.out.println("\nПодзадача с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                response = gson.toJson(subtask);
                System.out.println("\nПодзадача с id=" + id + " возвращена");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else if ("/tasks/epic/".equals(path)) {
            if (query == null) {
                response = gson.toJson(taskManager.getEpics());
                System.out.println("\nВсе эпики успешно возвращены");
            } else if (query.contains("id=")) {
                int id = getId(query);
                Epic epic = taskManager.getEpicById(id);
                if (epic == null) {
                    System.out.println("\nЭпик с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                response = gson.toJson(epic);
                System.out.println("\nЭпик с id=" + id + " возвращён");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else if ("/tasks/subtask/epic/".equals(path)) {
            if (query.contains("id=")) {
                int id = getId(query);
                Epic epic = taskManager.getEpicById(id);
                if (epic == null) {
                    System.out.println("\nЭпик с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                response = gson.toJson(taskManager.getEpicSubtasks(id));
                System.out.println("\nПодзадачи эпика с id=" + id + " возвращены");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else if ("/tasks/history/".equals(path) && query == null) {
            response = gson.toJson(taskManager.getHistory());
            System.out.println("\nИстория просмотров успешно возвращена");
        } else if ("/tasks/".equals(path) && query == null) {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            System.out.println("\nЗадачи и подзадачи в порядке приоритета успешно возвращены");
        } else {
            System.out.println("\nПуть '" + path + "' отсутствует или добавлен лишний запрос");
            exchange.sendResponseHeaders(400, 0);
            return;
        }
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes(UTF_8).length);
        exchange.getResponseBody().write(response.getBytes(UTF_8));
    }

    private void post(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body = new String(exchange.getRequestBody().readAllBytes(), UTF_8);

        if ("/tasks/task/".equals(path)) {
            Task task = gson.fromJson(body, Task.class);
            if (taskManager.getTaskById(task.getId()) != null) {
                taskManager.updateTask(task);
                System.out.println("\nЗадача обновлена");
                exchange.sendResponseHeaders(200, 0);
            } else if (taskManager.checkIntersection(task)) {
                taskManager.addTask(task);
                System.out.println("\nЗадача добавлена");
                exchange.sendResponseHeaders(201, 0);
            } else {
                System.out.println("\nОбнаружено пересечение задач");
                exchange.sendResponseHeaders(400, 0);
            }
        } else if ("/tasks/subtask/".equals(path)) {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (taskManager.getSubtaskById(subtask.getId()) != null) {
                taskManager.updateSubtask(subtask);
                System.out.println("\nПодзадача обновлена");
                exchange.sendResponseHeaders(200, 0);
            } else if (taskManager.checkIntersection(subtask)) {
                taskManager.addSubtask(subtask);
                System.out.println("\nПодзадача добавлена");
                exchange.sendResponseHeaders(201, 0);
            } else {
                System.out.println("\nОбнаружено пересечение задач");
                exchange.sendResponseHeaders(400, 0);
            }
        } else if ("/tasks/epic/".equals(path)) {
            Epic epic = gson.fromJson(body, Epic.class);
            if (taskManager.getEpicById(epic.getId()) != null) {
                taskManager.updateEpic(epic);
                System.out.println("\nЭпик обновлён");
                exchange.sendResponseHeaders(200, 0);
            } else {
                taskManager.addEpic(epic);
                System.out.println("\nЭпик добавлен");
                exchange.sendResponseHeaders(201, 0);
            }
        } else {
            System.out.println("\nПуть '" + path + "' отсутствует");
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void delete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        if ("/tasks/task/".equals(path)) {
            if (query == null) {
                taskManager.deleteAllTasks();
                System.out.println("\nВсе задачи успешно удалены");
            } else if (query.contains("id=")) {
                int id = getId(query);
                Task task = taskManager.getTaskById(id);
                if (task == null) {
                    System.out.println("\nЗадача с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                taskManager.deleteTaskById(id);
                System.out.println("\nЗадача с id=" + id + " удалена");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else if ("/tasks/subtask/".equals(path)) {
            if (query == null) {
                taskManager.deleteAllSubtasks();
                System.out.println("\nВсе подзадачи успешно удалены");
            } else if (query.contains("id=")) {
                int id = getId(query);
                Subtask subtask = taskManager.getSubtaskById(id);
                if (subtask == null) {
                    System.out.println("\nПодзадача с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                taskManager.deleteSubtaskById(id);
                System.out.println("\nПодзадача с id=" + id + " удалена");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else if ("/tasks/epic/".equals(path)) {
            if (query == null) {
                taskManager.deleteAllEpics();
                System.out.println("\nВсе эпики и подзадачи успешно удалены");
            } else if (query.contains("id=")) {
                int id = getId(query);
                Epic epic = taskManager.getEpicById(id);
                if (epic == null) {
                    System.out.println("\nЭпик с id=" + id + " отсутствует");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                taskManager.deleteEpicById(id);
                System.out.println("\nЭпик с id=" + id + " и его подзадачи удалены");
            } else {
                System.out.println("\nНеправильная комбинация пути и запроса");
                exchange.sendResponseHeaders(400, 0);
                return;
            }
        } else {
            System.out.println("\nПуть '" + path + "' отсутствует");
            exchange.sendResponseHeaders(400, 0);
            return;
        }
        exchange.sendResponseHeaders(200, 0);
    }

    private int getId(String query) {
        return Integer.parseInt(query.replaceFirst("id=", ""));
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }
    public void stop() {
        server.stop(1);
    }
}
