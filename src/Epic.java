import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String title, String description, int id) {
        super(title, description, id);
    }

    public void printSubtasks() {
        if (subtasks.size() > 0) {
            int counter = 1;
            for (Subtask subtask : subtasks) {
                System.out.println("\t\tПодзадача " + counter + ": " + subtask.title);
                counter++;
            }
        }
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    @Override
    public String toString() {
        return "\nEpic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtasks=" + subtasks +
                '}';
    }
}
