package managers;

import tasks.Task;

import java.util.Deque;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final Deque<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        checkLengthOfHistoryList();
        historyList.add(task);
    }

    @Override
    public Deque<Task> getHistory() {
        return historyList;
    }

    private void checkLengthOfHistoryList() {
        if (historyList.size() == 10) {
            historyList.removeFirst();
        }
    }
}
