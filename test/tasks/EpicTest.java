package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static Epic epicFirst;
    private static Epic epicSecond;

    @BeforeEach
    public void createEpic(){
        epicFirst = new Epic("Задача 1", "Первая задача", TaskStatus.NEW);
        epicSecond = new Epic("Задача 2", "Вторая задача", TaskStatus.NEW);
    }

    @Test
    public void shouldBeEqualsEpicSameId(){
        epicFirst.setId(1);
        epicSecond.setId(1);
        assertEquals(epicFirst, epicSecond, "Объекты Epic не равны");
    }

}