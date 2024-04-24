package test;

import manager.InMemoryTaskManager;
import tasks.*;

import java.util.Collection;

public class Main {

    /*public static void main(String[] args) {
        System.out.println("Начало тесто. Создание объектов.");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createTask(new Task("Task_1", "1", TaskStatus.NEW));
        taskManager.createTask(new Task("Task_2", "2", TaskStatus.NEW));
        taskManager.createEpic(new Epic("Epic_1", "E_1", TaskStatus.NEW));
        taskManager.createEpic(new Epic("Epic_2", "E_2", TaskStatus.NEW));
        Epic epicFirst = taskManager.getEpicOfId(3);
        taskManager.createSubtask(new Subtask("Subtask_1", "E_1", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_2", "E_1", TaskStatus.NEW, epicFirst));
        taskManager.createSubtask(new Subtask("Subtask_3", "E_1", TaskStatus.NEW, epicFirst));

        Collection<Task> tasks = taskManager.getTaskCollection();
        Collection<Epic> epics = taskManager.getEpicCollection();
        Collection<Subtask> subtasks = taskManager.getSubtaskCollection();

        System.out.println("Вывод списка задач по типу");
        System.out.println(tasks);
        System.out.println(epics);
        System.out.println(subtasks + "\n");
        System.out.println("Вывод задачи по id");
        System.out.println(taskManager.getTaskOfId(2));
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();
        System.out.println(taskManager.getTaskOfId(1));
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();
        System.out.println(taskManager.getTaskOfId(2));
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();
        System.out.println(taskManager.getSubtaskOfId(5));
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();
        System.out.println(taskManager.getSubtaskOfId(7));
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();
        System.out.println(taskManager.getSubtaskOfId(5));
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();

        System.out.println("Удаление задачи по id");
        taskManager.removeTaskOfId(2);
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();
        taskManager.removeEpicOfId(3);
        System.out.println(taskManager.getHistoryTasks());
        System.out.println();

    }*/
}
