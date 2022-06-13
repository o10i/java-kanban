import java.util.HashMap;
import java.util.Map;

public class Manager {
    private int idCounter = 1;
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Epic> epicMap = new HashMap<>();
    private Map<Integer, Subtask> subtaskMap = new HashMap<>();

    public void printAllTasks() {
        if (taskMap.size() > 0) {
            int counter = 1;
            System.out.println("Задачи:");
            for (Task task : taskMap.values()) {
                System.out.println("\t Задача " + counter + ": " + task.title);
                counter++;
            }
        }
        if (epicMap.size() > 0) {
            int counter = 1;
            System.out.println("Эпики:");
            for (Epic epic : epicMap.values()) {
                System.out.println("\tЭпик " + counter + ": " + epic.title);
                counter++;
                epic.printSubtasks();
            }
        }
    }

    public void deleteAllTasks() {
        taskMap.clear();
        epicMap.clear();
        subtaskMap.clear();
    }

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
