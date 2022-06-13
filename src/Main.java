public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.addTask(new Epic("Важный эпик 1", "Описание 1", manager.getIdCounter()));
        manager.addTask(new Subtask("Задача 1", "Описание задачи 1", manager.getIdCounter(), "Важный эпик 1"));
        manager.addTask(new Subtask("Задача 2", "Описание задачи 2", manager.getIdCounter(), "Важный эпик 1"));

        manager.addTask(new Epic("Важный эпик 2", "Описание 2", manager.getIdCounter()));
        manager.addTask(new Subtask("Задача 1", "Описание задачи 1", manager.getIdCounter(), "Важный эпик 2"));

        //manager.deleteAllTasks();

        manager.printAllTasks();
/*
        System.out.println(manager);*/
    }
}
