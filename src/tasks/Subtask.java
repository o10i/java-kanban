package tasks;

public class Subtask extends Task {
    private final int parentEpicId;

    public Subtask(String title, String description, int parentEpicId) {
        super(title, description);
        this.parentEpicId = parentEpicId;
        status = NEW;
    }

    public int getParentEpicId() {
        return parentEpicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + "parentEpicId=" + parentEpicId + ", title='" + title + '\'' + ", description='" +
                description + '\'' + ", id=" + id + ", status='" + status + '\'' + '}';
    }
}
