package managers;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String fileName;
    private final int ID = 0;
    private final int TYPE = 1;
    private final int NAME = 2;
    private final int STATUS = 3;
    private final int DESCRIPTION = 4;
    private final int EPIC = 5;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = new FileBackedTasksManager("history.csv");

        Task task1 = new Task("Купить автомобиль", "Для семейных целей");
        taskManager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы");
        taskManager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", epic1.getId());
        taskManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", epic1.getId());
        taskManager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Черешня", "Собрать черешню", epic1.getId());
        taskManager.addSubtask(subtask13);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию");
        taskManager.addEpic(epic2);

        long start = System.nanoTime();

        System.out.println(taskManager.getHistory());
        taskManager.getEpic(7);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtask(5);
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(3);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtask(4);
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(3);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(2);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(7);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(2);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtask(6);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(2);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println(taskManager.getHistory());

        taskManager.deleteTask(1);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(3);
        System.out.println(taskManager.getHistory());

        long finish = System.nanoTime();
        System.out.println();
        System.out.println((finish - start) / 1000000 + " миллисекунд");

        taskManager = loadFromFile(new File("history.csv"));
        System.out.println(taskManager.getHistory());
    }

    private static String historyToString(HistoryManager manager) {
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
        List<Integer> history = new ArrayList<>();
        for (String s : el) {
            history.add(Integer.parseInt(s));
        }
        return history;
    }

    private static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fbtm = new FileBackedTasksManager(file.getName());
        String content;
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        String[] tasksAndHistory = content.split("\n\n");
        String[] tasks = tasksAndHistory[0].split("\n");
        String history = tasksAndHistory[1];
        fbtm.restoreMaps(tasks);
        fbtm.restoreHistory(tasks, history);
        fbtm.save(); // чтобы история перезаписалась в файл, а не только таски
        return fbtm;
    }

    @Override
    public Task getTask(Integer id) {
        if (taskMap.containsKey(id)) {
            historyManager.add(taskMap.get(id));
            save();
            return taskMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        if (epicMap.containsKey(id)) {
            historyManager.add(epicMap.get(id));
            save();
            return epicMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        if (subtaskMap.containsKey(id)) {
            historyManager.add(subtaskMap.get(id));
            save();
            return subtaskMap.get(id);
        }
        return null;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void setStatus(Integer id, Status status) {
        super.setStatus(id, status);
        save();
    }

    private void restoreMaps(String[] tasks) {
        for (int i = 1; i < tasks.length; i++) {
            Task t = fromString(tasks[i]);
            if (t instanceof Epic) {
                addEpic((Epic) t);
            } else if (t instanceof Subtask) {
                addSubtask((Subtask) t);
            } else {
                addTask(t);
            }
        }
    }

    private void restoreHistory(String[] tasks, String history) {
        List<Integer> historyList = historyFromString(history);
        for (Integer id : historyList) {
/*            super.getTask(id);
            super.getEpic(id);
            super.getSubtask(id);*/
            // Не работает такая логика, вот у меня например история просмотра двух задач это [2, 7]
            // Восстанавливается история, их индексы становятся 1, 2 и поэтому 7-ая старая задача даже не добавляется в историю
            for (int i = 1; i < tasks.length; i++) {
                Task oldTask = fromString(tasks[i]);
                for (Task newTask : getAllTasks()) {
                    if (oldTask.getId() == id) {
                        if (newTask.getTitle().equals(oldTask.getTitle())) {
                            historyManager.add(newTask);
                        }
                    }
                }
            }
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("id,type,name,status,description,epic\n");
            List<Task> tasks = getAllTasks();
            tasks.sort(Comparator.comparingInt(Task::getId));
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

    private String toString(Task task) {
        StringBuilder taskToString = new StringBuilder();
        taskToString.append(task.getId());
        taskToString.append(",");
        taskToString.append(task.getType());
        taskToString.append(",");
        taskToString.append(task.getTitle());
        taskToString.append(",");
        taskToString.append(task.getStatus());
        taskToString.append(",");
        taskToString.append(task.getDescription());
        taskToString.append(",");
        if (task instanceof Subtask) {
            taskToString.append(((Subtask) task).getParentEpicId());
        }
        return taskToString.toString();
    }

    private Task fromString(String value) {
        String[] el = value.split(",");
        if (Type.valueOf(el[TYPE]).equals(Type.TASK)) {
            return new Task(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION]);
        }
        if (Type.valueOf(el[TYPE]).equals(Type.EPIC)) {
            return new Epic(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION]);
        }
        return new Subtask(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Integer.parseInt(el[EPIC]));
    }
}
