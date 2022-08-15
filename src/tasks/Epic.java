package tasks;

import managers.Type;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
        status = Status.NEW;
    }

    public Epic(String title, String description, int id) {
        super(title, description, id);
        status = Status.NEW;
    }

    public Epic(int id, String title, Status status, String description) {
        super(id, title, status, description);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    public void addSubtaskId(Integer id) {
        subtasksId.add(id);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void removeSubtaskIdByObject(Object o) {
        subtasksId.remove(o);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}