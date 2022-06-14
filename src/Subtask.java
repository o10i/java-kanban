public class Subtask extends Task {
    private Epic parentEpic;

    public Subtask(String title, String description, int id, String status, Epic parentEpic) {
        super(title, description, id, status);
        this.parentEpic = parentEpic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" + "parentEpic='" + parentEpic.getTitle() + '\'' + ", title='" + title + '\'' +
               ", description='" + description + '\'' + ", id=" + id + ", status='" + status + '\'' + '}';
    }
}
