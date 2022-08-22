package tasks;

import tasks.enums.Status;
import tasks.enums.Type;

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

    public Subtask(String title, String description, long duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, int id, long duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, id, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(int id, String title, Status status, String description, long duration, LocalDateTime startTime, int parentEpicId) {
        super(id, title, status, description, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, Status status, long duration, LocalDateTime startTime, int parentEpicId) {
        super(title, description, status, duration, startTime);
        this.parentEpicId = parentEpicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" + "parentEpicId=" + parentEpicId + ", title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id + ", status=" + status + ", duration=" + duration.getSeconds() / 60 + ", startTime=" + startTime + '}';
    }

    public int getParentEpicId() {
        return parentEpicId;
    }
}