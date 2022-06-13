public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Дела на даче", "Летние работы", manager.getIdCounter());
        manager.addTask(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", manager.getIdCounter(), epic1);
        manager.addTask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", manager.getIdCounter(), epic1);
        manager.addTask(subtask12);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию", manager.getIdCounter());
        manager.addTask(epic2);
        Subtask subtask21 = new Subtask("Занятия по боксу", "Сводить, понравится ли", manager.getIdCounter(), epic2);
        manager.addTask(subtask21);

        //manager.deleteAllTasks();

        //System.out.println(manager.getTask(5).getTitle());

        //manager.updateTask(new Subtask("Задача измененная", "Описание задачи изменённой", 3, epic1));

        manager.printAllTasks();
    }
}
