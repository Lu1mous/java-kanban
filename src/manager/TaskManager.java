package manager;

import tasks.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class TaskManager {
    private int idCount = 0;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    public TaskManager(){
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    private int getNewId(){
        idCount++;
        return idCount;
    }

    public Collection<Task> getTaskCollection() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    public Collection<Epic> getEpicCollection() {
        return Collections.unmodifiableCollection(epics.values());
    }

    public Collection<Subtask> getSubtaskCollection() {
        return Collections.unmodifiableCollection(subtasks.values());
    }

    public void removeTasks(){
        tasks.clear();
    }

    public void removeEpics(){
        subtasks.clear();
        epics.clear();
    }

    public void removeSubtasks(){
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateStatusOfEpic(epic);
        }
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
        task.setId(getNewId());
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic){
        epic.setId(getNewId());
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask){
        subtask.setId(getNewId());
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
        Epic epic = epics.get(id);
        if(epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }

    public void removeSubtaskOfId(int id){
        Subtask subtask = subtasks.get(id);
        if(subtask != null) {
            Epic epic = subtask.getEpic();
            if(epic != null) {
                subtasks.remove(id);
                updateStatusOfEpic(epic);
            }
        }
    }

    public Collection<Subtask> getSubtasksOfEpic(int id){
        Epic epic = epics.get(id);
        if(epic != null) {
            return Collections.unmodifiableCollection(epic.getSubtasks());
        }else {
            return null;
        }
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
