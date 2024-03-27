package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TypeTask;

public class ConverterTask {
    public static String taskToCsv(Task task) {
        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + "," + task.getStatus()
                + "," + task.getDescription() + "," + "\n";
    }

    public static String epicToCsv(Epic epic) {
        return epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getStatus()
                + "," + epic.getDescription() + "," + "\n";
    }

    public static String subtaskToCsv(Subtask subtask) {
        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus()
                + "," + subtask.getDescription() + ","  + subtask.getEpic().getId() + "\n";
    }
}
