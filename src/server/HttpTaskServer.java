package server;

import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTaskManager;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    static boolean start = true;

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = FileBackedTaskManager.loadFromFile("saveTasks.txt");
        start(taskManager);
        Scanner in = new Scanner(System.in);
        while (start) {
            String str = in.next();
            if (str.equals("exit")) {
                start = false;
                stop();
            }
        }
    }

    public static void start(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtask", new SubtasksHandler(taskManager));
        httpServer.createContext("/epic", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(0);
    }
}
