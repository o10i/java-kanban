package managers;

import tasks.Task;

import java.util.Deque;

public interface HistoryManager {
    void add(Task task);

    Deque<Task> getHistory();
}
