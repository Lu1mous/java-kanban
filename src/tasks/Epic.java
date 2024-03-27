package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
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
