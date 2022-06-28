package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        checkLengthOfHistoryList();
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    private void checkLengthOfHistoryList() {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
    }
}
