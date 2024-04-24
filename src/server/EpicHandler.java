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

public class EpicHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;
    private String response;
    private int status;

    public EpicHandler(TaskManager taskManager) {
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
        if (path.endsWith("epics") || path.endsWith("epics/")) {
            Collection<Epic> epicCollection = taskManager.getEpicCollection();
            if (epicCollection == null) {
                return null;
            }
            return gson.toJson(epicCollection);
        }
        String[] splitPath = path.split("/");
        try {
            if (splitPath.length == 3) {
                Epic epic = taskManager.getEpicOfId(Integer.parseInt(splitPath[2]));
                if (epic == null) {
                    return null;
                }
                return gson.toJson(epic);
            }
            Collection<Subtask> subtaskCollection  = taskManager.getSubtasksOfEpic(Integer.parseInt(splitPath[2]));
            return gson.toJson(subtaskCollection);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private int responsePOST(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (path.endsWith("epics") || path.endsWith("epics/")) {
                JsonElement jsonElement = JsonParser.parseString(requestBody);
                if (!jsonElement.isJsonObject()) {
                    return 500;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Epic epic = gson.fromJson(jsonObject, Epic.class);
                try {
                    if (epic.getId() <= 0) {
                        taskManager.createEpic(epic);
                    } else {
                        taskManager.updateEpic(epic);
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
            if (path.endsWith("epics") || path.endsWith("epics/")) {
                JsonElement jsonElement = JsonParser.parseString(requestBody);
                if (!jsonElement.isJsonObject()) {
                    return 500;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int id = jsonObject.get("id").getAsInt();
                taskManager.removeEpicOfId(id);
                return 200;
            }
            return 404;
        } catch (IOException e) {
            return 500;
        }
    }
}
