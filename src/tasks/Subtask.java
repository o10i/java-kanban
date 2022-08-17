package tasks;

import managers.Type;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int parentEpicId;

    public Subtask(String title, String description, int parentEpicId) {
        super(title, description);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, int id, int parentEpicId) {
        super(title, description, id);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(int id, String title, Status status, String description, int parentEpicId) {
        super(id, title, status, description);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, Status status, int parentEpicId) {
        super(title, description, status);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, Duration duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, int id, Duration duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, id, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(int id, String title, Status status, String description, Duration duration, LocalDateTime startTime, int parentEpicId) {
        super(id, title, status, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, Status status, Duration duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, status, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    public int getParentEpicId() {
        return parentEpicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentEpicId=" + parentEpicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}