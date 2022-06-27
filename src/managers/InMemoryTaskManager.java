package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// основная моя проблема - понимание ТЗ, особенно крайний его подпункт, так сказать общая картина не рисуется
// потому рефакторинг не получается, чтобы не мучаться думаю лучше сразу твои ценные советы услышать
public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private int idCounter;
    private final List<Task> historyList;

    public InMemoryTaskManager() {
        idCounter = 1;
        taskMap = new HashMap<>();
        epicMap = new HashMap<>();
        historyList = new ArrayList<>();
    }

    @Override
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

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
        epicMap.clear();
    }

    @Override
    public Task getTask(Integer id) {
        if (taskMap.containsKey(id)) {
            lengthOfHistoryListCheck();
            historyList.add(taskMap.get(id));
            return taskMap.get(id);
        }
        if (epicMap.containsKey(id)) {
            return getEpic(id);
        }
        for (Epic epic : epicMap.values()) {
            if (epic.getSubtaskMap().containsKey(id)) {
                return getSubtask(id, epic);
            }
        }
        return null;
    }

    private Task getEpic(Integer id) {
        lengthOfHistoryListCheck();
        historyList.add(epicMap.get(id));
        return epicMap.get(id);
    }

    private Task getSubtask(Integer id, Epic epic) {
        lengthOfHistoryListCheck();
        historyList.add(epic.getSubtaskMap().get(id));
        return epic.getSubtaskMap().get(id);
    }

    @Override
    public void addTask(Task task) {
        task.setId(idCounter);
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

    @Override
    public int getIdCounter() {
        return idCounter;
    }

    @Override
    public void updateTask(Task task, int id) {
        task.setId(id);
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

    @Override
    public void deleteTask(Integer id) {
        if (taskMap.remove(id) != null) {
            taskMap.remove(id);
        } else if (epicMap.remove(id) != null) {
            epicMap.remove(id);
        } else for (Epic epic : epicMap.values()) {
            if (epic.getSubtaskMap().remove(id) != null) {
                epic.getSubtaskMap().remove(id);
            }
        }
    }

    @Override
    public String getEpicSubtasksByEpicId(int id) {
        return "Подзадачи эпика '" + epicMap.get(id).getTitle() + "':" + epicMap.get(id).getSubtasks();
    }

    @Override
    public Status getStatus(Integer id) {
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

    @Override
    public void setStatus(Integer id, Status status) {
        Task task = getTask(id);
        task.setStatus(status);
        if (task instanceof Subtask) {
            epicMap.get(((Subtask) task).getParentEpicId()).determineEpicStatus();
        }
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

    // сделал отдельный метод для печати истории, если печатать лист из getHistory(), то напечатается некрасиво из-за
    // переопределённых методов toString в задачах.
    public void printHistory() {
        System.out.println("История просмотров:");
        int counter = 1;
        for (Task task : historyList) {
            System.out.println(counter + ": " + task.getClass().getSimpleName() + "[" + task.getTitle() + " (id=" + task.getId() + ")]");
            counter++;
        }
    }
}
