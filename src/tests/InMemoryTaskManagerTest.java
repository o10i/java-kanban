package tests;

import managers.task.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void newManager() {
        taskManager = new InMemoryTaskManager();
    }
}