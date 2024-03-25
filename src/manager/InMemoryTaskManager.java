package manager;

import tasks.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import  java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idCount = 0;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    protected int getNewId() {
        idCount++;
        return idCount;
    }

    @Override
    public Collection<Task> getTaskCollection() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    @Override
    public Collection<Epic> getEpicCollection() {
        return Collections.unmodifiableCollection(epics.values());
    }

    @Override
    public Collection<Subtask> getSubtaskCollection() {
        return Collections.unmodifiableCollection(subtasks.values());
    }

    @Override
    public void removeTasks() {
        tasks.clear();
        historyManager.clear();
    }

    @Override
    public void removeEpics() {
        subtasks.clear();
        epics.clear();
        historyManager.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateStatusOfEpic(epic);
        }
        historyManager.clear();
    }

    @Override
    public Task getTaskOfId(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicOfId(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskOfId(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        task.setId(getNewId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNewId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(getNewId());
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateStatusOfEpic(subtask.getEpic());
    }

    @Override
    public void removeTaskOfId(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicOfId(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtaskOfId(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                subtasks.remove(id);
                updateStatusOfEpic(epic);
            }
        }
        historyManager.remove(id);
    }

    @Override
    public Collection<Subtask> getSubtasksOfEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return Collections.unmodifiableCollection(epic.getSubtasks());
        } else {
            return null;
        }
    }

    private void updateStatusOfEpic(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        }
        int statusNewCount = 0;
        int statusDoneCount = 0;
        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                statusNewCount += 1;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                statusDoneCount += 1;
            }
        }
        if (statusNewCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (statusDoneCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public List<Task> getHistoryTasks() {
        return historyManager.getHistory();
    }

}
