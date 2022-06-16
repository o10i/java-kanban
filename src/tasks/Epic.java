package tasks;

import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private final Map<Integer, Subtask> subtaskMap = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    @Override
    public String getStatus() {
        return super.getStatus();
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
        if (statusDeterminant == 0 || subtaskMap.size() == 0) status = NEW;
        else if (statusDeterminant == 2 * subtaskMap.size()) status = DONE;
        else status = IN_PROGRESS;
    }

    @Override
    public String toString() {
        determineEpicStatus();
        return "Epic{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id +
                ", status='" + status + '\'' + '}' + getSubtasks();
    }
}
