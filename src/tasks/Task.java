package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    private static DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime statTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = statTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public static DateTimeFormatter getDataTimeFormat() {
        return dataTimeFormat;
    }

    public boolean isIntersection(Task task) {
        if (this.equals(task)) {
            return false;
        }
        LocalDateTime startTime1 = task.getStartTime();
        LocalDateTime endTime1 = task.getEndTime();
        LocalDateTime startTime2 = this.getStartTime();
        LocalDateTime endTime2 = this.getEndTime();
        return startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);
    }

    @Override
    public String toString() {
        return id + "," + TypeTask.TASK + "," + name + "," + status + "," + description + "," + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Task clone() {
        Task cloneTask = new Task(this.name, this.description, this.status);
        cloneTask.setId(this.id);
        return cloneTask;
    }

}
