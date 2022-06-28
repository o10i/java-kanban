package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, Subtask> subtaskMap;
    private int idCounter;

    public Manager() {
        idCounter = 1;
        taskMap = new HashMap<>();
        epicMap = new HashMap<>();
        subtaskMap = new HashMap<>();
    }

    public List<Task> getAllTasks() {
        List<Task> allTasksList = new ArrayList<>();
        allTasksList.addAll(getTasks());
        allTasksList.addAll(getEpics());
        allTasksList.addAll(getSubtasks());
        return allTasksList;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    private List<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    private List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    public void deleteAllTasks() {
        taskMap.clear();
        epicMap.clear();
        subtaskMap.clear();
    }

    public Task getTask(Integer id) {
        if (taskMap.containsKey(id)) {
            return taskMap.get(id);
        }
        if (epicMap.containsKey(id)) {
            return epicMap.get(id);
        }
        if (subtaskMap.containsKey(id)) {
            return subtaskMap.get(id);
        }
        return null;
    }

    public void addTask(Task task) {
        task.setId(idCounter);
        taskMap.put(idCounter++, task);
    }

    public void addTask(Epic epic) {
        epic.setId(idCounter);
        epicMap.put(idCounter++, epic);
    }

    public void addTask(Subtask subtask) {
        subtask.setId(idCounter);
        subtaskMap.put(idCounter++, subtask);
        for (Integer key : epicMap.keySet()) {
            if (key == subtask.getParentEpicId()) {
                epicMap.get(key).addSubtaskId(subtask.getId());
                determineEpicStatus(subtask.getParentEpicId());
            }
        }
    }

    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void updateTask(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    public void updateTask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        determineEpicStatus(subtask.getParentEpicId());
    }

    public void deleteTask(Integer id) {
        if (taskMap.remove(id) != null) {
            taskMap.remove(id);
        } else if (epicMap.remove(id) != null) {
            epicMap.remove(id);
        } else if (subtaskMap.remove(id) != null) {
            subtaskMap.remove(id);
            for (Epic epic : epicMap.values()) {
                if (epic.getSubtasksId().contains(id)) {
                    epic.removeSubtaskIdByObject(id);
                    determineEpicStatus(epic.getId());
                }
            }
        }
    }

    public List<Subtask> getSubtasksByEpicId(int id) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtaskMap.values()) {
            if (subtask.getParentEpicId() == id) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    public Status getStatus(Integer id) {
        if (taskMap.containsKey(id)) {
            return taskMap.get(id).getStatus();
        }
        if (epicMap.containsKey(id)) {
            return epicMap.get(id).getStatus();
        }
        if (subtaskMap.containsKey(id)) {
            return subtaskMap.get(id).getStatus();
        }
        return null;
    }

    public void setStatus(Integer id, Status status) {
        Task task = getTask(id);
        if (!(task instanceof Epic)) {
            task.setStatus(status);
            if (task instanceof Subtask) {
                determineEpicStatus(((Subtask) task).getParentEpicId());
            }
        }
    }

    private void determineEpicStatus(int id) {
        if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(Status.NEW))) {
            getTask(id).setStatus(Status.NEW);
        } else if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(Status.DONE))) {
            getTask(id).setStatus(Status.DONE);
        } else getTask(id).setStatus(Status.IN_PROGRESS);
    }
}
