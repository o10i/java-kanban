package tests;

import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private static Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);


    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
    }

    @Test
    void getHistory() {
    }
}