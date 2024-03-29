package ru.yandex.practicum.tasks;

import ru.yandex.practicum.tasks.enums.Status;
import ru.yandex.practicum.tasks.enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, 0, null);
    }

    public Epic(int id, String name, String description) {
        super(id, name, Status.NEW, description, 0, null);
    }
    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(Integer id) {
        subtasksId.add(id);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtasksId.remove(subtaskId);
    }

    public void addSubtaskDuration(Duration duration) {
        this.duration = this.duration.plus(duration);
    }

    public void removeSubtaskDuration(Duration duration) {
        this.duration = this.duration.minus(duration);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + getDuration().getSeconds() / 60 +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}