import manager.Manager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Купить автомобиль", "Для семейных целей");
        manager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая");
        manager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы");
        manager.addTask(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", epic1.getId());
        manager.addTask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", epic1.getId());
        manager.addTask(subtask12);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию");
        manager.addTask(epic2);
        Subtask subtask21 = new Subtask("Занятия по боксу", "Сводить, понравится ли", epic2.getId());
        manager.addTask(subtask21);

        System.out.println(manager.getAllTasks());

        manager.setStatus(1, Status.DONE);
        manager.setStatus(2, Status.IN_PROGRESS);
        manager.setStatus(4, Status.DONE);
        manager.setStatus(5, Status.IN_PROGRESS);
        manager.setStatus(7, Status.IN_PROGRESS);
        manager.setStatus(3, Status.IN_PROGRESS); // не подействует
        System.out.println(manager.getAllTasks());

        manager.deleteTask(5);
        System.out.println(manager.getTask(3)); // статус меняется на DONE

        manager.deleteTask(7);
        System.out.println(manager.getTask(6)); // статус меняется на NEW

        manager.updateTask(new Subtask("Огород", "Вспахать огород", 4, epic1.getId()));
        System.out.println(manager.getTask(3)); // статус меняется на NEW
    }
}
