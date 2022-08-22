package tasks;

import managers.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title, String description, int id) {
        super(title, description, id);
    }

    public Epic(int id, String title, Status status, String description) {
        super(id, title, status, description);
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(String title, String description, long duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
    }

    public Epic(String title, String description, int id, long duration, LocalDateTime startTime) {
        super(title, description, id, duration, startTime);
    }

    public Epic(int id, String title, Status status, String description, long duration) {
        super(id, title, status, description, duration);
    }

    public Epic(int id, String title, Status status, String description, long duration, LocalDateTime startTime) {
        super(id, title, status, description, duration, startTime);
    }

    public Epic(String title, String description, Status status, long duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" + "subtasksId=" + subtasksId + ", title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id + ", status=" + status + ", duration=" + duration.getSeconds() / 60 + ", startTime=" + startTime + '}';
    }

    public void addSubtaskId(Integer id) {
        subtasksId.add(id);
    }

    public void addSubtaskDuration(Duration duration) {
        this.duration = this.duration.plus(duration);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtasksId.remove(subtaskId);
    }

    public void removeSubtaskDuration(Duration duration) {
        this.duration = this.duration.minus(duration);
    }
}