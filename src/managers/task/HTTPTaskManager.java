package managers.task;

import api.KVTaskClient;
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

    public static HTTPTaskManager loadFromServer(String serverURL, String key) throws IOException, InterruptedException {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager(serverURL);
        String content = httpTaskManager.client.load(key);

        String[] tasksAndHistory = content.split("\n\n");
        String[] tasks = tasksAndHistory[0].split("\n");
        httpTaskManager.restoreMaps(tasks);
        httpTaskManager.idCounter = httpTaskManager.getAllTasksSortedById().stream().map(Task::getId).max(Integer::compareTo).orElse(0) + 1;
        if (tasksAndHistory.length > 1) {
            String history = tasksAndHistory[1];
            httpTaskManager.restoreHistory(history);
        }
        httpTaskManager.save();
        return httpTaskManager;
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
