package managers.task;

import managers.Managers;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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
    public Task getTaskById(Integer id) {
        if (taskMap.containsKey(id)) {
            historyManager.add(taskMap.get(id));
            return taskMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (subtaskMap.containsKey(id)) {
            historyManager.add(subtaskMap.get(id));
            return subtaskMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (epicMap.containsKey(id)) {
            historyManager.add(epicMap.get(id));
            return epicMap.get(id);
        }
        return null;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void addTask(Task task) {
        if (checkAndUpdateIntersectionWhenTaskAdded(task)) {
            task.setId(idCounter);
            taskMap.put(idCounter++, task);
            treeSet.add(task);
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + task.getName() + "' не добавлена.");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (checkAndUpdateIntersectionWhenTaskAdded(subtask)) {
            subtask.setId(idCounter);
            subtaskMap.put(idCounter++, subtask);
            treeSet.add(subtask);
            determineEpicFields(subtask.getEpicId());
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, подзадача '" + subtask.getName() + "' не добавлена.");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(idCounter);
        epicMap.put(idCounter++, epic);
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
    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = subtaskMap.put(subtask.getId(), subtask);
        if (oldSubtask != null) {
            treeSet.remove(oldSubtask);
        }
        treeSet.add(subtask);
        determineEpicFields(subtask.getEpicId());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epicMap.put(epic.getId(), epic);
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (taskMap.containsKey(id)) {
            historyManager.remove(id);
            treeSet.remove(taskMap.get(id));
            Task deletedTask = taskMap.remove(id);
            updateIntersectionWhenTaskDeleted(deletedTask);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (subtaskMap.containsKey(id)) {
            Epic epic = epicMap.get(subtaskMap.get(id).getEpicId());
            epic.removeSubtaskId(id);

            historyManager.remove(id);
            treeSet.remove(subtaskMap.get(id));
            Subtask deletedSubtask = subtaskMap.remove(id);
            updateIntersectionWhenTaskDeleted(deletedSubtask);

            determineEpicFields(epic.getId());
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epicMap.containsKey(id)) {
            List<Integer> subtasksId = List.copyOf(epicMap.get(id).getSubtasksId());
            for (Integer integer : subtasksId) {
                treeSet.remove(subtaskMap.get(integer));
                subtaskMap.remove(integer);
                historyManager.remove(integer);
            }
            historyManager.remove(id);
            treeSet.remove(epicMap.get(id));
            Epic deletedEpic = epicMap.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        List.copyOf(taskMap.values()).stream().map(Task::getId).forEach(this::deleteTaskById);
    }

    @Override
    public void deleteAllSubtasks() {
        List.copyOf(subtaskMap.values()).stream().map(Subtask::getId).forEach(this::deleteSubtaskById);
    }

    @Override
    public void deleteAllEpics() {
        List.copyOf(epicMap.values()).stream().map(Epic::getId).forEach(this::deleteEpicById);
    }

    @Override
    public void determineEpicStatus(int id) {
        if (getEpicSubtasks(id).stream().allMatch(subtask -> subtask.getStatus().equals(NEW))) {
            epicMap.get(id).setStatus(NEW);
        } else if (getEpicSubtasks(id).stream().allMatch(subtask -> subtask.getStatus().equals(DONE))) {
            epicMap.get(id).setStatus(DONE);
        } else {
            epicMap.get(id).setStatus(IN_PROGRESS);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        if (epicMap.get(id) != null) {
            return epicMap.get(id).getSubtasksId().stream().map(subtaskId -> subtaskMap.get(subtaskId)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return treeSet;
    }

    private void determineEpicFields(int id) {
        determineEpicSubtasksId(id);
        determineEpicStatus(id);
        determineEpicDuration(id);
        determineEpicStartTime(id);
        determineEpicEndTime(id);
    }

    private void determineEpicSubtasksId(int id) {
        List<Integer> epicSubtasks = subtaskMap.values().stream()
                .filter(subtask -> subtask.getEpicId() == id)
                .mapToInt(Subtask::getId)
                .boxed()
                .collect(Collectors.toList());
        Epic parentEpic = epicMap.get(id);
        epicSubtasks.stream()
                .filter(epicSubtaskId -> !parentEpic.getSubtasksId()
                        .contains(epicSubtaskId))
                .forEach(parentEpic::addSubtaskId);
    }

    private void determineEpicDuration(int id) {
        if (!getEpicSubtasks(id).isEmpty()) {
            long duration = getEpicSubtasks(id).stream()
                    .map(Subtask::getDuration)
                    .map(Duration::getSeconds)
                    .mapToInt(Math::toIntExact)
                    .sum() / 60;
            epicMap.get(id).setDuration(duration);
        }
    }

    private void determineEpicStartTime(int id) {
        if (!getEpicSubtasks(id).isEmpty()) {
            LocalDateTime startTime = getEpicSubtasks(id).stream()
                    .map(Subtask::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
            epicMap.get(id).setStartTime(startTime);
        }
    }

    private void determineEpicEndTime(int id) {
        if (!getEpicSubtasks(id).isEmpty()) {
            LocalDateTime endTime = getEpicSubtasks(id).stream()
                    .map(Subtask::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            epicMap.get(id).setEndTime(endTime);
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public boolean checkAndUpdateIntersectionWhenTaskAdded(Task task) {
        if (task.getStartTime().isBefore(start) || task.getEndTime().isAfter(end)) {
            throw new IllegalArgumentException("Время начала и завершения задач доступно только на 2022 год");
        }
        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15); // округление до четверти часа в меньшую сторону
        LocalDateTime taskEnd = task.getEndTime();

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

    protected void updateIntersectionWhenTaskDeleted(Task task) {
        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15); // округление до четверти часа в меньшую сторону
        LocalDateTime taskEnd = task.getEndTime();

        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            intersectionMap.put(i, true);
        }
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
        return idCounter == that.idCounter
                && Objects.equals(taskMap, that.taskMap)
                && Objects.equals(epicMap, that.epicMap)
                && Objects.equals(subtaskMap, that.subtaskMap)
                && Objects.equals(historyManager.getHistory(), that.historyManager.getHistory())
                && Objects.equals(treeSet, that.treeSet)
                && Objects.equals(intersectionMap, that.intersectionMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskMap, epicMap, subtaskMap, historyManager, idCounter, treeSet, intersectionMap);
    }
}