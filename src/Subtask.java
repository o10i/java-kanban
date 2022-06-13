public class Subtask extends Task {
    private String parentEpic;

    public Subtask(String title, String description, int id, String parentEpic) {
        super(title, description, id);
        this.parentEpic = parentEpic;
    }

    public String getParentEpic() {
        return parentEpic;
    }

    @Override
    public String toString() {
        return "\n\tSubtask{" +
                "parentEpic='" + parentEpic + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
