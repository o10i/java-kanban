import manager.Manager;
import tasks.Epic;
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

        manager.setStatus(1, Task.DONE);
        manager.setStatus(2, Task.IN_PROGRESS);
        manager.setStatus(4, Task.DONE);
        manager.setStatus(5, Task.DONE);
        manager.setStatus(7, Task.IN_PROGRESS);
        manager.setStatus(6, Task.DONE); // проверка бездействия метода у Epic

        System.out.println(manager.getAllTasks());

        manager.deleteTask(1);
        manager.deleteTask(3);
/*
        Ниже код для проверки обновления

        Epic epic3 = new Epic("Досуг дочери", "Найти подходящую секцию");
        manager.updateTask(epic3, epic2.getId());
        Subtask subtask31 = new Subtask("Занятия по кексу", "Сводить, понравится ли", epic2.getId());
        manager.updateTask(subtask31, subtask21.getId());
*/
        System.out.println(manager.getAllTasks());
    }
}
