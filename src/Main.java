// Вопросы конечно есть, по ТЗ, надеюсь из твоих комментов они решатся) тяжкий у тебя труд конечно :)
public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Купить автомобиль", "Для семейных целей", manager.getIdCounter(), Task.NEW);
        manager.addTask(task1);

        Task task2 = new Task("Продать квартиру", "Пока цена хорошая", manager.getIdCounter(), Task.NEW);
        manager.addTask(task2);

        Epic epic1 = new Epic("Дела на даче", "Летние работы", manager.getIdCounter(), Task.NEW);
        manager.addTask(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", manager.getIdCounter(), Task.NEW, epic1);
        manager.addTask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", manager.getIdCounter(), Task.NEW, epic1);
        manager.addTask(subtask12);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию", manager.getIdCounter(), Task.NEW);
        manager.addTask(epic2);
        Subtask subtask21 = new Subtask("Занятия по боксу", "Сводить, понравится ли", manager.getIdCounter(), Task.NEW, epic2);
        manager.addTask(subtask21);

        System.out.println(manager.getAllTasks());

        manager.setStatus(1, Task.DONE);
        manager.setStatus(2, Task.IN_PROGRESS);
        manager.setStatus(4, Task.DONE);
        manager.setStatus(5, Task.DONE);
        manager.setStatus(7, Task.IN_PROGRESS);

        System.out.println(manager.getAllTasks());

        manager.deleteTask(1);
        manager.deleteTask(3);

        System.out.println(manager.getAllTasks());
    }
}
