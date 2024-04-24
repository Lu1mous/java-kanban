package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jsontypeadapter.DurationAdapter;
import jsontypeadapter.EpicAdapter;
import jsontypeadapter.LocalDateTimeAdapter;
import jsontypeadapter.SubtaskAdapter;
import jsonTypeToken.TaskListTypeToken;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class PriorityHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;

    public PriorityHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Subtask.class, new SubtaskAdapter(taskManager))
                .registerTypeAdapter(Epic.class, new EpicAdapter(taskManager)).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter());
        this.gson = gsonBuilder.create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                Collection<Task> priorityTask = taskManager.getPrioritizedTasks();
                if (priorityTask == null) {
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().close();
                    return;
                }
                String response = gson.toJson(priorityTask, new TaskListTypeToken().getType());
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            default:
                exchange.sendResponseHeaders(500, 0);
                exchange.getResponseBody().close();
                break;
        }
    }
}
