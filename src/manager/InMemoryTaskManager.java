package manager;

import tasks.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected int idCount = 0;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;
    private TreeSet<Task> priorityTasks;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
        priorityTasks = priorityTasks.stream()
                .filter(task -> task.getClass() != Task.class)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
    }

    @Override
    public void removeEpics() {
        subtasks.clear();
        epics.clear();
        historyManager.clear();
        priorityTasks = priorityTasks.stream()
                .filter(task -> task.getClass() != Epic.class && task.getClass() != Subtask.class)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateStatusOfEpic(epic);
        }
        historyManager.clear();
        priorityTasks = priorityTasks.stream()
                .filter(task -> task.getClass() != Subtask.class)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
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
    public Epic getEpicOfIdWithoutHistory(int id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskOfId(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        boolean isIntersection = priorityTasks
                .stream()
                .anyMatch(task1 -> task1.isIntersection(task));
        if (!isIntersection) {
            task.setId(getNewId());
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                priorityTasks.add(task);
            }
        } else {
            throw new TaskIsIntersectionException("Пересечение задач");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNewId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        boolean isIntersection = priorityTasks
                .stream()
                .anyMatch(subtask1 -> subtask1.isIntersection(subtask));
        if (!isIntersection) {
            subtask.setId(getNewId());
            subtasks.put(subtask.getId(), subtask);
            if (subtask.getStartTime() != null) {
                priorityTasks.add(subtask);
            }
        } else {
            throw new TaskIsIntersectionException("Пересечение задач");
        }
    }

    @Override
    public void updateTask(Task task) {
        boolean isIntersection = priorityTasks
                .stream()
                .filter(t -> t.getId() != task.getId())
                .anyMatch(task1 -> task1.isIntersection(task));
        if (!isIntersection) {
            tasks.put(task.getId(), task);
            priorityTasks = priorityTasks.stream()
                    .filter(task1 -> task1.getId() != task.getId())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
            priorityTasks.add(task);
        } else {
            throw new TaskIsIntersectionException("Пересечение задач");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateStatusOfEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        boolean isIntersection = priorityTasks
                .stream()
                .filter(t -> t.getId() != subtask.getId())
                .anyMatch(task1 -> task1.isIntersection(subtask));
        if (!isIntersection) {
            subtasks.put(subtask.getId(), subtask);
            priorityTasks = priorityTasks.stream()
                    .filter(subtask1 -> subtask1.getId() != subtask.getId())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
            priorityTasks.add(subtask);
            updateStatusOfEpic(subtask.getEpic());
        } else {
            throw new TaskIsIntersectionException("Пересечение задач");
        }
    }

    @Override
    public void removeTaskOfId(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            tasks.remove(id);
            historyManager.remove(id);
            priorityTasks.remove(task);
        }
    }

    @Override
    public void removeEpicOfId(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
                priorityTasks.remove(subtask);
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
        priorityTasks.remove(subtask);
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

    public Collection<Task> getPrioritizedTasks() {
        if (priorityTasks.isEmpty()) {
            priorityTasks.addAll(tasks.values());
            priorityTasks.addAll(subtasks.values());
        }
        return Collections.unmodifiableCollection(priorityTasks);
    }

}
