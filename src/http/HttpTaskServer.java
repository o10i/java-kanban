package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;

        gson = new Gson();

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer(new InMemoryTaskManager()).start();
    }

    private void handleTasks(HttpExchange exchange) {
        try {
            String uri = String.valueOf(exchange.getRequestURI());
            String query = exchange.getRequestURI().getQuery();
            System.out.println("\n" + uri);

            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET": {
                    final String response;
                    if (Pattern.matches("^/tasks/task$", uri)) {
                        response = gson.toJson(taskManager.getTasks());
                        System.out.println("Все задачи возвращены");
                    } else if (Pattern.matches("^/tasks/subtask$", uri)) {
                        response = gson.toJson(taskManager.getSubtasks());
                        System.out.println("Все подзадачи возвращены");
                    } else if (Pattern.matches("^/tasks/epic$", uri)) {
                        response = gson.toJson(taskManager.getEpics());
                        System.out.println("Все эпики возвращены");
                    } else if (Pattern.matches("^/tasks/task/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Task task = taskManager.getTaskById(id);
                        if (task == null) {
                            System.out.println("Задача с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        response = gson.toJson(task);
                        System.out.println("Задача с id=" + id + " возвращена");
                    } else if (Pattern.matches("^/tasks/subtask/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Subtask subtask = taskManager.getSubtaskById(id);
                        if (subtask == null) {
                            System.out.println("Подзадача с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        response = gson.toJson(subtask);
                        System.out.println("Подзадача с id=" + id + " возвращена");
                    } else if (Pattern.matches("^/tasks/epic/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Epic epic = taskManager.getEpicById(id);
                        if (epic == null) {
                            System.out.println("Эпик с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        response = gson.toJson(epic);
                        System.out.println("Эпик с id=" + id + " возвращён");
                    } else if (Pattern.matches("^/tasks/subtask/epic/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Epic epic = taskManager.getEpicById(id);
                        if (epic == null) {
                            System.out.println("Эпик с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        response = gson.toJson(taskManager.getEpicSubtasks(id));
                        System.out.println("Подзадачи эпика с id=" + id + " возвращены");
                    } else if (Pattern.matches("^/tasks/history$", uri)) {
                        response = gson.toJson(taskManager.getHistory());
                        System.out.println("История просмотров возвращена");
                    } else if (Pattern.matches("^/tasks$", uri)) {
                        response = gson.toJson(taskManager.getPrioritizedTasks());
                        System.out.println("Задачи и подзадачи возвращены в порядке приоритета");
                    } else {
                        System.out.println("Неправильный URI: " + uri);
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    sendText(exchange, response);
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/task$", uri)) {
                        Task task = gson.fromJson(readText(exchange), Task.class);
                        if (taskManager.getTaskById(task.getId()) != null) {
                            taskManager.updateTask(task);
                            System.out.println("Задача c id=" + task.getId() + " обновлена");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (taskManager.checkIntersection(task)) {
                            taskManager.addTask(task);
                            System.out.println("Задача добавлена");
                            exchange.sendResponseHeaders(201, 0);
                        } else {
                            System.out.println("Обнаружено пересечение задач");
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask$", uri)) {
                        Subtask subtask = gson.fromJson(readText(exchange), Subtask.class);
                        if (taskManager.getSubtaskById(subtask.getId()) != null) {
                            taskManager.updateSubtask(subtask);
                            System.out.println("Подзадача c id=" + subtask.getId() + " обновлена");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (taskManager.checkIntersection(subtask)) {
                            taskManager.addSubtask(subtask);
                            System.out.println("Подзадача добавлена");
                            exchange.sendResponseHeaders(201, 0);
                        } else {
                            System.out.println("Обнаружено пересечение задач");
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else if (Pattern.matches("^/tasks/epic$", uri)) {
                        Epic epic = gson.fromJson(readText(exchange), Epic.class);
                        if (taskManager.getEpicById(epic.getId()) != null) {
                            taskManager.updateEpic(epic);
                            System.out.println("Эпик c id=" + epic.getId() + " обновлён");
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            taskManager.addEpic(epic);
                            System.out.println("Эпик добавлен");
                            exchange.sendResponseHeaders(201, 0);
                        }
                    } else {
                        System.out.println("Неправильный URI: " + uri);
                        exchange.sendResponseHeaders(400, 0);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/task$", uri)) {
                        taskManager.deleteAllTasks();
                        System.out.println("Все задачи удалены");
                    } else if (Pattern.matches("^/tasks/subtask$", uri)) {
                        taskManager.deleteAllSubtasks();
                        System.out.println("Все подзадачи удалены");
                    } else if (Pattern.matches("^/tasks/epic$", uri)) {
                        taskManager.deleteAllEpics();
                        System.out.println("Все эпики и подзадачи удалены");
                    } else if (Pattern.matches("^/tasks/task/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Task task = taskManager.getTaskById(id);
                        if (task == null) {
                            System.out.println("Задача с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        taskManager.deleteTaskById(id);
                        System.out.println("Задача с id=" + id + " удалена");
                    } else if (Pattern.matches("^/tasks/subtask/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Subtask subtask = taskManager.getSubtaskById(id);
                        if (subtask == null) {
                            System.out.println("Подзадача с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        taskManager.deleteSubtaskById(id);
                        System.out.println("Подзадача с id=" + id + " удалена");
                    } else if (Pattern.matches("^/tasks/epic/\\?id=\\d+$", uri)) {
                        int id = getId(query);
                        Epic epic = taskManager.getEpicById(id);
                        if (epic == null) {
                            System.out.println("Эпик с id=" + id + " отсутствует");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        taskManager.deleteEpicById(id);
                        System.out.println("Эпик с id=" + id + " и его подзадачи удалены");
                    } else {
                        System.out.println("Неправильный URI: " + uri);
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;
                }
                default: {
                    System.out.println("/tasks ждёт GET/POST/DELETE-запросы, а получил: " + method);
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обработке запроса /tasks" + e.getMessage());
        } finally {
            exchange.close();
        }
    }

    private int getId(String query) {
        return Integer.parseInt(query.replaceFirst("id=", ""));
    }

    public void start() {
        System.out.println("Запускаем TaskServer на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("\nОстановили TaskServer на порту " + PORT);
    }

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }
}
