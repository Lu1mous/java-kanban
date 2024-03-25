package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private static TaskManager taskManager;
    private static Task firstTask;
    private static Task secondTask;
    private static Epic firstEpic;
    private static Subtask firstSubtask;

    @BeforeEach
    public void createTasks() {
        taskManager = new FileBackedTaskManager();
        firstTask = new Task("Задача 1", "Первая задача", TaskStatus.NEW);
        secondTask = new Task("Задача 2", "Вторая задача", TaskStatus.NEW);
        firstEpic = new Epic("Эпик 1", "Первый эпик", TaskStatus.IN_PROGRESS);
        firstSubtask = new Subtask("Подзадача 1", "Первая подзадача", TaskStatus.NEW, firstEpic);
    }

    @Test
    public void shouldBeSaveTasks() {
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createEpic(firstEpic);
        taskManager.createSubtask(firstSubtask);
        taskManager.getTaskOfId(1);
        taskManager.getTaskOfId(2);
        taskManager.getEpicOfId(3);
        taskManager.getSubtaskOfId(4);
        TaskManager taskManagerCopy = FileBackedTaskManager.loadFromFile();
        Collection<Task> taskCollection = taskManagerCopy.getTaskCollection();
        Collection<Epic> epicCollection = taskManagerCopy.getEpicCollection();
        Collection<Subtask> subtaskCollection = taskManagerCopy.getSubtaskCollection();
        assertEquals(firstTask, taskCollection.toArray()[0], "Задача 1 не создана");
        assertEquals(secondTask, taskCollection.toArray()[1], "Задача 2 не создана");
        assertEquals(firstEpic, epicCollection.toArray()[0], "Эпик 1 не создан");
        assertEquals(firstSubtask, subtaskCollection.toArray()[0], "Подзадача 1 не создана");
    }

}