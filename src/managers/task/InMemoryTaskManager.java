package managers.task;

import managers.Managers;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static tasks.enums.Status.*;

public class InMemoryTaskManager implements TaskManager {
    protected static LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0);
    protected static LocalDateTime end = LocalDateTime.of(2023, 1, 1, 0, 0);
    protected Map<Integer, Task> taskMap = new HashMap<>();
    protected Map<Integer, Epic> epicMap = new HashMap<>();
    protected Map<Integer, Subtask> subtaskMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idCounter = 1;
    protected Set<Task> treeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    protected Map<LocalDateTime, Boolean> intersectionMap = fillIntersectionMap();

    @Override
    public void addTask(Task task) {
        if (checkIntersection(task)) {
            task.setId(idCounter);
            taskMap.put(idCounter++, task);
            treeSet.add(task);
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + task.getName() + "' не добавлена.");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(idCounter);
        epicMap.put(idCounter++, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (checkIntersection(subtask)) {
            subtask.setId(idCounter);
            if (epicMap.get(subtask.getParentEpicId()).getSubtasksId().contains(subtask.getId())) {
                throw new IllegalArgumentException("Подзадача с id=" + subtask.getId() + " уже есть у эпика.");
            } else {
                subtaskMap.put(idCounter++, subtask);
                treeSet.add(subtask);
                determineParentEpicFields(subtask.getParentEpicId());
            }
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, подзадача '" + subtask.getName() + "' не добавлена.");
        }
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = taskMap.put(task.getId(), task);
        if (oldTask != null) {
            treeSet.remove(oldTask);
        }
        treeSet.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epicMap.put(epic.getId(), epic);
        if (oldEpic != null) {
            treeSet.remove(oldEpic);
        }
        treeSet.add(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = subtaskMap.put(subtask.getId(), subtask);
        if (oldSubtask != null) {
            treeSet.remove(oldSubtask);
        }
        treeSet.add(subtask);
        determineParentEpicFields(subtask.getParentEpicId());
    }

    @Override
    public void deleteTask(Integer id) {
        if (taskMap.containsKey(id)) {
            treeSet.remove(taskMap.get(id));
            taskMap.remove(id);
        } else if (epicMap.containsKey(id)) {
            List<Integer> subtasksId = List.copyOf(epicMap.get(id).getSubtasksId());
            for (Integer subtaskId : subtasksId) {
                deleteTask(subtaskId);
            }
            treeSet.remove(epicMap.get(id));
            epicMap.remove(id);
        } else if (subtaskMap.containsKey(id)) {
            Epic parentEpic = epicMap.get(subtaskMap.get(id).getParentEpicId());
            parentEpic.removeSubtaskId(id);
            treeSet.remove(subtaskMap.get(id));
            subtaskMap.remove(id);
            determineParentEpicFields(parentEpic.getId());
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        taskMap.clear();
        epicMap.clear();
        subtaskMap.clear();
        historyManager = Managers.getDefaultHistory();
        treeSet.clear();
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
    public List<Task> getAllTasks() {
        List<Task> allTasksList = new ArrayList<>();
        allTasksList.addAll(taskMap.values());
        allTasksList.addAll(epicMap.values());
        allTasksList.addAll(subtaskMap.values());
        return allTasksList;
    }

    @Override
    public List<Task> getAllTasksSortedById() {
        return getAllTasks().stream().sorted(Comparator.comparingInt(Task::getId)).collect(Collectors.toList());
    }

    public Set<Task> getPrioritizedTasks() {
        return treeSet;
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epicMap.get(id).getSubtasksId()) {
            epicSubtasks.add(subtaskMap.get(subtaskId));
        }
        return epicSubtasks;
    }

    @Override
    public int getIdCounter() {
        return idCounter;
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void determineParentEpicFields(int id) {
        determineEpicSubtasksId(id);
        determineEpicStatus(id);
        determineEpicDuration(id);
        determineEpicStartTime(id);
        determineEpicEndTime(id);
    }

    private void determineEpicSubtasksId(int id) {
        List<Integer> epicSubtasks = subtaskMap.values().stream().filter(subtask -> subtask.getParentEpicId() == id).mapToInt(Subtask::getId).boxed().collect(Collectors.toList());
        Epic parentEpic = epicMap.get(id);
        epicSubtasks.stream().filter(epicSubtaskId -> !parentEpic.getSubtasksId().contains(epicSubtaskId)).forEach(parentEpic::addSubtaskId);
    }

    @Override
    public void determineEpicStatus(int id) {
        if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(NEW))) {
            epicMap.get(id).setStatus(NEW);
        } else if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(DONE))) {
            epicMap.get(id).setStatus(DONE);
        } else {
            epicMap.get(id).setStatus(IN_PROGRESS);
        }
    }

    protected void determineEpicDuration(int id) {
        if (!getSubtasksByEpicId(id).isEmpty()) {
            long duration = getSubtasksByEpicId(id).stream().map(Subtask::getDuration).map(Duration::getSeconds).mapToInt(Math::toIntExact).sum() / 60;
            epicMap.get(id).setDuration(duration);
        }
    }

    protected void determineEpicStartTime(int id) {
        if (!getSubtasksByEpicId(id).isEmpty()) {
            LocalDateTime startTime = getSubtasksByEpicId(id).stream().map(Subtask::getStartTime).min(LocalDateTime::compareTo).orElse(null);
            epicMap.get(id).setStartTime(startTime);
        }
    }

    protected void determineEpicEndTime(int id) {
        if (!getSubtasksByEpicId(id).isEmpty()) {
            LocalDateTime endTime = getSubtasksByEpicId(id).stream().map(Subtask::getEndTime).max(LocalDateTime::compareTo).orElse(null);
            epicMap.get(id).setEndTime(endTime);
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private boolean checkIntersection(Task task) {
        if (task.getStartTime().isBefore(start) || task.getEndTime().isAfter(end)) {
            throw new IllegalArgumentException("Время начала и завершения задач доступно только на 2022 год");
        }
        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15); // округление до четверти часа в меньшую сторону
        LocalDateTime taskEnd = task.getEndTime(); // округление до четверти часа в меньшую сторону

        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            if (!intersectionMap.get(i)) {
                return false;
            }
        }
        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            intersectionMap.put(i, false);
        }
        return true;
    }

    private Map<LocalDateTime, Boolean> fillIntersectionMap() {
        Map<LocalDateTime, Boolean> intersectionMap = new HashMap<>();
        for (LocalDateTime i = start; i.isBefore(end); i = i.plusMinutes(15)) {
            intersectionMap.put(i, true);
        }
        return intersectionMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return idCounter == that.idCounter && Objects.equals(taskMap, that.taskMap) && Objects.equals(epicMap, that.epicMap) && Objects.equals(subtaskMap, that.subtaskMap) && Objects.equals(historyManager.getHistory(), that.historyManager.getHistory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskMap, epicMap, subtaskMap, historyManager, idCounter);
    }
}