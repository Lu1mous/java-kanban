import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private static int idCount = 0;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    public TaskManager(){
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }


    public Collection<Task> getTaskCollection() {
        return tasks.values();
    }

    public Collection<Epic> getEpicCollection() {
        return epics.values();
    }

    public Collection<Subtask> getSubtaskCollection() {
        return subtasks.values();
    }

    public void removeTasks(){
        tasks.clear();
    }

    public void removeEpics(){
        epics.clear();
    }

    public void removeSubtasks(){
        subtasks.clear();
    }

    public Task getTaskOfId(int id){
        return tasks.get(id);
    }

    public Epic getEpicOfId(int id){
        return epics.get(id);
    }

    public Subtask getSubtaskOfId(int id){
        return subtasks.get(id);
    }

    public void createTask(Task task){
        idCount++;
        task.setId(idCount);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic){
        idCount++;
        epic.setId(idCount);
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask){
        idCount++;
        subtask.setId(idCount);
        subtasks.put(subtask.getId(), subtask);
    }

    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    public void updateSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        updateStatusOfEpic(subtask.getEpic());
    }

    public void removeTaskOfId(int id){
        tasks.remove(id);
    }

    public void removeEpicOfId(int id){
        epics.remove(id);
    }

    public void removeSubtaskOfId(int id){
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksOfEpic(int id){
        return epics.get(id).getSubtasks();
    }

    private void updateStatusOfEpic(Epic epic){
        if(epic.getSubtasks().isEmpty()){
            epic.setStatus(TaskStatus.NEW);
        }
        int statusNewCount = 0;
        int statusDoneCount = 0;
        for (Subtask subtask : epic.getSubtasks()) {
            if(subtask.getStatus() == TaskStatus.NEW){
                statusNewCount += 1;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                statusDoneCount += 1;
            }
        }
        if(statusNewCount == epic.getSubtasks().size()){
            epic.setStatus(TaskStatus.NEW);
        } else if (statusDoneCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        }else{
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
