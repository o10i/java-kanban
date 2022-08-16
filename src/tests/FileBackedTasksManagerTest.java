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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    void newManager() {
        taskManager = new FileBackedTasksManager("history.csv");
    }

    @Test
    void saveEmptyTasksFile() throws IOException {
        taskManager.save();
        String content = Files.readString(Path.of("history.csv"));
        assertEquals("id,type,name,status,description,epic\n\n", content, "Список задач не пуст.");
    }

    @Test
    void saveEmptyHistoryFile() throws IOException {
        Task task = new Task("Test saveEmptyHistoryFile", "Test saveEmptyHistoryFile description");
        taskManager.addTask(task);

        taskManager.save();
        String content = Files.readString(Path.of("history.csv"));

        assertEquals("id,type,name,status,description,epic\n" +
                "1,TASK,Test saveEmptyHistoryFile,NEW,Test saveEmptyHistoryFile description,\n\n", content, "Список истории не пуст.");
    }

    @Test
    void saveOnlyEpicFile() throws IOException {
        Epic epic = new Epic("Test saveOnlyEpicFile", "Test saveOnlyEpicFile description");
        final int epicId = taskManager.addEpic(epic);
        taskManager.getEpic(epicId);

        taskManager.save();
        String content = Files.readString(Path.of("history.csv"));

        assertEquals("id,type,name,status,description,epic\n" +
                "1,EPIC,Test saveOnlyEpicFile,NEW,Test saveOnlyEpicFile description,\n\n" +
                "1", content, "Список истории пуст.");
    }

    @Test
    void loadFromEmptyTasksFile() throws IOException {
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File("history.csv"));
        String content = Files.readString(Path.of("history.csv"));

        assertEquals("id,type,name,status,description,epic\n\n", content, "Список задач не пуст.");

        final List<Task> tasks = restoredManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(0), "Список задач не пуст.");
    }

    @Test
    void loadFromEmptyHistoryFile() throws IOException {
        Task task = new Task("Test saveEmptyHistoryFile", "Test saveEmptyHistoryFile description");
        taskManager.addTask(task);
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File("history.csv"));
        String content = Files.readString(Path.of("history.csv"));

        assertEquals("id,type,name,status,description,epic\n" +
                "1,TASK,Test saveEmptyHistoryFile,NEW,Test saveEmptyHistoryFile description,\n\n", content, "Список истории не пуст.");

        final List<Task> tasks = restoredManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Список задач не совпадает.");
    }

    @Test
    void loadFromOnlyEpicFile() throws IOException {
        Epic epic = new Epic("Test saveOnlyEpicFile", "Test saveOnlyEpicFile description");
        final int epicId = taskManager.addEpic(epic);
        taskManager.getEpic(epicId);

        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File("history.csv"));
        String content = Files.readString(Path.of("history.csv"));

        assertEquals("id,type,name,status,description,epic\n" +
                "1,EPIC,Test saveOnlyEpicFile,NEW,Test saveOnlyEpicFile description,\n\n" +
                "1", content, "Список истории пуст.");

        final List<Task> tasks = restoredManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Список задач не совпадает.");
    }
}