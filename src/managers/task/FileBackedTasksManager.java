package managers.task;

import exceptions.ManagerSaveException;
import tasks.enums.Type;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.enums.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tasks.enums.Type.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String fileName;
    private final int ID = 0;
    private final int TYPE = 1;
    private final int NAME = 2;
    private final int STATUS = 3;
    private final int DESCRIPTION = 4;
    private final int DURATION = 5;
    private final int START_TIME = 6;
    private final int PARENT_EPIC = 7;
    private final String TABLE_HEADER = "id,type,name,status,description,duration,startTime,epic\n";

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        // старт секундомер
        long start = System.nanoTime();

        // создание менеджера и задач
        TaskManager taskManager = new FileBackedTasksManager("history.csv");

        Task task1 = new Task("Купить автомобиль", "Для семейных целей", 60, LocalDateTime.of(2022, 1, 1, 0, 0));
        taskManager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая", 60, LocalDateTime.of(2022, 1, 1, 1, 0));
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы");
        taskManager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", 30, LocalDateTime.of(2022, 12, 31, 23, 30), epic1.getId());
        taskManager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", 30, LocalDateTime.of(2022, 1, 1, 2, 0), epic1.getId());
        taskManager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Черешня", "Собрать черешню", 30, LocalDateTime.of(2022, 1, 1, 2, 30), epic1.getId());
        taskManager.addSubtask(subtask13);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию");
        taskManager.addEpic(epic2);

        System.out.println("Список созданных задач в порядке id:");
        for (Task task : taskManager.getAllTasksSortedById()) {
            System.out.println(task);
        }

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
        System.out.println("\nИстория просмотров (5, 4, 3, 7, 6, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

/*        // обновление задач
        Task updatedTask = new Task("Обновлённая задача", "Для проверки", 60, LocalDateTime.of(2022, 1, 1, 0, 0));
        updatedTask.setId(1);
        taskManager.updateTask(updatedTask);
        Epic updatedEpic = new Epic("Обновлённый эпик", "Для проверки");
        updatedEpic.setId(3);
        taskManager.updateEpic(updatedEpic);
        Subtask updatedSubtask = new Subtask("Обновлённая подзадача", "Для проверки", 30, LocalDateTime.of(2022, 12, 31, 23, 30), epic1.getId());
        updatedSubtask.setId(4);
        taskManager.updateSubtask(updatedSubtask);
        System.out.println("\nСписок обновлённых задач id=[1,3,4]:");
        for (Task task : taskManager.getAllTasksSortedById()) {
            System.out.println(task);
        }
        System.out.println("\nИстория просмотров после обновления задач, не должна меняться (5, 4, 3, 7, 6, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }


        // проверка полей эпика и истории просмотров после удаление подзадачи
        taskManager.deleteTask(4);
        System.out.println("\nПроверка изменения полей эпика (id=3) после удаление подзадачи (id=4):");
        System.out.println(taskManager.getEpic(3));
        System.out.println("\nИстория просмотров (5, 7, 6, 2, 1, 3):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        // проверка на изменение статуса эпика
        taskManager.setStatus(5, Status.DONE);
        taskManager.setStatus(6, Status.DONE);
        System.out.println("\nПроверка изменения статуса эпика (id=3) после изменения статуса его подзадач на DONE:");
        System.out.println(taskManager.getEpic(3));

        // удаление задач и проверка списка задач и истории просмотров
        taskManager.deleteTask(3);
        System.out.println("\nСписок задач (удалён id=3 и его подзадачи id=[5, 6]):");
        for (Task task : taskManager.getAllTasksSortedById()) {
            System.out.println(task);
        }
        System.out.println("\nИстория просмотров (7, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        taskManager.deleteAllTasks();
        System.out.println("\nСписок задач (удалены все задачи):");
        System.out.println(taskManager.getAllTasks()); // []
        System.out.println("История просмотров (удалена вся история):");
        System.out.println(taskManager.getHistory()); // []*/

        // восстановление из файла
        TaskManager restoredTaskManager = loadFromFile(new File("history.csv"));

        System.out.println("\nВосстановленный менеджер равен сохранённому:");
        System.out.println(restoredTaskManager.equals(taskManager));
        System.out.println("\nСписок восстановленных задач:");
        for (Task task : restoredTaskManager.getAllTasksSortedById()) {
            System.out.println(task);
        }
        System.out.println("\nВосстановленная история просмотров:");
        for (Task task : restoredTaskManager.getHistory()) {
            System.out.println(task);
        }

        // проверка idCounter после восстановления
        System.out.println("\nСледующая задача будет создана с id=" + taskManager.getIdCounter());
        Epic epic3 = new Epic("Epic id=8", "Description Epic id=8");
        taskManager.addEpic(epic3);
        System.out.println(taskManager.getEpic(8));

        // список задач и подзадач в порядке приоритета
        System.out.println("\nСписок задач и подзадач в порядке приоритета:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        // стоп секундомер
        long finish = System.nanoTime();
        System.out.println("\nМетод 'main' выполнен за " + (finish - start) / 1000000 + " миллисекунд");
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
        fbtm.idCounter = fbtm.getAllTasksSortedById().stream().map(Task::getId).max(Integer::compareTo).orElse(0) + 1;
        if (tasksAndHistory.length > 1) {
            String history = tasksAndHistory[1];
            fbtm.restoreHistory(history);
        }
        fbtm.save();
        return fbtm;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
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
            Task task = fromString(tasks[i]);
            if (task instanceof Epic) {
                updateEpic((Epic) task);
            } else if (checkAndUpdateIntersectionWhenTaskAdded(task)) {
                if (task instanceof Subtask) {
                    updateSubtask((Subtask) task);
                } else {
                    updateTask(task);
                }
            } else {
                throw new IllegalArgumentException("Обнаружено пересечение задач, задача '" + task.getName() + "' не добавлена.");
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

    private String toString(Task task) {
        StringBuilder taskToString = new StringBuilder()
                .append(task.getId())
                .append(",")
                .append(task.getType())
                .append(",")
                .append(task.getName())
                .append(",")
                .append(task.getStatus())
                .append(",")
                .append(task.getDescription())
                .append(",")
                .append(task.getDuration().getSeconds() / 60)
                .append(",")
                .append(task.getStartTime())
                .append(",");
        if (task instanceof Subtask) {
            taskToString.append(((Subtask) task).getParentEpicId());
        }
        return taskToString.toString();
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
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void setStatus(Integer id, Status status) {
        super.setStatus(id, status);
        save();
    }
}