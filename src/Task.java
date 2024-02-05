public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(String name, String description, TaskStatus status){
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public boolean equals(Task task){
        return this.id == task.id;
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

    @Override
    public String toString(){
        return "Task {name = " + name + "/ description = " + description +
                "/ id = " + id + "/ status = " + status +" }";
    }
}
