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

class InMemoryHistoryManagerTest extends TaskManagerTest {

    @BeforeEach
    public void createTasks() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        super.createTasks();
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
    public void shouldBeDeletedHistoryTask() {
        Task task = taskManager.getTaskOfId(1);
        Task subtask = taskManager.getSubtaskOfId(5);
        assertEquals(2, taskManager.getHistoryTasks().size());
        taskManager.removeTaskOfId(task.getId());
        assertEquals(1, taskManager.getHistoryTasks().size());
        Subtask subtaskHistory = (Subtask) taskManager.getHistoryTasks().toArray()[0];
        assertEquals(subtaskHistory, subtask);
    }

    @Test
    public void shouldBeEmptyHistory() {
        assertEquals(0, taskManager.getHistoryTasks().toArray().length);
    }

}