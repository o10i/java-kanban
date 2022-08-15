package tasks;

import managers.Type;

public class Subtask extends Task {
    private final int parentEpicId;

    public Subtask(String title, String description, int parentEpicId) {
        super(title, description);
        status = Status.NEW;
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String title, String description, int id, int parentEpicId) {
        super(title, description, id);
        status = Status.NEW;
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