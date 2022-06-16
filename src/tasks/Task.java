package tasks;

public class Task {
    public static final String NEW = "NEW";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String DONE = "DONE";
    protected String title;
    protected String description;
    protected int id;
    protected String status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id +
                ", status='" + status + '\'' + '}';
    }
}