package tests;

import managers.history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.enums.Status.NEW;

class InMemoryHistoryManagerTest {
    private static int idCounter = 0;
    private InMemoryHistoryManager historyManager;

    private static Task newTask() {
        idCounter++;
        return new Task("Test Task" + idCounter, NEW, "Test Task description" + idCounter, idCounter, LocalDateTime.of(2022, idCounter, 14 + idCounter, idCounter, 0));
    }

    private static Epic newEpic() {
        idCounter++;
        return new Epic("Test Epic" + idCounter, "Test Epic description" + idCounter);
    }

    private static Subtask newSubtask(Status status, int epicId) {
        idCounter++;
        return new Subtask("Test Subtasks" + idCounter, status, "Test Subtasks description" + idCounter, idCounter, LocalDateTime.of(2022, idCounter, idCounter, idCounter, 0), epicId);
    }

    @BeforeEach
    void setUp() {
        idCounter = 0;
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        Task task = newTask();
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(1, history.size(), "История пустая.");
    }

    @Test
    void addDouble() {
        Task task1 = newTask();
        historyManager.add(task1);

        Task task2 = newTask();
        task2.setId(task1.getId());
        historyManager.add(task2);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(1, history.size(), "Дубликат задачи не заменён.");
    }

    @Test
    void getZeroElementHistory() {
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        //noinspection ResultOfMethodCallIgnored
        assertThrows(IndexOutOfBoundsException.class, () -> history.get(0), "Элемент не должен возвращаться.");
    }

    @Test
    void getOneElementHistory() {
        Task task = newTask();
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void removeFirstTask() {
        Task task1 = newTask();
        task1.setId(1);
        historyManager.add(task1);

        Task task2 = newTask();
        task2.setId(2);
        historyManager.add(task2);

        Task task3 = newTask();
        task3.setId(3);
        historyManager.add(task3);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(3, history.size(), "История содержит не 3 элемента.");

        historyManager.remove(task1.getId());

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(2, historyAfterRemove.size(), "Задача из истории не удалена.");
        assertEquals(task2, historyAfterRemove.get(0), "Задача с начала не удалена");
    }

    @Test
    void removeMiddleTask() {
        Task task1 = newTask();
        task1.setId(1);
        historyManager.add(task1);

        Task task2 = newTask();
        task2.setId(2);
        historyManager.add(task2);

        Task task3 = newTask();
        task3.setId(3);
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
        Task task1 = newTask();
        task1.setId(1);
        historyManager.add(task1);

        Task task2 = newTask();
        task2.setId(2);
        historyManager.add(task2);

        Task task3 = newTask();
        task2.setId(3);
        historyManager.add(task3);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(3, history.size(), "История содержит не 3 элемента.");

        historyManager.remove(task3.getId());

        final List<Task> historyAfterRemove = historyManager.getHistory();

        assertNotNull(history, "Список истории отсутствует.");
        assertEquals(2, historyAfterRemove.size(), "Задача из истории не удалена.");
        //noinspection ResultOfMethodCallIgnored
        assertThrows(IndexOutOfBoundsException.class, () -> historyAfterRemove.get(2), "Задача с конца не удалена");
    }
}