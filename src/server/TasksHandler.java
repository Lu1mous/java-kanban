package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jsonTypeAdapter.DurationAdapter;
import jsonTypeAdapter.LocalDateTimeAdapter;
import manager.TaskIsIntersectionException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class TasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    private String response;
    private int status;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter());
        this.gson = gsonBuilder.create();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "GET":
                System.out.println("GET");
                response = responseGET(httpExchange);
                System.out.println(response);
                if (response == null) {
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.getResponseBody().close();
                    return;
                }

                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                break;
            case "POST":
                System.out.println("POST");
                status = responsePOST(httpExchange);
                httpExchange.sendResponseHeaders(status, 0);
                httpExchange.getResponseBody().close();
                break;
            case "DELETE":
                status = responseDELETE(httpExchange);
                httpExchange.sendResponseHeaders(status, 0);
                httpExchange.getResponseBody().close();
                System.out.println("DELETE");
                break;
            default:
                System.out.println("Такого метода нет");
                break;
        }
    }

    private String responseGET(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (path.endsWith("tasks") || path.endsWith("tasks/")) {
            Collection<Task> taskCollection = taskManager.getTaskCollection();
            if (taskCollection == null) {
                return null;
            }
            return gson.toJson(taskCollection);
        }
        String[] splitPath = path.split("/");
        try {
            Task task = taskManager.getTaskOfId(Integer.parseInt(splitPath[2]));
            if (task == null) {
                return null;
            }
            return gson.toJson(task);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private int responsePOST(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (path.endsWith("tasks") || path.endsWith("tasks/")) {
                JsonElement jsonElement = JsonParser.parseString(requestBody);
                if (!jsonElement.isJsonObject()) {
                    return 500;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Task task = gson.fromJson(jsonObject, Task.class);
                try {
                    if (task.getId() <= 0) {
                        taskManager.createTask(task);
                    } else {
                        taskManager.updateTask(task);
                    }
                    return 201;
                } catch (TaskIsIntersectionException e) {
                    return 406;
                }
            }
            return 404;
        } catch (IOException e) {
            return 500;
        }
    }

    private int responseDELETE(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (path.endsWith("tasks") || path.endsWith("tasks/")) {
                JsonElement jsonElement = JsonParser.parseString(requestBody);
                if (!jsonElement.isJsonObject()) {
                    return 500;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int id = jsonObject.get("id").getAsInt();
                taskManager.removeTaskOfId(id);
                return 200;
            }
            return 404;
        } catch (IOException e) {
            return 500;
        }
    }
}
