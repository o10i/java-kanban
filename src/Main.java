import api.KVServer;
import managers.Managers;
import managers.task.HTTPTaskManager;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

// Привет! Рад знакомству) 7-ой спринт предыдущим ревьюером был практически не проверен,
// прошу по возможности проверить тщательно, дотошно, я буду только рад, даже если это займёт больше времени)
// спасибо!
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // запуск секундомера
        long start = System.nanoTime();

        // создание сервера и менеджера
        new KVServer().start();
        TaskManager taskManager = Managers.getDefault();

        // создание и задач
        fillTasks(taskManager);

        System.out.println("\nСписок созданных задач:");
        printTasks(taskManager);

        // создание истории просмотров
        fillHistory(taskManager);
        System.out.println("\nИстория просмотров (5, 4, 3, 7, 6, 2, 1):");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        // восстановление из файла
        TaskManager restoredTaskManager = HTTPTaskManager.loadFromServer("http://localhost:8078", "1");

        System.out.println("\nВосстановленный менеджер равен сохранённому:");
        System.out.println(restoredTaskManager.equals(taskManager));
        System.out.println("\nСписок восстановленных задач:");
        printTasks(taskManager);
        System.out.println("\nВосстановленная история просмотров:");
        for (Task task : restoredTaskManager.getHistory()) {
            System.out.println(task);
        }

        // проверка idCounter после восстановления
        System.out.println("\nСледующая задача будет создана с id=8");
        Epic epic3 = new Epic("Epic id=8", "Description Epic id=8");
        taskManager.addEpic(epic3);
        System.out.println(taskManager.getEpicById(8));

        // список задач и подзадач в порядке приоритета
        System.out.println("\nСписок задач и подзадач в порядке приоритета:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        // остановка секундомера
        long finish = System.nanoTime();
        System.out.println("\nМетод 'main' выполнен за " + (finish - start) / 1000000 + " миллисекунд");

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
    }

    private static void fillHistory(TaskManager taskManager) {
        taskManager.getEpicById(7);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(7);
        taskManager.getTaskById(2);
        taskManager.getSubtaskById(6);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
    }

    private static void fillTasks(TaskManager taskManager) {
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
    }

    private static void printTasks(TaskManager taskManager) {
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
    }
}