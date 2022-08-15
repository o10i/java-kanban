import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

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
        System.out.println();

        taskManager.deleteTask(1);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(3);
        System.out.println(taskManager.getHistory());

        long finish = System.nanoTime();
        System.out.println();
        System.out.println((finish - start) / 1000000 + " миллисекунд");

        /*        System.out.println(inMemoryTaskManager.getAllTasks());

        taskManager.setStatus(1, Status.DONE);
        taskManager.setStatus(2, Status.IN_PROGRESS);
        taskManager.setStatus(4, Status.DONE);
        taskManager.setStatus(5, Status.IN_PROGRESS);
        taskManager.setStatus(7, Status.IN_PROGRESS);
        taskManager.setStatus(3, Status.IN_PROGRESS); // не подействует
        System.out.println(inMemoryTaskManager.getAllTasks());

        inMemoryTaskManager.deleteTask(5);
        System.out.println(inMemoryTaskManager.getTask(3)); // статус меняется на DONE

        inMemoryTaskManager.deleteTask(7);
        System.out.println(inMemoryTaskManager.getTask(6)); // статус меняется на NEW

        inMemoryTaskManager.updateTask(new Subtask("Огород", "Вспахать огород", 4, epic1.getId()));
        System.out.println(inMemoryTaskManager.getTask(3)); // статус меняется на NEW*/
    }
}