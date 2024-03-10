package tasks;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
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
}
