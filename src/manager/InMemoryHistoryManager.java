package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> historyTasks;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        historyTasks = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        int id = task.getId();
        if (historyTasks.containsKey(id)) {
            remove(id);
        }
        linkLast(task);
        historyTasks.put(id,tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = historyTasks.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public void clear() {
        historyTasks.clear();
    }

    private void linkLast(Task task) {
        Node oldTail = tail;
        tail = new Node(task);
        if (oldTail == null) {
            head = tail;
        } else {
            oldTail.next = tail;
            tail.prev = oldTail;
        }
    }

    private void removeNode(Node node) {
        Node next = node.next;
        Node prev = node.prev;
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (tail != null) {
            tasks.add(tail.data);
            Node prevNode = tail.prev;
            while (prevNode != null) {
                tasks.add(prevNode.data);
                prevNode = prevNode.prev;
            }
        }
        return tasks;
    }

}
