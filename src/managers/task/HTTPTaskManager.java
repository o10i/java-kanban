package managers.task;

import http.KVTaskClient;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private int key = 1;

    public HTTPTaskManager(String serverURL) throws IOException, InterruptedException {
        super(serverURL);
        client = new KVTaskClient(serverURL);
    }

/*    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.nanoTime();

        new KVServer().start();
        TaskManager taskManager = Managers.getDefault();

        fillTasks(taskManager);

        System.out.println("\nСписок созданных задач:");
        printTasks(taskManager);

        // создание истории просмотров
        fillHistory(taskManager);
        System.out.println("\nИстория просмотров (5, 4, 3, 7, 6, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        // восстановление из файла
        TaskManager restoredTaskManager = HTTPTaskManager.loadFromServer("http://localhost:8078", "1");

        System.out.println("\nВосстановленный менеджер равен сохранённому:");
        System.out.println(restoredTaskManager.equals(taskManager));
        System.out.println("\nСписок восстановленных задач:");
        printTasks(taskManager);
        System.out.println("\nВосстановленная история просмотров:");
        for (Task task : restoredTaskManager.getHistory()) {
            System.out.println(task);
        }

        // проверка idCounter после восстановления
        System.out.println("\nСледующая задача будет создана с id=8");
        Epic epic3 = new Epic("Epic id=8", "Description Epic id=8");
        taskManager.addEpic(epic3);
        System.out.println(taskManager.getEpicById(8));

        // список задач и подзадач в порядке приоритета
        System.out.println("\nСписок задач и подзадач в порядке приоритета:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        // остановка секундомера
        long finish = System.nanoTime();
        System.out.println("\nМетод 'main' выполнен за " + (finish - start) / 1000000 + " миллисекунд");

        // обновление задач
        Task updatedTask = new Task("Обновлённая задача", "Для проверки", 60, LocalDateTime.of(2022, 1, 1, 0, 0));
        updatedTask.setId(1);
        taskManager.updateTask(updatedTask);
        Epic updatedEpic = new Epic("Обновлённый эпик", "Для проверки");
        updatedEpic.setId(3);
        taskManager.updateEpic(updatedEpic);
        Subtask updatedSubtask = new Subtask("Обновлённая подзадача", "Для проверки", 30, LocalDateTime.of(2022, 12, 31, 23, 30), epic1.getId());
        updatedSubtask.setId(4);
        taskManager.updateSubtask(updatedSubtask);
        System.out.println("\nСписок обновлённых задач id=[1,3,4]:");
        for (Task task : taskManager.getAllTasksSortedById()) {
            System.out.println(task);
        }
        System.out.println("\nИстория просмотров после обновления задач, не должна меняться (5, 4, 3, 7, 6, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }


        // проверка полей эпика и истории просмотров после удаление подзадачи
        taskManager.deleteTask(4);
        System.out.println("\nПроверка изменения полей эпика (id=3) после удаление подзадачи (id=4):");
        System.out.println(taskManager.getEpic(3));
        System.out.println("\nИстория просмотров (5, 7, 6, 2, 1, 3):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        // проверка на изменение статуса эпика
        taskManager.setStatus(5, Status.DONE);
        taskManager.setStatus(6, Status.DONE);
        System.out.println("\nПроверка изменения статуса эпика (id=3) после изменения статуса его подзадач на DONE:");
        System.out.println(taskManager.getEpic(3));

        // удаление задач и проверка списка задач и истории просмотров
        taskManager.deleteTask(3);
        System.out.println("\nСписок задач (удалён id=3 и его подзадачи id=[5, 6]):");
        for (Task task : taskManager.getAllTasksSortedById()) {
            System.out.println(task);
        }
        System.out.println("\nИстория просмотров (7, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        taskManager.deleteAllTasks();
        System.out.println("\nСписок задач (удалены все задачи):");
        System.out.println(taskManager.getAllTasks()); // []
        System.out.println("История просмотров (удалена вся история):");
        System.out.println(taskManager.getHistory()); // []*/

    public static HTTPTaskManager loadFromServer(String serverURL, String key) throws IOException, InterruptedException {
        HTTPTaskManager htm = new HTTPTaskManager(serverURL);
        String content = htm.client.load(key);

        String[] tasksAndHistory = content.split("\n\n");
        String[] tasks = tasksAndHistory[0].split("\n");
        htm.restoreMaps(tasks);
        htm.idCounter = htm.getAllTasksSortedById().stream().map(Task::getId).max(Integer::compareTo).orElse(0) + 1;
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
            client.put(String.valueOf(key), sb.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseKey() {
        key++;
        System.out.println("Значение ключа key=" + key);
    }

    public KVTaskClient getClient() {
        return client;
    }

    public int getKey() {
        return key;
    }
}
