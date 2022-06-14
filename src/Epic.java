import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> subtaskMap = new HashMap<>();

    public Epic(String title, String description, int id, String status) {
        super(title, description, id, status);
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

    @Override
    public String getStatus() {
        int statusDeterminant = 0;
        for (Subtask subtask : subtaskMap.values()) {
            switch (subtask.getStatus()) {
                case NEW:
                    break;
                case DONE: {
                    statusDeterminant += 2;
                    break;
                }
                case IN_PROGRESS: {
                    statusDeterminant++;
                    break;
                }
            }
        }
        if (statusDeterminant == 0 || subtaskMap.size() == 0) return NEW;
        if (statusDeterminant == 2 * subtaskMap.size()) return DONE;
        return IN_PROGRESS;
    }
}
