package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Collection;

public interface TaskManager {
    Collection<Task> getTaskCollection();

    Collection<Epic> getEpicCollection();

    Collection<Subtask> getSubtaskCollection();

    void removeTasks();

    void removeEpics();

    void removeSubtasks();

    Task getTaskOfId(int id);

    Epic getEpicOfId(int id);

    Subtask getSubtaskOfId(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskOfId(int id);

    void removeEpicOfId(int id);

    void removeSubtaskOfId(int id);

    Collection<Subtask> getSubtasksOfEpic(int id);

    HistoryManager getHistoryManager();

}
