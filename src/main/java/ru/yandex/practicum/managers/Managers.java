package ru.yandex.practicum.managers;

import ru.yandex.practicum.managers.history.HistoryManager;
import ru.yandex.practicum.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.managers.task.HTTPTaskManager;
import ru.yandex.practicum.managers.task.TaskManager;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HTTPTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}