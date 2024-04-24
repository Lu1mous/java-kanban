package jsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.TaskManager;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SubtaskAdapter extends TypeAdapter<Subtask> {
    private TaskManager taskManager;

    public SubtaskAdapter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public  void write(final JsonWriter jsonWriter, final Subtask subtask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(subtask.getName());
        jsonWriter.name("description").value(subtask.getDescription());
        jsonWriter.name("status").value(subtask.getStatus().toString());
        jsonWriter.name("id").value(subtask.getId());
        jsonWriter.name("type").value(subtask.getType().toString());
        jsonWriter.name("startTime").value(subtask.getStartTime().format(Task.getDataTimeFormat()));
        jsonWriter.name("duration").value(subtask.getDuration().toMinutes());
        jsonWriter.name("idEpic").value(subtask.getEpic().getId());
        jsonWriter.endObject();
    }

    @Override
    public Subtask read(final JsonReader jsonReader) throws IOException {

        String name = "";
        String description = "";
        int id = 0;
        TaskStatus status = null;
        TypeTask type = null;
        LocalDateTime startTime = null;
        Duration duration = null;
        Epic epic = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {

            switch (jsonReader.nextName()) {
                case "name":
                    name = jsonReader.nextString();
                    break;
                case "description":
                    description = jsonReader.nextString();
                    break;
                case "status":
                    status = TaskStatus.valueOf(jsonReader.nextString());
                    break;
                case "id":
                    id = Integer.parseInt(jsonReader.nextString());
                    break;
                case "type":
                    type = TypeTask.valueOf(jsonReader.nextString());
                    break;
                case "startTime":
                    startTime = LocalDateTime.parse(jsonReader.nextString(), Task.getDataTimeFormat());
                    break;
                case "duration":
                    duration = Duration.of(jsonReader.nextInt(), ChronoUnit.MINUTES);
                    break;
                case "idEpic":
                    epic = taskManager.getEpicOfIdWithoutHistory(Integer.parseInt(jsonReader.nextString()));
                    if (epic == null) {
                        throw new IOException();
                    }
                    break;
            }
        }
        jsonReader.endObject();

        Subtask subtask = new Subtask(name, description, status, epic, startTime, duration);
        subtask.setId(id);
        return subtask;
    }
}
