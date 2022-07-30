package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


// сделал - как понял, а как понял - как уж понял :D уверен, можно куда лучше написать
public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    String fileName;

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

/*        taskManager.deleteTask(1);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(3);
        System.out.println(taskManager.getHistory());*/

        long finish = System.nanoTime();
        System.out.println();
        System.out.println((finish - start) / 1000000 + " миллисекунд");

        taskManager = loadFromFile(new File("history.csv"));
        System.out.println(taskManager.getHistory());
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> list = manager.getHistory();
        String history = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) history += list.get(i).getId();
            else history += list.get(i).getId() + ",";
        }
        return history;
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
        FileBackedTasksManager fbtm = new FileBackedTasksManager("history.csv");
        String content;
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        String[] tasksAndHistory = content.split("\n\n");
        String[] tasks = tasksAndHistory[0].split("\n");
        String history = tasksAndHistory[1];
        boolean isNotFirstLine = false;
        for (String task : tasks) {
            if (isNotFirstLine) { // пропуск первой строки
                Task t = fbtm.fromString(task);
                if (t instanceof Epic) fbtm.updateEpic((Epic) t); // через update, потому что через add нумерация id будет сбиваться на counter++
                else if (t instanceof Subtask) fbtm.updateSubtask((Subtask) t);
                else fbtm.updateTask(t);
            } else isNotFirstLine = true;
        }
        List<Integer> historyList = historyFromString(history);
        for (Integer i : historyList) {
            fbtm.getTask(i);
            fbtm.getEpic(i);
            fbtm.getSubtask(i);
        }
        return fbtm;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : super.getAllTasks()) {
                fileWriter.write(toString(task) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(super.getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private Type getType(Task task) {
        if (task instanceof Epic) return Type.EPIC;
        if (task instanceof Subtask) return Type.SUBTASK;
        return Type.TASK;
    }

    private String toString(Task task) {
        String taskToString = task.getId() + "," + getType(task) + "," + task.getTitle() + "," + task.getStatus()
                + "," + task.getDescription() + ",";
        if (task instanceof Subtask) taskToString += ((Subtask) task).getParentEpicId();
        return taskToString;
    }

    private Task fromString(String value) {
        String[] el = value.split(",");
        Status status = null;
        switch (el[3]) {
            case "NEW":
                status = Status.NEW;
                break;
            case "IN_PROGRESS":
                status = Status.IN_PROGRESS;
                break;
            case "DONE":
                status = Status.DONE;
                break;
        }
        if (el[1].equals(Type.TASK.toString())) return new Task(Integer.parseInt(el[0]), el[2], status, el[4]);
        if (el[1].equals(Type.EPIC.toString())) return new Epic(Integer.parseInt(el[0]), el[2], status, el[4]);
        return new Subtask(Integer.parseInt(el[0]), el[2], status, el[4], Integer.parseInt(el[5]));
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
}
