package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    public void createTasks() {
        taskManager = new FileBackedTaskManager();
        super.createTasks();
    }

    @Test
    public void shouldBeSaveTasks() {
        taskManager.getTaskOfId(1);
        taskManager.getTaskOfId(2);
        taskManager.getEpicOfId(3);
        taskManager.getSubtaskOfId(5);
        TaskManager taskManagerCopy = FileBackedTaskManager.loadFromFile("saveTasks.txt");
        Collection<Task> taskCollection = taskManagerCopy.getTaskCollection();
        Collection<Epic> epicCollection = taskManagerCopy.getEpicCollection();
        Collection<Subtask> subtaskCollection = taskManagerCopy.getSubtaskCollection();
        assertEquals(firstTask, taskCollection.toArray()[0], "Задача 1 не создана");
        assertEquals(secondTask, taskCollection.toArray()[1], "Задача 2 не создана");
        assertEquals(firstEpic, epicCollection.toArray()[0], "Эпик 1 не создан");
        assertEquals(firstSubtask, subtaskCollection.toArray()[0], "Подзадача 1 не создана");
    }

    @Test
    public void testExceptionSave() {
        String fileName = "saveTasks.txt";
        Path path = Paths.get(fileName);
        assertDoesNotThrow(() -> taskManager.updateTask(firstTask), "Исключение");
    }

    @Test
    public void testExceptionLoad() {
        String fileName = "saveTasks.txt";
        Path path = Paths.get(fileName);
        assertDoesNotThrow(() -> FileBackedTaskManager.loadFromFile(fileName));
    }

}