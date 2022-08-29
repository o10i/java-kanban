package tests;

import managers.task.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tasks.enums.Status.NEW;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setUp() {
        idCounter = 0;
        taskManager = new FileBackedTasksManager(filename);
    }

    @Test
    void saveEmptyTasksFile() throws IOException {
        taskManager.save();
        String content = Files.readString(Path.of(filename));
        assertEquals("id,type,name,status,description,duration,startTime,epic\n\n", content, "Список задач не пуст.");
    }

    @Test
    void saveEmptyHistoryFile() throws IOException {
        Task task = newTask(NEW);
        taskManager.addTask(task);
        taskManager.save();
        String content = Files.readString(Path.of(filename));

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,TASK,Test Task1,NEW,Test Task description1,1,2022-01-15T01:00,\n\n", content, "Список истории не пуст.");
    }

    @Test
    void saveOnlyEpicFile() throws IOException {
        Epic epic = newEpic();
        taskManager.addEpic(epic);
        taskManager.getEpic(taskManager.getEpics().get(0).getId());
        taskManager.save();
        String content = Files.readString(Path.of(filename));

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,EPIC,Test Epic1,NEW,Test Epic description1,0,null,\n\n" + "1", content, "Список истории пуст.");
    }

    @Test
    void loadFromEmptyTasksFile() throws IOException {
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));
        final List<Task> tasks = restoredManager.getAllTasksSortedById();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n\n", content, "Список задач не пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(0, tasks.size(), "Список задач не пуст.");
    }

    @Test
    void loadFromEmptyHistoryFile() throws IOException {
        Task task = newTask(NEW);
        taskManager.addTask(task);
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));
        final List<Task> tasks = restoredManager.getAllTasksSortedById();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,TASK,Test Task1,NEW,Test Task description1,1,2022-01-15T01:00,\n\n", content, "Список истории не пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Список задач не совпадает.");
    }

    @Test
    void loadFromOnlyEpicFile() throws IOException {
        Epic epic = newEpic();
        taskManager.addEpic(epic);
        taskManager.getEpic(taskManager.getEpics().get(0).getId());
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getAllTasksSortedById();

        Epic loadedEpic = (Epic) tasks.get(0);
        loadedEpic.setStatus(NEW);

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,EPIC,Test Epic1,NEW,Test Epic description1,0,null,\n\n" + "1", content, "Список истории пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, loadedEpic, "Список задач не совпадает.");
    }
}