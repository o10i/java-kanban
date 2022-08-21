package tests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    void newManager() {
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
        Task task = new Task("Test saveEmptyHistoryFile", "Test saveEmptyHistoryFile description", 1, LocalDateTime.of(2022, 2, 22, 0, 0));
        taskManager.addTask(task);
        taskManager.save();
        String content = Files.readString(Path.of(filename));

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" +
                "1,TASK,Test saveEmptyHistoryFile,NEW,Test saveEmptyHistoryFile description,1,2022-02-22T00:00,\n\n", content, "Список истории не пуст.");
    }

    @Test
    void saveOnlyEpicFile() throws IOException {
        Epic epic = new Epic("Test saveOnlyEpicFile", "Test saveOnlyEpicFile description");
        taskManager.addEpic(epic);
        taskManager.getEpic(taskManager.getEpics().get(0).getId());
        taskManager.save();
        String content = Files.readString(Path.of(filename));

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" +
                "1,EPIC,Test saveOnlyEpicFile,NEW,Test saveOnlyEpicFile description,0,null,\n\n" + "1",
                content, "Список истории пуст.");
    }

    @Test
    void loadFromEmptyTasksFile() throws IOException {
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));
        final List<Task> tasks = restoredManager.getAllTasks();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n\n", content, "Список задач не пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(0, tasks.size(), "Список задач не пуст.");
    }

    @Test
    void loadFromEmptyHistoryFile() throws IOException {
        Task task = new Task("Test saveEmptyHistoryFile", "Test saveEmptyHistoryFile description", 1, LocalDateTime.of(2022, 2, 22, 0, 0));
        taskManager.addTask(task);
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));
        final List<Task> tasks = restoredManager.getAllTasks();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" +
                "1,TASK,Test saveEmptyHistoryFile,NEW,Test saveEmptyHistoryFile description,1,2022-02-22T00:00,\n\n",
                content, "Список истории не пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Список задач не совпадает.");
    }

    @Test
    void loadFromOnlyEpicFile() throws IOException {
        Epic epic = new Epic("Test saveOnlyEpicFile", "Test saveOnlyEpicFile description");
        taskManager.addEpic(epic);
        taskManager.getEpic(taskManager.getEpics().get(0).getId());
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));
        final List<Task> tasks = restoredManager.getAllTasks();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" +
                "1,EPIC,Test saveOnlyEpicFile,NEW,Test saveOnlyEpicFile description,0,null,\n\n" + "1",
                content, "Список истории пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Список задач не совпадает.");
    }
}