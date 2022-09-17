package ru.yandex.practicum;

import ru.yandex.practicum.managers.task.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Epic;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setUp() {
        taskManager = new FileBackedTasksManager(filename);
    }

    @Test
    void saveEmptyTasksFile() {
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void saveEmptyHistoryFile() {
        addTasks();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void saveOnlyEpicFile() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void loadFromEmptyTasksFile() {
        taskManager.save();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void loadFromOnlyEpicFile() {
        epicByDefault = new Epic("Test epic", "Test epic description");
        int epicByDefaultId = taskManager.addEpic(epicByDefault);
        taskManager.getEpicById(epicByDefaultId);

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }

    @Test
    void loadFromEmptyHistoryFile() {
        addTasks();

        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(new File(filename));

        assertEquals(taskManager, restoredManager, "Менеджеры не равны");
    }
}