import java.util.HashMap;
import java.util.Map;

public class Manager {
    private int idCounter;
    private Map<Integer, Task> taskMap;
    private Map<Integer, Epic> epicMap;

    public Manager() {
        this.idCounter = 1;
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
    }

    public String getAllTasks() {
        String allTasks = "";
        if (taskMap.size() > 0) {
            int counter = 1;
            allTasks += "Задачи:\n";
            for (Task task : taskMap.values()) {
                allTasks += "\t" + counter + ": " + task.toString() + "\n";
                counter++;
            }
        }
        if (epicMap.size() > 0) {
            int counter = 1;
            allTasks += "Эпики:\n";
            for (Epic epic : epicMap.values()) {
                allTasks += "\t" + counter + ": " + epic.toString() + "\n";
                counter++;
            }
        }
        return allTasks;
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

    public String getEpicSubtasks(Epic epic) {
        return "Подзадачи эпика '" + epic.getTitle() + "':" + epic.getSubtasks();
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

    public void setStatus(Integer id, String status) {
        getTask(id).setStatus(status);
    }
}
