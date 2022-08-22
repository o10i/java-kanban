package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idCounter = 1;
    protected Set<Task> treeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    protected Map<LocalDateTime, Boolean> intersectionMap = fillIntersectionMap();
    private static final LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0);
    private static final LocalDateTime end = LocalDateTime.of(2023, 1, 1, 0, 0);

    @Override
    public void addTask(Task task) {
        if (checkIntersection(task)) {
            task.setId(idCounter);
            taskMap.put(idCounter++, task);
            treeSet.add(task);
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + task.getTitle() + "' не добавлена.");
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
            subtaskMap.put(idCounter++, subtask);
            treeSet.add(subtask);
            Epic parentEpic = epicMap.get(subtask.getParentEpicId());
            if (!parentEpic.getSubtasksId().contains(subtask.getId())) {
                parentEpic.addSubtaskId(subtask.getId());
            }
            determineEpicFields(subtask.getParentEpicId());
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, подзадача '" + subtask.getTitle() + "' не добавлена.");
        }
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
            determineEpicFields(parentEpic.getId());
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
    public List<Subtask> getSubtasksByEpicId(int id) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epicMap.get(id).getSubtasksId()) {
            epicSubtasks.add(subtaskMap.get(subtaskId));
        }
        return epicSubtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
    public void updateTask(Task task) {
        if (checkIntersection(task)) {
            taskMap.put(task.getId(), task);
            treeSet.remove(task);
            treeSet.add(task);
        } else {
            System.out.println("Обнаружено пересечение задач, задача не добавлена.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
        treeSet.remove(epic);
        treeSet.add(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (checkIntersection(subtask)) {
            subtaskMap.put(subtask.getId(), subtask);
            treeSet.remove(subtask);
            treeSet.add(subtask);
            Epic parentEpic = epicMap.get(subtask.getParentEpicId());
            if (!parentEpic.getSubtasksId().contains(subtask.getId())) {
                parentEpic.addSubtaskId(subtask.getId());
            }
            determineEpicFields(subtask.getParentEpicId());
        } else {
            System.out.println("Обнаружено пересечение задач, подзадача не добавлена.");
        }
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

    public void determineEpicStatus(int id) {
        if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(Status.NEW))) {
            epicMap.get(id).setStatus(Status.NEW);
        } else if (getSubtasksByEpicId(id).stream().allMatch(subtask -> subtask.getStatus().equals(Status.DONE))) {
            epicMap.get(id).setStatus(Status.DONE);
        } else {
            epicMap.get(id).setStatus(Status.IN_PROGRESS);
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
    public Set<Task> getPrioritizedTasks() {
        return treeSet;
    }

    protected void determineEpicFields(int id) {
        determineEpicStatus(id);
        determineEpicDuration(id);
        determineEpicStartTime(id);
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
}