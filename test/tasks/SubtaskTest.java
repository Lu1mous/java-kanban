package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void shouldBeEqualsSubtaskSameId() {
        Task subtaskFirst = new Task("Задача 1", "Первая задача", TaskStatus.NEW);
        Task subtaskSecond = new Task("Задача 2", "Вторая задача", TaskStatus.NEW);
        subtaskFirst.setId(1);
        subtaskSecond.setId(1);
        assertEquals(subtaskFirst, subtaskSecond, "Объекты Subtask не равны");
    }

}