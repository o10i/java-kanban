package ru.yandex.practicum.tasks;

import ru.yandex.practicum.tasks.enums.Status;
import ru.yandex.practicum.tasks.enums.Type;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, long duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, Status status, String description, long duration, LocalDateTime startTime, int epicId) {
        super(id, name, status, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentEpicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration.getSeconds() / 60 +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}