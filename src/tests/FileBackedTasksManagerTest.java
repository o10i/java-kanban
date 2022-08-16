package tests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    void newManager() {
        taskManager = new FileBackedTasksManager("history.csv");
    }
}