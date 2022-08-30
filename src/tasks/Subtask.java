package tasks;

import tasks.enums.Status;
import tasks.enums.Type;

import java.time.LocalDateTime;

import static tasks.enums.Type.SUBTASK;

public class Subtask extends Task {
    private final int parentEpicId;

    public Subtask(String title, String description, long duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String name, Status status, String description, long duration, LocalDateTime startTime, int parentEpicId) {
        super(name, status, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(int id, String name, Status status, String description, long duration, LocalDateTime startTime, int parentEpicId) {
        super(id, name, status, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public int getEpicId() {
        return parentEpicId;
    }

    @Override
    public Type getType() {
        return SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentEpicId=" + parentEpicId +
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