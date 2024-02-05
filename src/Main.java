import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Task_1", "just do it_1", TaskStatus.NEW));
        taskManager.createTask(new Task("Task_2", "just do it_2", TaskStatus.NEW));
        taskManager.createEpic(new Epic("Epic_1", "Do_1", TaskStatus.NEW));
        taskManager.createEpic(new Epic("Epic_2", "Do_2", TaskStatus.NEW));
        Epic epicFirst = taskManager.getEpicOfId(3);
        Epic epicSecond = taskManager.getEpicOfId(4);
        taskManager.createSubtask(new Subtask("Subtask_1", "sub1", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_2", "sub2", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_1", "sub3", TaskStatus.NEW, epicSecond));
        Collection<Task> tasks = taskManager.getTaskCollection();
        Collection<Epic> epics = taskManager.getEpicCollection();
        Collection<Subtask> subtasks = taskManager.getSubtaskCollection();

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");
        System.out.println(taskManager.getTaskOfId(2));

        Task task = taskManager.getTaskOfId(1);
        task.setStatus(TaskStatus.IN_PROGRESS);
        Subtask subtaskFirst = taskManager.getSubtaskOfId(7);
        subtaskFirst.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        taskManager.updateSubtask(subtaskFirst);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");

        taskManager.removeTaskOfId(2);
        taskManager.removeEpicOfId(3);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");
    }
}
