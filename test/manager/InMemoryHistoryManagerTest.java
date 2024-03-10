package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;
    private static HistoryManager historyManager;
    private static Task firstTask;
    private static Task secondTask;
    private static Epic firstEpic;
    private static Epic secondEpic;
    private static Subtask firstSubtask;
    private static Subtask secondSubtask;
    private static Subtask thirdSubtask;

    @BeforeEach
    public void createTasks() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        firstTask = new Task("Задача 1", "Первая задача", TaskStatus.NEW);
        secondTask = new Task("Задача 2", "Вторая задача", TaskStatus.NEW);
        firstEpic = new Epic("Эпик 1", "Первый эпик", TaskStatus.NEW);
        secondEpic = new Epic("Эпик 2", "Второй эпик", TaskStatus.NEW);
        firstSubtask = new Subtask("Подзадача 1", "Первая подзадача", TaskStatus.NEW, firstEpic);
        secondSubtask = new Subtask("Подзадача 2", "Вторая подзадача", TaskStatus.NEW, firstEpic);
        thirdSubtask = new Subtask("Подзадача 3", "Третья подзадача", TaskStatus.NEW, secondEpic);
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        taskManager.createSubtask(firstSubtask);
        taskManager.createSubtask(secondSubtask);
    }

    @Test
    public void shouldBeSizeEquals3() {
        for (int i = 0; i < 5; i++) {
            historyManager.add(firstTask);
            historyManager.add(firstEpic);
            historyManager.add(firstSubtask);
        }
        System.out.println(historyManager.getHistory());
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    public void shouldBeAddHistoryByGetTaskOfId() {
        Task firstHistory = taskManager.getTaskOfId(1);
        Task secondHistory = taskManager.getSubtaskOfId(5);
        assertEquals(firstHistory, taskManager.getHistoryTasks().toArray()[1]);
        assertEquals(secondHistory, taskManager.getHistoryTasks().toArray()[0]);
    }

    @Test
    public void shouldBeSavedVersionTask() {
        Task task = taskManager.getTaskOfId(1);
        Task subtask = taskManager.getSubtaskOfId(5);
        task.setStatus(TaskStatus.DONE);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        Task taskHistory = (Task)taskManager.getHistoryTasks().toArray()[1];
        Subtask subtaskHistory = (Subtask) taskManager.getHistoryTasks().toArray()[0];
        assertNotEquals(taskHistory.getStatus(), task.getStatus());
        assertNotEquals(subtaskHistory.getStatus(), subtask.getStatus());
    }

    @Test
    public void shouldBeDeletedHistoryTask() {
        Task task = taskManager.getTaskOfId(1);
        Task subtask = taskManager.getSubtaskOfId(5);
        assertEquals(2, taskManager.getHistoryTasks().size());
        taskManager.removeTaskOfId(1);
        assertEquals(1, taskManager.getHistoryTasks().size());
        Subtask subtaskHistory = (Subtask) taskManager.getHistoryTasks().toArray()[0];
        assertEquals(subtaskHistory, subtask);
    }

}