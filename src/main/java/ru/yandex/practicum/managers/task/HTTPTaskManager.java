package ru.yandex.practicum.managers.task;

import ru.yandex.practicum.http.KVServer;
import ru.yandex.practicum.http.KVTaskClient;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.enums.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private String key;

    public HTTPTaskManager(String sourceName) throws IOException, InterruptedException {
        this(sourceName, "defaultKey");
    }

    public HTTPTaskManager(String sourceName, String key) throws IOException, InterruptedException {
        super(sourceName);
        this.key = key;
        this.client = new KVTaskClient(this.sourceName);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.nanoTime();

        new KVServer().start();
        TaskManager taskManager = Managers.getDefault();

        fillData(taskManager);

        TaskManager htm = HTTPTaskManager.loadFromServer("http://localhost:8078", "defaultKey");

        System.out.println("\nВосстановленный менеджер равен сохранённому:");
        System.out.println(htm.equals(taskManager));
        System.out.println("\nСписок восстановленных задач:");
        printTasks(htm);
        System.out.println("\nВосстановленная история просмотров:");
        for (Task task : htm.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\nСледующая задача будет создана с id=8");
        Epic epic3 = new Epic("Epic3", "Epic3 description");
        htm.addEpic(epic3);
        System.out.println(htm.getEpicById(8));

        System.out.println("\nСписок задач и подзадач в порядке приоритета:");
        for (Task task : htm.getPrioritizedTasks()) {
            System.out.println(task);
        }

        Task updatedTask = new Task("updatedTask", "updatedTask description", 30, LocalDateTime.now().plusDays(9));
        updatedTask.setId(1);
        htm.updateTask(updatedTask);
        Epic updatedEpic = new Epic("updatedEpic", "updatedEpic description");
        updatedEpic.setId(3);
        htm.updateEpic(updatedEpic);
        Subtask updatedSubtask = new Subtask("updatedSubtask", "updatedSubtask description", 30, LocalDateTime.now().plusDays(10), 3);
        updatedSubtask.setId(4);
        htm.updateSubtask(updatedSubtask);
        System.out.println("\nСписок обновлённых задач id=[1,3,4]:");
        printTasks(htm);
        System.out.println("\nИстория просмотров после обновления задач, не должна меняться (2, 3, 4, 5, 6, 7, 1, 8):");
        for (Task task : htm.getHistory()) {
            System.out.println(task);
        }

        htm.deleteSubtaskById(4);
        System.out.println("\nПроверка изменения полей эпика (id=3) после удаление подзадачи (id=4):");
        System.out.println(htm.getEpicById(3));
        System.out.println("\nИстория просмотров (2, 5, 6, 7, 1, 8, 3):");
        for (Task task : htm.getHistory()) {
            System.out.println(task);
        }

        htm.getSubtaskById(5).setStatus(Status.DONE);
        htm.getSubtaskById(6).setStatus(Status.DONE);
        htm.determineEpicStatus(3);
        System.out.println("\nПроверка изменения статуса эпика (id=3) после изменения статуса его подзадач на DONE:");
        System.out.println(htm.getEpicById(3));

        htm.deleteEpicById(3);
        System.out.println("\nСписок задач (удалён id=3 и его подзадачи id=[5, 6]):");
        printTasks(htm);
        System.out.println("\nИстория просмотров (2, 7, 1, 8):");
        for (Task task : htm.getHistory()) {
            System.out.println(task);
        }

        htm.deleteAllTasks();
        htm.deleteAllEpics();
        System.out.println("\nСписок задач (удалены все задачи):");
        printTasks(htm);
        System.out.println("История просмотров (удалена вся история):");
        System.out.println(htm.getHistory());

        long finish = System.nanoTime();
        System.out.println("\nМетод 'main' выполнен за " + (finish - start) / 1000000 + " миллисекунд");
    }

    public static HTTPTaskManager loadFromServer(String sourceName, String key) throws IOException, InterruptedException {
        HTTPTaskManager htm = new HTTPTaskManager(sourceName, key);
        String content = htm.client.load(key);

        String[] tasksAndHistory = content.split("\n\n");
        String[] tasks = tasksAndHistory[0].split("\n");
        htm.restoreMaps(tasks);
        htm.idCounter = htm.getAllTasksSortedById().stream().map(Task::getId).max(Integer::compareTo).orElse(0);
        if (tasksAndHistory.length > 1) {
            String history = tasksAndHistory[1];
            htm.restoreHistory(history);
        }
        htm.save();
        return htm;
    }

    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append(TABLE_HEADER);
        List<Task> tasks = getAllTasksSortedById();
        for (Task task : tasks) {
            sb.append(toString(task));
            sb.append("\n");
        }
        sb.append("\n");
        sb.append(historyToString(getHistoryManager()));
        try {
            client.put(key, sb.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public KVTaskClient getClient() {
        return client;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
