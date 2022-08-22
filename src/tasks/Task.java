package tasks;

import tasks.enums.Status;
import tasks.enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration = Duration.ZERO;
    protected LocalDateTime startTime;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = Status.NEW;
    }

    public Task(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
        status = Status.NEW;
    }

    public Task(int id, String title, Status status, String description) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
        status = Status.NEW;
    }

    public Task(String title, String description, int id, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
        status = Status.NEW;
    }

    public Task(int id, String title, Status status, String description, long duration) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(int id, String title, Status status, String description, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(String title, String description, Status status, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return Type.TASK;
    }

    @Override
    public String toString() {
        return "Task{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id + ", status=" + status + ", duration=" + duration.getSeconds() / 60 + ", startTime=" + startTime + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }
}