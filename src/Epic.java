import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> subtaskMap = new HashMap<>();

    public Epic(String title, String description, int id) {
        super(title, description, id);
    }

    public void printSubtasks() {
        int counter = 1;
        for (Subtask subtask : subtaskMap.values()) {
            System.out.println("\t\tПодзадача " + counter + ": " + subtask.getTitle());
            counter++;
        }
    }

    public void addSubtask(Integer key, Subtask value) {
        subtaskMap.put(key, value);
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    /*public void setSubtask(Integer id, Subtask subtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == id) {
                subtasks.set(i, subtask);
            }
        }
    }*/
}
