package managers.task;

import managers.exceptions.ManagerSaveException;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;
import tasks.enums.Type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tasks.enums.Type.EPIC;
import static tasks.enums.Type.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected final String TABLE_HEADER = "id,type,name,status,description,duration,startTime,epic\n";
    private final int ID = 0;
    private final int TYPE = 1;
    private final int NAME = 2;
    private final int STATUS = 3;
    private final int DESCRIPTION = 4;
    private final int DURATION = 5;
    private final int START_TIME = 6;
    private final int PARENT_EPIC = 7;
    protected String sourceName;

    public FileBackedTasksManager(String sourceName) {
        this.sourceName = sourceName;
    }

    protected static String historyToString(HistoryManager manager) {
        List<Task> list = manager.getHistory();
        StringBuilder history = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                history.append(list.get(i).getId());
            } else {
                history.append(list.get(i).getId());
                history.append(",");
            }
        }
        return history.toString();
    }

    private static List<Integer> historyFromString(String value) {
        String[] el = value.split(",");
        return Arrays.stream(el).map(Integer::parseInt).collect(Collectors.toList());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fbtm = new FileBackedTasksManager(file.getName());
        String content;
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        String[] tasksAndHistory = content.split("\n\n");
        String[] tasks = tasksAndHistory[0].split("\n");
        fbtm.restoreMaps(tasks);
        fbtm.idCounter = fbtm.getAllTasksSortedById().stream().map(Task::getId).max(Integer::compareTo).orElse(0);
        if (tasksAndHistory.length > 1) {
            String history = tasksAndHistory[1];
            fbtm.restoreHistory(history);
        }
        fbtm.save();
        return fbtm;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceName))) {
            writer.write(TABLE_HEADER);
            List<Task> tasks = getAllTasksSortedById();
            for (Task task : tasks) {
                writer.write(toString(task));
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    protected void restoreHistory(String history) {
        List<Integer> historyList = historyFromString(history);
        for (Integer id : historyList) {
            super.getTaskById(id);
            super.getEpicById(id);
            super.getSubtaskById(id);
        }
    }

    protected void restoreMaps(String[] tasks) {
        for (int i = 1; i < tasks.length; i++) {
            Task task = fromString(tasks[i]);
            int id = task.getId();
            if (task instanceof Epic) {
                Epic epic = (Epic) task;
                epicMap.put(id, epic);
            } else if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                subtaskMap.put(id, subtask);
                prioritizedTasks.add(subtask);
                updateIntersectionWhenTaskAdded(subtask);
                determineEpicFields(subtask.getEpicId());
            } else {
                taskMap.put(id, task);
                prioritizedTasks.add(task);
                updateIntersectionWhenTaskAdded(task);
            }
        }
    }

    private Task fromString(String value) {
        String[] el = value.split(",");
        if (Type.valueOf(el[TYPE]).equals(TASK)) {
            return new Task(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Long.parseLong(el[DURATION]), LocalDateTime.parse(el[START_TIME]));
        }
        if (Type.valueOf(el[TYPE]).equals(EPIC)) {
            return new Epic(Integer.parseInt(el[ID]), el[NAME], el[DESCRIPTION]);
        }
        return new Subtask(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Long.parseLong(el[DURATION]), LocalDateTime.parse(el[START_TIME]), Integer.parseInt(el[PARENT_EPIC]));
    }

    protected String toString(Task task) {
        StringBuilder taskToString = new StringBuilder().append(task.getId()).append(",").append(task.getType()).append(",").append(task.getName()).append(",").append(task.getStatus()).append(",").append(task.getDescription()).append(",").append(task.getDuration().getSeconds() / 60).append(",").append(task.getStartTime()).append(",");
        if (task instanceof Subtask) {
            taskToString.append(((Subtask) task).getEpicId());
        }
        return taskToString.toString();
    }

    protected List<Task> getAllTasksSortedById() {
        return Stream.concat(Stream.concat(taskMap.values().stream(), epicMap.values().stream()), subtaskMap.values().stream()).sorted(Comparator.comparingInt(Task::getId)).collect(Collectors.toList());
    }

    /*    public static void main(String[] args) {
            long start = System.nanoTime();
            FileBackedTasksManager taskManager = new FileBackedTasksManager("history.csv");

            fillData(taskManager);

            TaskManager fbtm = FileBackedTasksManager.loadFromFile(new File("history.csv"));

            System.out.println("\nВосстановленный менеджер равен сохранённому:");
            System.out.println(fbtm.equals(taskManager));
            System.out.println("\nСписок восстановленных задач:");
            printTasks(fbtm);
            System.out.println("\nВосстановленная история просмотров:");
            for (Task task : fbtm.getHistory()) {
                System.out.println(task);
            }

            System.out.println("\nСледующая задача будет создана с id=8");
            Epic epic3 = new Epic("Epic3", "Epic3 description");
            fbtm.addEpic(epic3);
            System.out.println(fbtm.getEpicById(8));

            System.out.println("\nСписок задач и подзадач в порядке приоритета:");
            for (Task task : taskManager.getPrioritizedTasks()) {
                System.out.println(task);
            }

            long finish = System.nanoTime();
            System.out.println("\nМетод 'main' выполнен за " + (finish - start) / 1000000 + " миллисекунд");
        }*/
}