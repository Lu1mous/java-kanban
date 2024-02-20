package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager{

    private ArrayList<Task> historyTasks;

    public InMemoryHistoryManager(){
        historyTasks = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if(historyTasks.size() >= 10){
            historyTasks.remove(0);
        }
        historyTasks.add(task);
    }

    @Override
    public Collection<Task> getHistory() {
        return historyTasks;
    }
}
