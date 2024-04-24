package server;

import com.google.gson.*;
import jsonTypeAdapter.DurationAdapter;
import jsonTypeAdapter.EpicAdapter;
import jsonTypeAdapter.LocalDateTimeAdapter;
import jsonTypeAdapter.SubtaskAdapter;
import jsonTypeToken.EpicListTypeToken;
import jsonTypeToken.SubtaskListTypeToken;
import jsonTypeToken.TaskListTypeToken;
import manager.Managers;
import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HttpTaskServerTest extends TaskManagerTest {

    public static HttpClient client;
    public static Gson gson;

    @BeforeAll
    public static void createHttpClient() {
        client = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter(taskManager))
                .registerTypeAdapter(Epic.class, new EpicAdapter(taskManager))
                .setPrettyPrinting()
                .create();
    }

    @BeforeEach
    public void createTasks() {
        taskManager = Managers.getDefault();
        super.createTasks();
        taskManager.getTaskOfId(1);
        taskManager.getTaskOfId(2);
        taskManager.getSubtaskOfId(6);
        taskManager.getSubtaskOfId(5);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter(taskManager))
                .registerTypeAdapter(Epic.class, new EpicAdapter(taskManager))
                .setPrettyPrinting()
                .create();
    }

    @BeforeEach
    public void startServer() throws IOException {
        HttpTaskServer.start(taskManager);
    }

    @AfterEach
    public void stopServer() {
        HttpTaskServer.stop();
    }

    @Test
    public void shouldBeRequestTasks() {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            List<Task> tasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());
            assertEquals(200, status);
            for (Task t : taskManager.getTaskCollection()) {
                assertTrue(tasks.contains(t), "Полученный список задач не соответствует");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestTaskOfId() {
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            Task task = gson.fromJson(response.body(), Task.class);
            assertEquals(200, status);
            assertEquals(taskManager.getTaskOfId(1), task, "Полученный список задач не соответствует");

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeCreateTask() {
        URI uri = URI.create("http://localhost:8080/tasks");
        String requestBody = gson.toJson(firstTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            assertEquals(201, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeDeleteTask() {
        URI uri = URI.create("http://localhost:8080/tasks");
        String requestBody = gson.toJson(firstTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            assertEquals(200, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestSubtasks() {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            List<Subtask> subtasks = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
            assertEquals(200, status);
            for (Subtask t : taskManager.getSubtaskCollection()) {
                assertTrue(subtasks.contains(t), "Полученный список задач не соответствует");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestSubtaskOfId() {
        URI uri = URI.create("http://localhost:8080/subtasks/5");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            Subtask subtask = gson.fromJson(response.body(), Subtask.class);
            assertEquals(200, status);
            assertEquals(taskManager.getSubtaskOfId(5), subtask, "Полученный список задач не соответствует");

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeCreateSubtask() {
        URI uri = URI.create("http://localhost:8080/subtasks");
        String requestBody = gson.toJson(firstSubtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            assertEquals(201, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeDeleteSubtask() {
        URI uri = URI.create("http://localhost:8080/subtasks");
        String requestBody = gson.toJson(firstSubtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            assertEquals(200, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestEpics() {
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            List<Epic> epics = gson.fromJson(response.body(), new EpicListTypeToken().getType());
            assertEquals(200, status);
            for (Epic t : taskManager.getEpicCollection()) {
                assertTrue(epics.contains(t), "Полученный список задач не соответствует");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestEpicOfId() {
        URI uri = URI.create("http://localhost:8080/epics/4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            Epic epic = gson.fromJson(response.body(), Epic.class);
            assertEquals(200, status);
            assertEquals(taskManager.getEpicOfId(4), epic, "Эпик не соответствует");

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestSubtasksOfEpic() {
        URI uri = URI.create("http://localhost:8080/epics/3/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            List<Subtask> subtasks = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
            for (Subtask t : taskManager.getSubtasksOfEpic(3)) {
                assertTrue(subtasks.contains(t), "Полученный список задач не соответствует");
            }
            assertEquals(200, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeCreateEpic() {
        URI uri = URI.create("http://localhost:8080/epics");
        String requestBody = gson.toJson(secondEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            assertEquals(201, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeDeleteEpic() {
        URI uri = URI.create("http://localhost:8080/epics");
        String requestBody = gson.toJson(firstEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            assertEquals(200, status);

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestHistoryTasks() {
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            List<Task> historyList = new ArrayList<>();
            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                String type = jsonArray.get(i).getAsJsonObject().get("type").getAsString();
                switch (type) {
                    case "TASK":
                        historyList.add(gson.fromJson(jsonArray.get(i), Task.class));
                        break;
                    case "SUBTASK":
                        historyList.add(gson.fromJson(jsonArray.get(i), Subtask.class));
                        break;
                    case "EPIC":
                        historyList.add(gson.fromJson(jsonArray.get(i), Epic.class));
                        break;
                }
            }
            assertEquals(200, status);
            for (Task t : taskManager.getHistoryTasks()) {
                    assertTrue(historyList.contains(t), "Полученный список задач не соответствует");
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

    @Test
    public void shouldBeRequestPrioritized() {
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            List<Task> prioritizedList = new ArrayList<>();
            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                String type = jsonArray.get(i).getAsJsonObject().get("type").getAsString();
                switch (type){
                    case "TASK":
                        prioritizedList.add(gson.fromJson(jsonArray.get(i), Task.class));
                        break;
                    case "SUBTASK":
                        prioritizedList.add(gson.fromJson(jsonArray.get(i), Subtask.class));
                        break;
                }
            }
            assertEquals(200, status);
            for (Task t : taskManager.getPrioritizedTasks()) {
                assertTrue(prioritizedList.contains(t), "Полученный список задач не соответствует");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка запроса ресурса по URL: " + uri + request.method());
        }
    }

}
