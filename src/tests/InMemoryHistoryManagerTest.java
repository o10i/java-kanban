package tests;

import managers.history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task;

    @BeforeEach
    void newManager() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Test Task", "Test Task description", 1,
                1, LocalDateTime.of(2021, 1, 1, 0, 0));
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(1, history.size(), "История пустая.");
    }

    @Test
    void addDouble() {
        historyManager.add(task);

        Task taskDouble = new Task("Test addDouble", "Test addDouble description", 1,
                1, LocalDateTime.of(2021, 1, 1, 0, 0));
        historyManager.add(taskDouble);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(1, history.size(), "Дубликат задачи не заменён.");
    }

    @Test
    void removeFirstTask() {
        historyManager.add(task);

        Task task2 = new Task("Test removeFirstTask", "Test removeFirstTask description", 2);
        historyManager.add(task2);
        Task task3 = new Task("Test removeFirstTask", "Test removeFirstTask description", 3);
        historyManager.add(task3);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(3, history.size(), "История содержит не 3 элемента.");

        historyManager.remove(task.getId());

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(2, historyAfterRemove.size(), "Задача из истории не удалена.");
        assertEquals(task2, historyAfterRemove.get(0), "Задача с начала не удалена");
    }

    @Test
    void removeMiddleTask() {
        historyManager.add(task);

        Task task2 = new Task("Test removeMiddleTask", "Test removeMiddleTask description", 2,
                2, LocalDateTime.of(2022, 2, 2, 0, 0));
        historyManager.add(task2);
        Task task3 = new Task("Test removeMiddleTask", "Test removeMiddleTask description", 3,
                3, LocalDateTime.of(2023, 3, 3, 0, 0));
        historyManager.add(task3);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(3, history.size(), "История содержит не 3 элемента.");

        historyManager.remove(task2.getId());

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(2, historyAfterRemove.size(), "Задача из истории не удалена.");
        assertEquals(task3, historyAfterRemove.get(1), "Задача с начала не удалена");
    }

    @Test
    void removeLastTask() {
        historyManager.add(task);

        Task task2 = new Task("Test removeLastTask", "Test removeLastTask description", 2,
                2, LocalDateTime.of(2022, 2, 2, 0, 0));
        historyManager.add(task2);
        Task task3 = new Task("Test removeLastTask", "Test removeLastTask description", 3,
        3, LocalDateTime.of(2023, 3, 3, 0, 0));
        historyManager.add(task3);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(3, history.size(), "История содержит не 3 элемента.");

        historyManager.remove(task3.getId());

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(2, historyAfterRemove.size(), "Задача из истории не удалена.");
        assertThrows(IndexOutOfBoundsException.class, () -> historyAfterRemove.get(2), "Задача с конца не удалена");
    }

    @Test
    void getZeroElementHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertThrows(IndexOutOfBoundsException.class, () -> history.get(0), "Элемент не должен возвращаться.");
    }

    @Test
    void getOneElementHistory() {
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}