import managers.history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task taskByDefault;
    private Epic epicByDefault;
    private Subtask subtaskByDefault;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    private void addTasks() {
        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        taskByDefault.setId(1);
        historyManager.add(taskByDefault);
        epicByDefault = new Epic("Test epic", "Test epic description");
        epicByDefault.setId(2);
        historyManager.add(epicByDefault);
        subtaskByDefault = new Subtask("Test subtask", "Test subtask description", 30, LocalDateTime.now().plusDays(1), epicByDefault.getId());
        subtaskByDefault.setId(3);
        historyManager.add(subtaskByDefault);
    }
    @Test
    void add() {
        addTasks();

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "История пустая");
    }

    @Test
    void getZeroElementHistory() {
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(0, history.size(), "История не пустая");
    }

    @Test
    void getOneElementHistory() {
        taskByDefault = new Task("Test task", "Test task description", 20, LocalDateTime.now());
        taskByDefault.setId(1);
        historyManager.add(taskByDefault);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    void removeFirstTask() {
        addTasks();

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "История содержит не 3 элемента");

        historyManager.remove(1);

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(2, historyAfterRemove.size(), "Задача из истории не удалена");
        assertEquals(epicByDefault, historyAfterRemove.get(0), "Задача с начала не удалена");
    }

    @Test
    void removeMiddleTask() {
        addTasks();

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "История содержит не 3 элемента");

        historyManager.remove(2);

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(2, historyAfterRemove.size(), "Эпик из истории не удалён");
        assertEquals(subtaskByDefault, historyAfterRemove.get(1), "Эпик из истории не удалён");
    }

    @Test
    void removeLastTask() {
        addTasks();

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(3, history.size(), "История содержит не 3 элемента");

        historyManager.remove(3);

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует");
        assertEquals(2, historyAfterRemove.size(), "Подзадача из истории не удалена");
    }
}