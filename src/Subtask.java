public class Subtask extends Task {
    private Epic parentEpic;

    public Subtask(String title, String description, int id, Epic parentEpic) {
        super(title, description, id);
        this.parentEpic = parentEpic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }
}
