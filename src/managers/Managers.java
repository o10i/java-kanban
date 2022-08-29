package managers;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.FileBackedTasksManager;
import managers.task.TaskManager;

public class Managers {
    public TaskManager getDefault() {
        return new FileBackedTasksManager("history.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}