package tasks;

import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private final Map<Integer, Subtask> subtaskMap;

    public Epic(String title, String description) {
        super(title, description);
        subtaskMap = new HashMap<>();
        status = NEW;
    }

    @Override
    public void setStatus(String status) {
    }

    public String getSubtasks() {
        String subtasks = "";
        int counter = 1;
        for (Subtask subtask : subtaskMap.values()) {
            subtasks += "\n\t\t" + counter + ": " + subtask.toString();
            counter++;
        }
        return subtasks;
    }

    public void addSubtask(Integer key, Subtask value) {
        subtaskMap.put(key, value);
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    public void determineEpicStatus() {
        if (subtaskMap.values().stream().allMatch(foo -> foo.getStatus().equals(NEW))) {
            status = NEW;
        } else if (subtaskMap.values().stream().allMatch(Subtask -> Subtask.getStatus().equals(DONE))) {
            status = DONE;
        } else status = IN_PROGRESS;
    }

    @Override
    public String toString() {
        return "Epic{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id +
                ", status='" + status + '\'' + '}' + getSubtasks();
    }
}
