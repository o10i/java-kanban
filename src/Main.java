import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Купить автомобиль", "Для семейных целей");
        inMemoryTaskManager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая");
        inMemoryTaskManager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы");
        inMemoryTaskManager.addTask(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", epic1.getId());
        inMemoryTaskManager.addTask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", epic1.getId());
        inMemoryTaskManager.addTask(subtask12);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию");
        inMemoryTaskManager.addTask(epic2);
        Subtask subtask21 = new Subtask("Занятия по боксу", "Сводить, понравится ли", epic2.getId());
        inMemoryTaskManager.addTask(subtask21);

        System.out.println(inMemoryTaskManager.getAllTasks());

        inMemoryTaskManager.setStatus(1, Status.DONE);
        inMemoryTaskManager.setStatus(2, Status.IN_PROGRESS);
        inMemoryTaskManager.setStatus(4, Status.DONE);
        inMemoryTaskManager.setStatus(5, Status.DONE);
        inMemoryTaskManager.setStatus(7, Status.IN_PROGRESS);

        System.out.println(inMemoryTaskManager.getAllTasks());

        inMemoryTaskManager.deleteTask(1);
        inMemoryTaskManager.deleteTask(3);

        System.out.println(inMemoryTaskManager.getAllTasks());
    }
}
