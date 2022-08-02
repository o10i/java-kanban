package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idCounter = 1;

    @Override
    public List<Task> getAllTasks() {
        List<Task> allTasksList = new ArrayList<>();
        allTasksList.addAll(taskMap.values());
        allTasksList.addAll(epicMap.values());
        allTasksList.addAll(subtaskMap.values());
        return allTasksList;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public Task getTask(Integer id) {
        if (taskMap.containsKey(id)) {
            historyManager.add(taskMap.get(id));
            return taskMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        if (epicMap.containsKey(id)) {
            historyManager.add(epicMap.get(id));
            return epicMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        if (subtaskMap.containsKey(id)) {
            historyManager.add(subtaskMap.get(id));
            return subtaskMap.get(id);
        }
        return null;
    }

    @Override
    public void addTask(Task task) {
        task.setId(idCounter);
        taskMap.put(idCounter++, task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(idCounter);
        epicMap.put(idCounter++, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(idCounter);
        subtaskMap.put(idCounter++, subtask);
        for (Integer key : epicMap.keySet()) {
            if (key == subtask.getParentEpicId()) {
                epicMap.get(key).addSubtaskId(subtask.getId());
                determineEpicStatus(subtask.getParentEpicId());
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }
    // добавил тут логику
    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        for (Integer key : epicMap.keySet()) {
            if (key == subtask.getParentEpicId()) {
                if (!epicMap.get(key).getSubtasksId().contains(subtask.getId())) {
                    epicMap.get(key).addSubtaskId(subtask.getId());
                    determineEpicStatus(subtask.getParentEpicId());
                }
            }
        }
    }

    @Override
    public void deleteTask(Integer id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
        } else if (epicMap.containsKey(id)) {
            List<Integer> subtasksId = List.copyOf(epicMap.get(id).getSubtasksId());
            for (Integer subtaskId : subtasksId) {
                deleteTask(subtaskId);
            }
            epicMap.remove(id);
        } else if (subtaskMap.containsKey(id)) {
            subtaskMap.remove(id);
            for (Epic epic : epicMap.values()) {
                if (epic.getSubtasksId().contains(id)) {
                    epic.removeSubtaskIdByObject(id);
                    determineEpicStatus(epic.getId());
                    break;
                }
            }
        }
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtaskMap.values()) {
            if (subtask.getParentEpicId() == id) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
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

    @Override
    public void setStatus(Integer id, Status status) {
        if (taskMap.containsKey(id)) {
            taskMap.get(id).setStatus(status);
        } else if (subtaskMap.containsKey(id)) {
            subtaskMap.get(id).setStatus(status);
            determineEpicStatus(subtaskMap.get(id).getParentEpicId());
        }
    }

    private void determineEpicStatus(int id) {
        if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(Status.NEW))) {
            epicMap.get(id).setStatus(Status.NEW);
        } else if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(Status.DONE))) {
            epicMap.get(id).setStatus(Status.DONE);
        } else {
            epicMap.get(id).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
