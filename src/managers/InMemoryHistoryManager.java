package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    public void lengthOfHistoryListCheck() {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
    }

    public void printHistory() {
        System.out.println("История просмотров:");
        int counter = 1;
        for (Task task : historyList) {
            System.out.println(counter + ": " + task.getClass().getSimpleName() + "[" + task.getTitle() + " (id=" + task.getId() + ")]");
            counter++;
        }
    }
}
