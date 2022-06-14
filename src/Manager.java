import java.util.HashMap;
import java.util.Map;

public class Manager {
    private int idCounter = 1;
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Epic> epicMap = new HashMap<>();

    public void printAllTasks() {
        if (taskMap.size() > 0) {
            int counter = 1;
            System.out.println("Задачи:");
            for (Task task : taskMap.values()) {
                System.out.println("\t Задача " + counter + ": " + task.getTitle());
                counter++;
            }
        }
        if (epicMap.size() > 0) {
            int counter = 1;
            System.out.println("Эпики:");
            for (Epic epic : epicMap.values()) {
                System.out.println("\tЭпик " + counter + ": " + epic.getTitle());
                counter++;
                epic.printSubtasks();
            }
        }
    }

    public void deleteAllTasks() {
        taskMap.clear();
        epicMap.clear();
    }

    public Task getTask(Integer id) {
        if (taskMap.containsKey(id)) return taskMap.get(id);
        if (epicMap.containsKey(id)) return epicMap.get(id);
        for (Epic epic : epicMap.values()) {
            if (epic.getSubtaskMap().containsKey(id)) return epic.getSubtaskMap().get(id);
        }
        return null;
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
                for (Integer key : epicMap.keySet()) {
                    if (epicMap.get(key) == ((Subtask) task).getParentEpic()) {
                        epicMap.get(key).addSubtask(task.getId(), (Subtask) task);
                    }
                }
                break;
        }
        idCounter++;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void updateTask(Task task) {
        switch (task.getClass().toString()) {
            case "class Task":
                taskMap.put(task.getId(), task);
                break;
            case "class Epic":
                epicMap.put(task.getId(), (Epic) task);
                break;
            case "class Subtask":
                for (Integer key : epicMap.keySet()) {
                    if (epicMap.get(key) == ((Subtask) task).getParentEpic()) {
                        epicMap.get(key).getSubtaskMap().put(task.getId(), (Subtask) task);
                    }
                }
                break;
        }
    }

    public void deleteTask(Integer id) {
        taskMap.remove(id);
        epicMap.remove(id);
        for (Epic epic : epicMap.values()) {
            epic.getSubtaskMap().remove(id);
        }
    }

    public void printEpicSubtasks(Epic epic) {
        System.out.println("Подзадачи эпика '" + epic.getTitle() + "':");
        epic.printSubtasks();
    }

    public String getStatus(Integer id) {
        if (taskMap.containsKey(id)) {

            return taskMap.get(id).getStatus();
        }
        if (epicMap.containsKey(id)) {
            return epicMap.get(id).getStatus();
        }
        for (Epic epic : epicMap.values()) {
            if (epic.getSubtaskMap().containsKey(id)) {
                return epic.getSubtaskMap().get(id).getStatus();
            }
        }
        return null;
    }
}
