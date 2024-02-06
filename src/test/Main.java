package test;

import manager.TaskManager;
import tasks.*;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        System.out.println("Начало тесто. Создание объектов.");
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Task_1", "1", TaskStatus.NEW));
        taskManager.createTask(new Task("Task_2", "2", TaskStatus.NEW));
        taskManager.createTask(new Task("Task_3", "3", TaskStatus.NEW));
        taskManager.createTask(new Task("Task_4", "4", TaskStatus.NEW));
        taskManager.createEpic(new Epic("Epic_1", "E_1", TaskStatus.NEW));
        taskManager.createEpic(new Epic("Epic_2", "E_2", TaskStatus.NEW));
        Epic epicFirst = taskManager.getEpicOfId(5);
        Epic epicSecond = taskManager.getEpicOfId(6);
        taskManager.createSubtask(new Subtask("Subtask_1", "E_1", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_2", "E_1", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_3", "E_1", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_1", "E_2", TaskStatus.NEW, epicSecond));
        taskManager.createSubtask(new Subtask("Subtask_2", "E_2", TaskStatus.NEW, epicSecond));
        taskManager.createSubtask(new Subtask("Subtask_3", "E_2", TaskStatus.NEW, epicSecond));

        Collection<Task> tasks = taskManager.getTaskCollection();
        Collection<Epic> epics = taskManager.getEpicCollection();
        Collection<Subtask> subtasks = taskManager.getSubtaskCollection();

        System.out.println("Вывод списка задач по типу");
        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");
        System.out.println("Вывод задачи по id");
        System.out.println(taskManager.getTaskOfId(2));

        System.out.println("Обновление статуса задач");
        Task task = taskManager.getTaskOfId(1);
        task.setStatus(TaskStatus.IN_PROGRESS);
        Subtask subtaskFirst = taskManager.getSubtaskOfId(7);
        subtaskFirst.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        taskManager.updateSubtask(subtaskFirst);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");

        System.out.println("Удаление задачи по id");
        taskManager.removeTaskOfId(2);
        taskManager.removeEpicOfId(6);

        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");

        System.out.println("Удаление задач");
        taskManager.removeTasks();
        System.out.println(tasks + "\n");
        System.out.println("Удаление подзадач");
        taskManager.removeSubtasks();
        System.out.println(epics);
        System.out.println(subtasks + "\n");
        System.out.println("Удаление подзадач по id");
        taskManager.removeSubtaskOfId(8);
        taskManager.removeSubtaskOfId(10);
        System.out.println(epics);
        System.out.println(subtasks + "\n");
        System.out.println("Удаление эпиков");
        taskManager.removeEpics();
        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");

    }
}
