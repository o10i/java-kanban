package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Gson gson = new Gson();
    private static final TaskManager taskManager = new Managers().getDefault();

    static void fillTasks() {
        Task task1 = new Task("Купить автомобиль", "Для семейных целей", 60, LocalDateTime.of(2022, 1, 1, 0, 0));
        taskManager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая", 60, LocalDateTime.of(2022, 1, 1, 1, 0));
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы");
        taskManager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", 30, LocalDateTime.of(2022, 12, 31, 23, 30), epic1.getId());
        taskManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", 30, LocalDateTime.of(2022, 1, 1, 2, 0), epic1.getId());
        taskManager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Черешня", "Собрать черешню", 30, LocalDateTime.of(2022, 1, 1, 2, 30), epic1.getId());
        taskManager.addSubtask(subtask13);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию");
        taskManager.addEpic(epic2);

        // создание истории просмотров
        taskManager.getEpicById(7);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(7);
        taskManager.getTaskById(2);
        taskManager.getSubtaskById(6);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
    }

    public static void main(String[] args) throws IOException {
        fillTasks();

        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "";

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET":
                    switch (path) {
                        case "/tasks/task/":
                            if (query == null) {
                                response = gson.toJson(taskManager.getTasks());
                                exchange.sendResponseHeaders(200, 0);
                            } else if (query.contains("id=")) {
                                System.out.println(2);
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                response = gson.toJson(taskManager.getTaskById(id));
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        case "/tasks/subtask/":
                            if (query == null) {
                                response = gson.toJson(taskManager.getSubtasks());
                                exchange.sendResponseHeaders(200, 0);
                            } else if (query.contains("id=")) {
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                response = gson.toJson(taskManager.getSubtaskById(id));
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        case "/tasks/epic/":
                            if (query == null) {
                                response = gson.toJson(taskManager.getEpics());
                                exchange.sendResponseHeaders(200, 0);
                            } else if (query.contains("id=")) {
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                response = gson.toJson(taskManager.getEpicById(id));
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        case "/tasks/subtask/epic/":
                            if (query.contains("id=")) {
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                response = gson.toJson(taskManager.getEpicSubtasks(id));
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        case "/tasks/history/":
                            response = gson.toJson(taskManager.getHistory());
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        case "/tasks/":
                            response = gson.toJson(taskManager.getPrioritizedTasks());
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        default:
                            response = "Некорректный метод!";
                            exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    switch (path) {
                        case "/tasks/task/":
                            Task task = gson.fromJson(body, Task.class);

                            if (taskManager.getTaskById(task.getId()) != null) {
                                taskManager.updateTask(task);
                                response = "Задача обновлена";
                            } else {
                                taskManager.addTask(task);
                                response = "Задача добавлена";
                            }

                            exchange.sendResponseHeaders(201, 0);
                            break;
                        case "/tasks/subtask/":
                            Subtask subtask = gson.fromJson(body, Subtask.class);

                            if (subtask.getId() > 0) {
                                taskManager.updateSubtask(subtask);
                                response = "Задача обновлена";
                            } else {
                                taskManager.addSubtask(subtask);
                                response = "Задача добавлена";
                            }

                            exchange.sendResponseHeaders(201, 0);
                            break;
                        case "/tasks/epic/":
                            Epic epic = gson.fromJson(body, Epic.class);

                            if (epic.getId() > 0) {
                                taskManager.updateEpic(epic);
                                response = "Задача обновлена";
                            } else {
                                taskManager.addEpic(epic);
                                response = "Задача добавлена";
                            }

                            exchange.sendResponseHeaders(201, 0);
                            break;
                        default:
                            response = "Некорректный метод!";
                            exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                case "DELETE":
                    switch (path) {
                        case "/tasks/task/":
                            if (query == null) {
                                taskManager.deleteAllTasks();
                                response = "Все задачи удалены";
                                exchange.sendResponseHeaders(200, 0);
                            } else if (query.contains("id=")) {
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                taskManager.deleteTaskById(id);
                                response = "Задача с id=" + id + " удалена, либо была удалена прежде";
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        case "/tasks/subtask/":
                            if (query == null) {
                                taskManager.deleteAllSubtasks();
                                response = "Все подзадачи удалены";
                                exchange.sendResponseHeaders(200, 0);
                            } else if (query.contains("id=")) {
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                taskManager.deleteSubtaskById(id);
                                response = "Подзадача с id=" + id + " удалена, либо была удалена прежде";
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        case "/tasks/epic/":
                            if (query == null) {
                                taskManager.deleteAllEpics();
                                response = "Все эпики и подзадачи удалены";
                                exchange.sendResponseHeaders(200, 0);
                            } else if (query.contains("id=")) {
                                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                                taskManager.deleteEpicById(id);
                                response = "Эпик с id=" + id + " удалён, либо был удалён прежде";
                                exchange.sendResponseHeaders(200, 0);
                            }
                            break;
                        default:
                            response = "Некорректный метод!";
                            exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                default:
                    response = "Некорректный метод!";
                    exchange.sendResponseHeaders(404, 0);
            }

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
