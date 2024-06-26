package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Subtask(String name, String description, TaskStatus status, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, description, status,startTime, duration);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public Subtask clone() {
        Subtask cloneSubtask = new Subtask(this.getName(), this.getDescription(), this.getStatus(), this.getEpic());
        cloneSubtask.setId(this.getId());
        return cloneSubtask;
    }

    @Override
    public String toString() {
        return this.getId() + "," + TypeTask.SUBTASK + "," + this.getName() + "," + this.getStatus()
                + "," + this.getDescription() + "," + this.epic.getId() + "\n";
    }

}
