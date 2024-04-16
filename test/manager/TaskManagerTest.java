package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {
    protected static TaskManager taskManager;
    protected static HistoryManager historyManager;
    protected static Task firstTask;
    protected static Task secondTask;
    protected static Epic firstEpic;
    protected static Epic secondEpic;
    protected static Subtask firstSubtask;
    protected static Subtask secondSubtask;
    protected static Subtask thirdSubtask;

    @BeforeEach
    public void createTasks() {
        firstTask = new Task("Задача 1", "Первая задача", TaskStatus.NEW, LocalDateTime.of(
                2024, 4, 17, 10, 0), Duration.of(2, ChronoUnit.HOURS));
        secondTask = new Task("Задача 2", "Вторая задача", TaskStatus.NEW, LocalDateTime.of(
                2024, 4, 17, 14, 0), Duration.of(3, ChronoUnit.HOURS));
        firstEpic = new Epic("Эпик 1", "Первый эпик", TaskStatus.NEW);
        secondEpic = new Epic("Эпик 2", "Второй эпик", TaskStatus.NEW);
        firstSubtask = new Subtask("Подзадача 1", "Первая подзадача", TaskStatus.NEW, firstEpic,
                LocalDateTime.of(2024, 4, 16, 8, 0),
                Duration.of(3, ChronoUnit.HOURS));
        secondSubtask = new Subtask("Подзадача 2", "Вторая подзадача", TaskStatus.NEW, firstEpic,
                LocalDateTime.of(2024, 4, 10, 10, 0),
                Duration.of(2, ChronoUnit.HOURS));
        thirdSubtask = new Subtask("Подзадача 3", "Третья подзадача", TaskStatus.NEW, secondEpic,
                LocalDateTime.of(2024, 4, 18, 16, 0),
                Duration.of(1, ChronoUnit.HOURS));
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createEpic(firstEpic);
        taskManager.createEpic(secondEpic);
        taskManager.createSubtask(firstSubtask);
        taskManager.createSubtask(secondSubtask);
    }

    @Test
    public void shouldBeCreateTask() {
        Collection<Task> taskCollection = taskManager.getTaskCollection();
        System.out.println(taskCollection);
        assertEquals(firstTask, taskCollection.toArray()[0], "Задача 1 не создана");
        assertEquals(secondTask, taskCollection.toArray()[1], "Задача 2 не создана");
    }

    @Test
    public void shouldBeCreateEpic() {
        Collection<Epic> epicCollection = taskManager.getEpicCollection();
        System.out.println(epicCollection);
        assertEquals(firstEpic, epicCollection.toArray()[0], "Эпик 1 не создана");
        assertEquals(secondEpic, epicCollection.toArray()[1], "Эпик 2 не создана");
    }

    @Test
    public void shouldBeCreateSubtask() {
        Collection<Subtask> subtaskCollection = taskManager.getSubtaskCollection();
        System.out.println(subtaskCollection);
        assertEquals(firstSubtask, subtaskCollection.toArray()[0], "Подзадача 1 не создана");
        assertEquals(secondSubtask, subtaskCollection.toArray()[1], "Подзадача 2 не создана");
    }

    @Test
    public void shouldBeGetTaskOfId() {
        assertEquals(firstTask, taskManager.getTaskOfId(1), "Задача не найдена");
    }

    @Test
    public void shouldBeGetEpicOfId() {
        assertEquals(firstEpic, taskManager.getEpicOfId(3), "Эпик не найден");
    }

    @Test
    public void shouldBeGetSubtaskOfId() {
        assertEquals(firstSubtask, taskManager.getSubtaskOfId(5), "Подзадача не найдена");
    }

    @Test
    public void shouldBeRemoveAllTasks() {
        taskManager.removeTasks();
        assertEquals(0,taskManager.getTaskCollection().size(), "В списке есть задачи");
    }

    @Test
    public void shouldBeRemoveAllEpics() {
        taskManager.removeEpics();
        assertEquals(0,taskManager.getEpicCollection().size(), "В списке есть эпики");
    }

    @Test
    public void shouldBeRemoveAllSubtasks() {
        taskManager.removeSubtasks();
        assertEquals(0,taskManager.getSubtaskCollection().size(), "В списке есть подзадачи");
    }

    @Test
    public void shouldBeUpdateStatusEpic() {
        assertEquals(TaskStatus.NEW, firstEpic.getStatus(), "Статус не обновлен");
        firstSubtask.setStatus(TaskStatus.IN_PROGRESS);
        secondEpic.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(firstSubtask);
        taskManager.updateSubtask(secondSubtask);
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus(), "Статус не обновлен");
        firstSubtask.setStatus(TaskStatus.DONE);
        secondSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(firstSubtask);
        taskManager.updateSubtask(secondSubtask);
        assertEquals(TaskStatus.DONE, firstEpic.getStatus(), "Статус не обновлен");
        firstSubtask.setStatus(TaskStatus.NEW);
        secondEpic.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(firstSubtask);
        taskManager.updateSubtask(secondSubtask);
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus(), "Статус не обновлен");
    }

    @Test
    public void shouldBeRemoveTaskOfId() {
        taskManager.removeTaskOfId(1);
        assertNotEquals(firstTask, taskManager.getTaskOfId(1), "Задача не удалена");
    }

    @Test
    public void shouldBeRemoveEpicOfId() {
        taskManager.removeEpicOfId(2);
        assertNotEquals(firstEpic, taskManager.getEpicOfId(2), "Эпик не удален");
    }

    @Test
    public void shouldBeRemoveSubtaskOfId() {
        taskManager.removeSubtaskOfId(5);
        assertNotEquals(firstSubtask, taskManager.getSubtaskOfId(5), "Подзадача не удалена");
    }

    @Test
    public void shouldBeEpicOfSubtask() {
        assertEquals(firstEpic, firstSubtask.getEpic(), "Эпик отсутствует");
    }

    @Test
    public void shouldBeIntersection() {
        assertThrows(RuntimeException.class, () -> taskManager.createTask(secondTask) , "Нет пересечения");
        Task t = new Task("Задача", "Проверка", TaskStatus.DONE,
                LocalDateTime.of(2024, 5,10, 20, 0),
                Duration.of(3, ChronoUnit.HOURS));
        assertDoesNotThrow(() -> taskManager.updateTask(t), "Пересечение задач");

    }

    @Test
    public void DoesNotCreateDoubleTask() {
        assertThrows(TaskIsIntersectionException.class, () -> {
            taskManager.createTask(firstTask);
            taskManager.createTask(firstTask);
        });
    }

    @Test
    public void shouldBeCloneTask() {
        Task task = firstTask.clone();
        Subtask subtask = firstSubtask.clone();
        Epic epic = firstEpic.clone();
        assertEquals(firstTask, task);
        assertEquals(firstSubtask, subtask);
        assertEquals(firstEpic, epic);
    }
}
