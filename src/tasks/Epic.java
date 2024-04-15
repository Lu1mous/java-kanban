package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        Optional<LocalDateTime> optionalStartTime = Optional.ofNullable(subtask.startTime);
        Optional<Duration> optionalDuration = Optional.ofNullable(subtask.duration);
        optionalStartTime.ifPresent(
                startTime -> {
                    if (this.startTime == null) {
                        this.startTime = startTime;
                    } else if (startTime.isBefore(this.startTime)) {
                        this.startTime = startTime;
                    }
                }
        );
        optionalDuration.ifPresent(
                duration -> {
                    if (this.endTime == null) {
                        optionalStartTime.ifPresent(
                                localDateTime -> this.endTime = localDateTime.plus(duration)
                        );
                    } else {
                        optionalStartTime.ifPresent(
                                localDateTime -> {
                                    if (localDateTime.plus(duration).isAfter(this.endTime)) {
                                        this.endTime = localDateTime.plus(duration);
                                    }
                                }
                        );
                    }
                }
        );
        if( this.startTime != null && this.endTime != null) {
            duration = Duration.between(startTime, endTime);
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public Epic clone() {
        Epic cloneEpic = new Epic(this.getName(), this.getDescription(), this.getStatus());
        cloneEpic.setId(this.getId());
        cloneEpic.subtasks = new ArrayList<>(this.subtasks);
        return cloneEpic;
    }

    @Override
    public String toString() {
        return this.getId() + "," + TypeTask.EPIC + "," + this.getName() + "," + this.getStatus()
                + "," + this.getDescription() + "," + "\n";
    }

}
