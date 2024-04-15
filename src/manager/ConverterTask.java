package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TypeTask;

public class ConverterTask {
    public static String taskToCsv(Task task) {
        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + "," + task.getStatus()
                + "," + task.getDescription() + "," + task.getStartTime().format(Task.getDataTimeFormat())
                + "," + task.getDuration().toMinutes() + "\n";
    }

    public static String epicToCsv(Epic epic) {
        return epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getStatus()
                + "," + epic.getDescription() + "," + "\n";
    }

    public static String subtaskToCsv(Subtask subtask) {
        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus()
                + "," + subtask.getDescription() + ","  + subtask.getEpic().getId() + ","
                + subtask.getStartTime().format(Task.getDataTimeFormat()) + ","
                + subtask.getDuration().toMinutes() + "\n";
    }
}
