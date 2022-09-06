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
    protected int idCounter = 0;
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    protected Map<LocalDateTime, Boolean> intersectionMap = fillIntersectionMap();

    protected static void printTasks(TaskManager taskManager) {
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    protected static void fillData(TaskManager taskManager) {
        Task task1 = new Task("Task1", "Task1 description", 30, LocalDateTime.now());
        taskManager.addTask(task1);

        Task task2 = new Task("Task2", "Task2 description", 30, LocalDateTime.now().plusDays(1));
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "Epic1 description");
        int epicId = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description", 30, LocalDateTime.now().plusDays(2), epicId);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description", 30, LocalDateTime.now().plusDays(4), epicId);
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3 description", 30, LocalDateTime.now().plusDays(3), epicId);
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Epic2", "Epic2 description");
        taskManager.addEpic(epic2);

        System.out.println("Список созданных задач:");
        printTasks(taskManager);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(7);
        taskManager.getTaskById(1);

        System.out.println("\nИстория просмотров (2, 3, 4, 5, 6, 7, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\nСписок задач и подзадач в порядке приоритета:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }

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
    public int addTask(Task task) {
        if (checkIntersection(task)) {
            updateIntersectionWhenTaskAdded(task);
            int id = ++idCounter;
            task.setId(idCounter);
            taskMap.put(idCounter, task);
            prioritizedTasks.add(task);
            return id;
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + task.getName() + "' не добавлена.");
        }
    }

    @Override
    public int addSubtask(Subtask subtask) {
        if (checkIntersection(subtask)) {
            updateIntersectionWhenTaskAdded(subtask);
            int id = ++idCounter;
            subtask.setId(idCounter);
            subtaskMap.put(idCounter, subtask);
            prioritizedTasks.add(subtask);
            determineEpicFields(subtask.getEpicId());
            return id;
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, подзадача '" + subtask.getName() + "' не добавлена.");
        }
    }

    @Override
    public int addEpic(Epic epic) {
        int id = ++idCounter;
        epic.setId(idCounter);
        epicMap.put(idCounter, epic);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if (!taskMap.containsKey(id)) {
            throw new IllegalArgumentException("Задача с id=" + task.getId() + " отсутствует, задача '" + task.getName() + "' не обновлена.");
        }
        Task oldTask = taskMap.get(id);
        if (checkIntersectionWhenTaskUpdated(oldTask, task)) {
            taskMap.put(id, task);
            prioritizedTasks.remove(oldTask);
            prioritizedTasks.add(task);
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + task.getName() + "' не обновлена.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (!subtaskMap.containsKey(id)) {
            throw new IllegalArgumentException("Подзадача с id=" + subtask.getId() + " отсутствует, подзадача '" + subtask.getName() + "' не обновлена.");
        }
        Subtask oldSubtask = subtaskMap.get(id);
        if (checkIntersectionWhenTaskUpdated(oldSubtask, subtask)) {
            subtaskMap.put(id, subtask);
            prioritizedTasks.remove(oldSubtask);
            prioritizedTasks.add(subtask);
            determineEpicFields(subtask.getEpicId());
        } else {
            throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + subtask.getName() + "' не обновлена.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if (!epicMap.containsKey(id)) {
            throw new IllegalArgumentException("Эпик с id=" + epic.getId() + " отсутствует, эпик '" + epic.getName() + "' не обновлён.");
        }
        epicMap.put(id, epic);
    }

    @Override
    public void deleteTaskById(Integer id) {
        if (taskMap.containsKey(id)) {
            historyManager.remove(id);
            prioritizedTasks.remove(taskMap.get(id));
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
            prioritizedTasks.remove(subtaskMap.get(id));
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
                prioritizedTasks.remove(subtaskMap.get(integer));
                subtaskMap.remove(integer);
                historyManager.remove(integer);
            }
            historyManager.remove(id);
            prioritizedTasks.remove(epicMap.get(id));
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
        return prioritizedTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return Objects.equals(taskMap, that.taskMap)
                && Objects.equals(epicMap, that.epicMap)
                && Objects.equals(subtaskMap, that.subtaskMap)
                && Objects.equals(historyManager.getHistory(), that.historyManager.getHistory())
                && Objects.equals(prioritizedTasks, that.prioritizedTasks)
                && Objects.equals(intersectionMap, that.intersectionMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskMap, epicMap, subtaskMap, historyManager, idCounter, prioritizedTasks, intersectionMap);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public boolean checkIntersection(Task task) {
        if (task.getStartTime().isBefore(start) || task.getEndTime().isAfter(end)) {
            throw new IllegalArgumentException("Время начала и завершения задач доступно только на 2022 год");
        }
        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15);
        LocalDateTime taskEnd = task.getEndTime();

        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            if (!intersectionMap.get(i)) {
                return false;
            }
        }
        return true;
    }

    protected void updateIntersectionWhenTaskAdded(Task task) {
        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15);
        LocalDateTime taskEnd = task.getEndTime();

        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            intersectionMap.put(i, false);
        }
    }

    private void updateIntersectionWhenTaskDeleted(Task task) {
        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15);
        LocalDateTime taskEnd = task.getEndTime();

        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            intersectionMap.put(i, true);
        }
    }

    private boolean checkIntersectionWhenTaskUpdated(Task oldTask, Task task) {
        if (task.getStartTime().isBefore(start) || task.getEndTime().isAfter(end)) {
            throw new IllegalArgumentException("Время начала и завершения задач доступно только на 2022 год");
        }

        Map<LocalDateTime, Boolean> map = new HashMap<>(intersectionMap);
        LocalDateTime oldTaskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15);
        LocalDateTime oldTaskEnd = task.getEndTime();

        for (LocalDateTime i = oldTaskStart; i.isBefore(oldTaskEnd); i = i.plusMinutes(15)) {
            map.put(i, true);
        }

        LocalDateTime taskStart = task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15);
        LocalDateTime taskEnd = task.getEndTime();

        for (LocalDateTime i = taskStart; i.isBefore(taskEnd); i = i.plusMinutes(15)) {
            if (!map.get(i)) {
                return false;
            }
        }
        return true;
    }

    protected void determineEpicFields(int id) {
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

    private Map<LocalDateTime, Boolean> fillIntersectionMap() {
        Map<LocalDateTime, Boolean> intersectionMap = new HashMap<>();
        for (LocalDateTime i = start; i.isBefore(end); i = i.plusMinutes(15)) {
            intersectionMap.put(i, true);
        }
        return intersectionMap;
    }

    /*    public static void main(String[] args) {
            long start = System.nanoTime();
            InMemoryTaskManager taskManager = new InMemoryTaskManager();

            fillData(taskManager);

            long finish = System.nanoTime();
            System.out.println("\nМетод 'main' выполнен за " + (finish - start) / 1000000 + " миллисекунд");
        }*/
}