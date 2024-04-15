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

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    public void createTasks() {
        taskManager = Managers.getDefault();
        super.createTasks();
    }

    @Test
    public void shouldBeCreateTask() {
        super.shouldBeCreateTask();
    }

    @Test
    public void shouldBeCreateEpic() {
        super.shouldBeCreateEpic();
    }

    @Test
    public void shouldBeCreateSubtask() {
        super.shouldBeCreateSubtask();
    }

    @Test
    public void shouldBeGetTaskOfId() {
        super.shouldBeGetTaskOfId();
    }

    @Test
    public void shouldBeGetEpicOfId() {
        super.shouldBeGetEpicOfId();
    }

    @Test
    public void shouldBeGetSubtaskOfId() {
        super.shouldBeGetSubtaskOfId();
    }

    @Test
    public void shouldBeRemoveAllTasks() {
        super.shouldBeRemoveAllTasks();
    }

    @Test
    public void shouldBeRemoveAllEpics() {
        super.shouldBeRemoveAllEpics();
    }

    @Test
    public void shouldBeRemoveAllSubtasks() {
        super.shouldBeRemoveAllSubtasks();
    }

    @Test
    public void shouldBeUpdateStatusEpic() {
        super.shouldBeUpdateStatusEpic();
    }

    @Test
    public void shouldBeEpicOfSubtask() {
        super.shouldBeEpicOfSubtask();
    }

    @Test
    public void shouldBeIntersection() {
        super.shouldBeIntersection();
    }

    @Test
    public void shouldBeCloneTask() {
        super.shouldBeCloneTask();
    }


}