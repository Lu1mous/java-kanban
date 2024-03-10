package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;
    private static Task firstTask;
    private static Task secondTask;
    private static Epic firstEpic;
    private static Epic secondEpic;
    private static Subtask firstSubtask;
    private static Subtask secondSubtask;
    private static Subtask thirdSubtask;

    @BeforeEach
    public void createTasks(){
        taskManager = Managers.getDefault();
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
    public void shouldBeCreateTask(){
        Collection<Task> taskCollection = taskManager.getTaskCollection();
        System.out.println(taskCollection);
        assertEquals(firstTask, taskCollection.toArray()[0], "Задача 1 не создана");
        assertEquals(secondTask, taskCollection.toArray()[1], "Задача 2 не создана");
    }

    @Test
    public void shouldBeCreateEpic(){
        Collection<Epic> epicCollection = taskManager.getEpicCollection();
        System.out.println(epicCollection);
        assertEquals(firstEpic, epicCollection.toArray()[0], "Эпик 1 не создана");
        assertEquals(secondEpic, epicCollection.toArray()[1], "Эпик 2 не создана");
    }

    @Test
    public void shouldBeCreateSubtask(){
        Collection<Subtask> subtaskCollection = taskManager.getSubtaskCollection();
        System.out.println(subtaskCollection);
        assertEquals(firstSubtask, subtaskCollection.toArray()[0], "Подзадача 1 не создана");
        assertEquals(secondSubtask, subtaskCollection.toArray()[1], "Подзадача 2 не создана");
    }

    @Test
    public void shouldBeGetTaskOfId(){
        assertEquals(firstTask, taskManager.getTaskOfId(1), "Задача не найдена");
    }

    @Test
    public void shouldBeGetEpicOfId(){
        assertEquals(firstEpic, taskManager.getEpicOfId(3), "Эпик не найден");
    }

    @Test
    public void shouldBeGetSubtaskOfId(){
        assertEquals(firstSubtask, taskManager.getSubtaskOfId(5), "Подзадача не найдена");
    }

    @Test
    public void shouldBeRemoveAllTasks(){
        taskManager.removeTasks();
        assertEquals(0,taskManager.getTaskCollection().size(), "В списке есть задачи");
    }

    @Test
    public void shouldBeRemoveAllEpics(){
        taskManager.removeEpics();
        assertEquals(0,taskManager.getEpicCollection().size(), "В списке есть эпики");
    }

    @Test
    public void shouldBeRemoveAllSubtasks(){
        taskManager.removeSubtasks();
        assertEquals(0,taskManager.getSubtaskCollection().size(), "В списке есть подзадачи");
    }

    @Test
    public void shouldBeUpdateStatusEpic(){
        firstSubtask.setStatus(TaskStatus.IN_PROGRESS);
        thirdSubtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(firstSubtask);
        taskManager.updateSubtask(thirdSubtask);
        assertEquals(TaskStatus.IN_PROGRESS, firstEpic.getStatus(), "Статус не обновлен");
        assertEquals(TaskStatus.DONE, secondEpic.getStatus(), "Статус не обновлен");
    }

    @Test
    public void shouldBeCloneTask(){
        Task task = firstTask.clone();
        Subtask subtask = firstSubtask.clone();
        Epic epic = firstEpic.clone();
        assertEquals(firstTask, task);
        assertEquals(firstSubtask, subtask);
        assertEquals(firstEpic, epic);
    }


}