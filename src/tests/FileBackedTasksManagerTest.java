package tests;

import managers.task.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Task task = newTask();
        taskManager.addTask(task);
        taskManager.save();
        String content = Files.readString(Path.of(filename));

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,TASK,Test Task1,NEW,Test Task description1,1,2022-01-15T01:00,\n\n", content, "Список истории не пуст.");
    }

    @Test
    void saveOnlyEpicFile() throws IOException {
        Epic epic = newEpic();
        taskManager.addEpic(epic);
        taskManager.getEpicById(taskManager.getEpics().get(0).getId());
        taskManager.save();
        String content = Files.readString(Path.of(filename));

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,EPIC,Test Epic1,NEW,Test Epic description1,0,null,\n\n" + "1", content, "Список истории пуст.");
    }

    @Test
    void loadFromEmptyTasksFile() throws IOException {
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Subtask> subtasks = restoredManager.getSubtasks();
        final List<Epic> epics = restoredManager.getEpics();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n\n", content, "Список задач не пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(0, tasks.size(), "Список задач не пуст.");
        assertEquals(0, subtasks.size(), "Список подзадач не пуст.");
        assertEquals(0, epics.size(), "Список эпиков не пуст.");
    }

    @Test
    void loadFromEmptyHistoryFile() throws IOException {
        Task task = newTask();
        taskManager.addTask(task);
        taskManager.save();
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Task> tasks = restoredManager.getTasks();
        final List<Task> history = restoredManager.getHistory();

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,TASK,Test Task1,NEW,Test Task description1,1,2022-01-15T01:00,\n\n", content, "Список истории не пуст.");
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertNotNull(history, "История на возвращается.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(0, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    void loadFromOnlyEpicFile() throws IOException {
        Epic epic = newEpic();
        taskManager.addEpic(epic);
        taskManager.getEpicById(taskManager.getEpics().get(0).getId());
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));
        String content = Files.readString(Path.of(filename));

        final List<Epic> epics = restoredManager.getEpics();

        Epic loadedEpic = epics.get(0);

        assertEquals("id,type,name,status,description,duration,startTime,epic\n" + "1,EPIC,Test Epic1,NEW,Test Epic description1,0,null,\n\n" + "1", content, "Список истории пуст.");
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, loadedEpic, "Список задач не совпадает.");
    }
}