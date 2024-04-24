package jsontypeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.TaskManager;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EpicAdapter extends TypeAdapter<Epic> {
    private TaskManager taskManager;

    public EpicAdapter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public  void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("status").value(epic.getStatus().toString());
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("type").value(epic.getType().toString());
        jsonWriter.name("startTime").value(epic.getStartTime().format(Task.getDataTimeFormat()));
        jsonWriter.name("endTime").value(epic.getEndTime().format(Task.getDataTimeFormat()));
        jsonWriter.name("duration").value(epic.getDuration().toMinutes());
        jsonWriter.endObject();
    }

    @Override
    public Epic read(final JsonReader jsonReader) throws IOException {

        String name = "";
        String description = "";
        int id = 0;
        TaskStatus status = null;
        TypeTask type = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = null;

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
                case "endTime":
                    endTime = LocalDateTime.parse(jsonReader.nextString(), Task.getDataTimeFormat());
                    break;
                case "duration":
                    duration = Duration.of(jsonReader.nextInt(), ChronoUnit.MINUTES);
                    break;
            }
        }
        jsonReader.endObject();

        Epic epic = new Epic(name, description, status, startTime, endTime, duration);
        epic.setId(id);
        return epic;
    }
}
