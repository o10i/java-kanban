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
import java.time.LocalDateTime;
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
    private final int DURATION = 5;
    private final int START_TIME = 6;
    private final int EPIC = 7;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        // создание менеджера и задач
        TaskManager taskManager = new FileBackedTasksManager("history.csv");

        Task task1 = new Task("Купить автомобиль", "Для семейных целей", 30, LocalDateTime.of(2022, 8, 19, 0, 0));
        taskManager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая", 40, LocalDateTime.of(2022, 8, 19, 0, 30));
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы");
        taskManager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", 10, LocalDateTime.of(2022, 8, 21, 0, 0), epic1.getId());
        taskManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", 15, LocalDateTime.of(2022, 8, 17, 0, 0), epic1.getId());
        taskManager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Черешня", "Собрать черешню", 20, LocalDateTime.of(2022, 8, 18, 0, 0), epic1.getId());
        taskManager.addSubtask(subtask13);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию");
        taskManager.addEpic(epic2);

        Epic epic3 = new Epic("Досуг дочери", "Найти подходящего мужа");
        taskManager.addEpic(epic3);

        // создание истории просмотров
        taskManager.getEpic(7);
        taskManager.getSubtask(5);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getEpic(3);
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getEpic(7);
        taskManager.getTask(2);
        taskManager.getSubtask(6);
        taskManager.getTask(2);
        taskManager.getTask(1);
/*        System.out.println("Список задач");
        System.out.println(taskManager.getAllTasks()); // 1..7
        System.out.println("История просмотров");
        System.out.println(taskManager.getHistory()); // 5 4 3 7 6 2 1

        // удаление задач и проверка списка задач и истории просмотров
        taskManager.deleteTask(1);
        System.out.println("\nСписок задач (удалена id=1)");
        System.out.println(taskManager.getAllTasks());  // 5 4 3 7 6 2 1
        System.out.println("История просмотров (удалена id=1)");
        System.out.println(taskManager.getHistory());

        taskManager.deleteTask(3);
        System.out.println("\nСписок задач (удалён id=3 и его подзадачи)");
        System.out.println(taskManager.getAllTasks()); // 2 7
        System.out.println("История просмотров (удалён id=3 и его подзадачи)");
        System.out.println(taskManager.getHistory()); // 7 2

        taskManager.deleteTask(7);
        taskManager.deleteTask(2);
        System.out.println("\nСписок задач (удалены все задачи)");
        System.out.println(taskManager.getAllTasks()); // []
        System.out.println("История просмотров (удалена вся история)");
        System.out.println(taskManager.getHistory()); // []

        // проверка на изменение статуса эпика
        System.out.println("\nПроверка изменения статуса эпика(id=3) после изменения статуса его сабтасок на DONE");
        System.out.println("\nПроверка изменения статуса эпика(id=3) после изменения статуса его сабтасок на DONE");
        taskManager.setStatus(4, Status.DONE);
        taskManager.setStatus(5, Status.DONE);
        taskManager.setStatus(6, Status.DONE);
        System.out.println(taskManager.getAllTasks());

        // проверка полей эпика на удаление подзадачи
        System.out.println("\nПроверка изменения полей эпика(id=3) после удаление подзадачи(id=4)");
        taskManager.deleteTask(4);
        System.out.println(taskManager.getAllTasks());

        // восстановление из файла
        TaskManager restoredTaskManager = loadFromFile(new File("history.csv"));

        System.out.println("\nВосстановленный менеджер равен сохранённому:");
        System.out.println(restoredTaskManager.equals(taskManager));
        System.out.println("\nСписок восстановленных задач:");
        System.out.println(restoredTaskManager.getAllTasks());
        System.out.println("\nВосстановленная история просмотров:");
        System.out.println(restoredTaskManager.getHistory());

        // проверка idCounter после восстановления
        System.out.println("\nСледующая задача будет создана с id=" + taskManager.getIdCounter());
        Epic epic3 = new Epic("Epic id=8", "Description Epic id=8");
        taskManager.addEpic(epic3);
        System.out.println(taskManager.getAllTasks());*/

        // список задач в порядке приоритета
        System.out.println("\nСписок задач в порядке приоритета:\n" + taskManager.getPrioritizedTasks());

/*        // измерение времени выполнения кода
        long start = System.nanoTime();
        long finish = System.nanoTime();
        System.out.println("\n" + (finish - start) / 1000000 + " миллисекунд");*/
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
        fbtm.idCounter = fbtm.getAllTasks().stream().map(Task::getId).max(Integer::compareTo).orElse(0) + 1;
        if (tasksAndHistory.length > 1) {
            String history = tasksAndHistory[1];
            fbtm.restoreHistory(history);
        }
        fbtm.save();
        return fbtm;
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
    public void setStatus(Integer id, Status status) {
        super.setStatus(id, status);
        save();
    }

    private void restoreHistory(String history) {
        List<Integer> historyList = historyFromString(history);
        for (Integer id : historyList) {
            super.getTask(id);
            super.getEpic(id);
            super.getSubtask(id);
        }
    }

    private void restoreMaps(String[] tasks) {
        for (int i = 1; i < tasks.length; i++) {
            Task t = fromString(tasks[i]);
            if (t instanceof Epic) {
                updateEpic((Epic) t);
            } else if (t instanceof Subtask) {
                updateSubtask((Subtask) t);
            } else {
                updateTask(t);
            }
        }
    }

    private Task fromString(String value) {
        String[] el = value.split(",");
        if (Type.valueOf(el[TYPE]).equals(Type.TASK)) {
            return new Task(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Long.parseLong(el[DURATION]), LocalDateTime.parse(el[START_TIME]));
        }
        if (Type.valueOf(el[TYPE]).equals(Type.EPIC)) {
            if (el[START_TIME].equals("null")) {
                return new Epic(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Long.parseLong(el[DURATION]));
            }
            return new Epic(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Long.parseLong(el[DURATION]), LocalDateTime.parse(el[START_TIME]));
        }
        return new Subtask(Integer.parseInt(el[ID]), el[NAME], Status.valueOf(el[STATUS]), el[DESCRIPTION], Long.parseLong(el[DURATION]), LocalDateTime.parse(el[START_TIME]), Integer.parseInt(el[EPIC]));
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
        taskToString.append(task.getDuration().getSeconds() / 60);
        taskToString.append(",");
        taskToString.append(task.getStartTime());
        taskToString.append(",");
        if (task instanceof Subtask) {
            taskToString.append(((Subtask) task).getParentEpicId());
        }
        return taskToString.toString();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
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
}