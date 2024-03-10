package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void shouldBeEqualsTaskSameId() {
        Task taskFirst = new Task("Задача 1", "Первая задача", TaskStatus.NEW);
        Task taskSecond = new Task("Задача 2", "Вторая задача", TaskStatus.NEW);
        taskFirst.setId(1);
        taskSecond.setId(1);
        assertEquals(taskFirst, taskSecond, "Объекты Task не равны");
    }

}