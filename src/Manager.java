import java.util.HashMap;
import java.util.Map;

public class Manager {
    private int idCounter = 1;
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Epic> epicMap = new HashMap<>();
    private Map<Integer, Subtask> subtaskMap = new HashMap<>();
    int[]  list = {1, 2, 3};


    public void addTask(Task task) {
        switch (task.getClass().toString()) {
            case "class Task":
                taskMap.put(idCounter, task);
                break;
            case "class Epic":
                epicMap.put(idCounter, (Epic) task);
                break;
            case "class Subtask":
                subtaskMap.put(idCounter, (Subtask) task);
                for (Integer integer : epicMap.keySet()) {
                    if (epicMap.get(integer).title.equals(((Subtask) task).getParentEpic())) {
                        epicMap.get(integer).addSubtask((Subtask) task);
                    }
                }
                break;
        }
        idCounter++;
    }

    public int getIdCounter() {
        return idCounter;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "\ntaskMap=" + taskMap +
                "epicMap=" + epicMap +
                '}';
    }
}
