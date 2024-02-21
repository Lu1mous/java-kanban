package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{

    private ArrayDeque<Task> historyTasks;

    public InMemoryHistoryManager(){
        historyTasks = new ArrayDeque<>(10);
    }

    @Override
    public void add(Task task) {
        if(historyTasks.size() >= 10){
            historyTasks.removeFirst();
        }
        historyTasks.add(task);
    }

    @Override
    public Collection<Task> getHistory() {
        return historyTasks;
    }
}
