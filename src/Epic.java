import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

}
