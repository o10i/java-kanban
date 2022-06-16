package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Manager {
    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private int idCounter;

    public Manager() {
        idCounter = 1;
        taskMap = new HashMap<>();
        epicMap = new HashMap<>();
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
        task.setId(idCounter);
        task.setStatus(Task.NEW);
        if (task instanceof Epic) {
            epicMap.put(idCounter, (Epic) task);
        } else if (task instanceof Subtask) {
            for (Integer key : epicMap.keySet()) {
                if (key == ((Subtask) task).getParentEpicId()) {
                    epicMap.get(key).addSubtask(task.getId(), (Subtask) task);
                }
            }
        } else taskMap.put(idCounter, task);
        idCounter++;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void updateTask(Task task, int id) {
        task.setId(id);
        task.setStatus(Task.NEW);
        if (task instanceof Epic) {
            epicMap.put(id, (Epic) task);
        } else if (task instanceof Subtask) {
            for (Integer key : epicMap.keySet()) {
                if (key == ((Subtask) task).getParentEpicId()) {
                    epicMap.get(key).addSubtask(id, (Subtask) task);
                }
            }
        } else taskMap.put(id, task);
    }

    public void deleteTask(Integer id) {
        if (taskMap.remove(id) != null) taskMap.remove(id);
        else if (epicMap.remove(id) != null) epicMap.remove(id);
        else for (Epic epic : epicMap.values()) {
                if (epic.getSubtaskMap().remove(id) != null) epic.getSubtaskMap().remove(id);
            }
    }

    public String getEpicSubtasksByEpicId(int id) {
        return "Подзадачи эпика '" + epicMap.get(id).getTitle() + "':" + epicMap.get(id).getSubtasks();
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
        getTask(id).setStatus(status); // у эпика метод переопределён, для него будет бездействие
        if (getTask(id) instanceof Subtask) {
            epicMap.get(((Subtask) getTask(id)).getParentEpicId()).determineEpicStatus();
        }
    }
}
