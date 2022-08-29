package tasks;

import tasks.enums.Type;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static tasks.enums.Type.EPIC;

public class Epic extends Task {
    private final List<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
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
    public Type getType() {
        return EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId +
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