package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jsontypeadapter.DurationAdapter;
import jsontypeadapter.EpicAdapter;
import jsontypeadapter.LocalDateTimeAdapter;
import jsontypeadapter.SubtaskAdapter;
import manager.TaskIsIntersectionException;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class SubtasksHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;
    private String response;
    private int status;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Subtask.class, new SubtaskAdapter(taskManager))
                .registerTypeAdapter(Epic.class, new EpicAdapter(taskManager))
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
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
        if (path.endsWith("subtasks") || path.endsWith("subtasks/")) {
            Collection<Subtask> subtaskCollection = taskManager.getSubtaskCollection();
            if (subtaskCollection == null) {
                return null;
            }
            String s = gson.toJson(subtaskCollection);
            System.out.println(s);
            return gson.toJson(subtaskCollection);
        }
        String[] splitPath = path.split("/");
        try {
            Subtask subtask = taskManager.getSubtaskOfId(Integer.parseInt(splitPath[2]));
            if (subtask == null) {
                return null;
            }
            return gson.toJson(subtask);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка обработки данных " + exchange.getRequestURI().getPath()
                    + " " + exchange.getRequestMethod());
            e.printStackTrace();
            return null;
        }

    }

    private int responsePOST(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (path.endsWith("subtasks") || path.endsWith("subtasks/")) {
                JsonElement jsonElement = JsonParser.parseString(requestBody);
                if (!jsonElement.isJsonObject()) {
                    return 500;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
                try {
                    if (subtask.getId() <= 0) {
                        taskManager.createSubtask(subtask);
                    } else {
                        taskManager.updateSubtask(subtask);
                    }
                    return 201;
                } catch (TaskIsIntersectionException e) {
                    return 203;
                }
            }
            return 404;
        } catch (IOException e) {
            System.out.println("Ошибка обработки данных " + exchange.getRequestURI().getPath()
                    + " " + exchange.getRequestMethod());
            e.printStackTrace();
            return 500;
        }
    }

    private int responseDELETE(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (path.endsWith("subtasks") || path.endsWith("subtasks/")) {
                JsonElement jsonElement = JsonParser.parseString(requestBody);
                if (!jsonElement.isJsonObject()) {
                    return 500;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int id = jsonObject.get("id").getAsInt();
                taskManager.removeSubtaskOfId(id);
                return 200;
            }
            return 404;
        } catch (IOException e) {
            System.out.println("Ошибка обработки данных " + exchange.getRequestURI().getPath()
                    + " " + exchange.getRequestMethod());
            e.printStackTrace();
            return 500;
        }
    }
}

