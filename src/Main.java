// Вопросы конечно есть, по ТЗ, надеюсь из твоих комментов они решатся)
public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Дела на даче", "Летние работы", manager.getIdCounter(), Task.NEW);
        manager.addTask(epic1);
        Subtask subtask11 = new Subtask("Вишня", "Собрать вишню", manager.getIdCounter(), Task.DONE, epic1);
        manager.addTask(subtask11);
        Subtask subtask12 = new Subtask("Огород", "Вспахать огород", manager.getIdCounter(), Task.NEW, epic1);
        manager.addTask(subtask12);

        Epic epic2 = new Epic("Досуг сына", "Найти подходящую секцию", manager.getIdCounter(), Task.NEW);
        manager.addTask(epic2);
        Subtask subtask21 = new Subtask("Занятия по боксу", "Сводить, понравится ли", manager.getIdCounter(), Task.NEW, epic2);
        manager.addTask(subtask21);

//        manager.deleteAllTasks();

//        System.out.println(manager.getTask(5).getTitle());
/*
        Subtask subtaskUpdated = new Subtask("Подзадача измененная", "Описание задачи изменённой", 3, epic1);
        manager.updateTask(subtaskUpdated);

        Epic epicUpdated = new Epic("Эпик изменённый", "Изменённые работы", 4);
        manager.updateTask(epicUpdated);
*/
//        manager.deleteTask(2);

        //manager.printEpicSubtasks(epic1);

        //System.out.println(manager.getStatus(1));

        manager.printAllTasks();
    }
}
