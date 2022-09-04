package tasks;

import tasks.enums.Status;
import tasks.enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static tasks.enums.Status.NEW;
import static tasks.enums.Type.TASK;

public class Task {
    protected int id;
    protected String name;
    protected Status status;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = NEW;
    }

    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime.minusSeconds(startTime.getSecond()).minusNanos(startTime.getNano());
        this.status = NEW;
    }

    public Task(String name, Status status, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime.minusSeconds(startTime.getSecond()).minusNanos(startTime.getNano());
        this.endTime = this.startTime.plus(this.duration);
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = NEW;
    }

    public Task(int id, String name, Status status, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime.minusSeconds(startTime.getSecond()).minusNanos(startTime.getNano());
        this.endTime = this.startTime.plus(this.duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        if (duration != null) {
            return duration;
        }
        return Duration.ZERO;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        endTime = startTime.plus(this.duration);
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Type getType() {
        return TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && status == task.status && Objects.equals(description, task.description) && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, duration, startTime);
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration.getSeconds() / 60 +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}