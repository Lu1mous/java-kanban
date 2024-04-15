package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static String fileName = "saveTasks.txt";
    private static String fileHistoryName = "saveHistoryTasks.txt";

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeTaskOfId(int id) {
        super.removeTaskOfId(id);
        save();
    }

    @Override
    public void removeEpicOfId(int id) {
        super.removeEpicOfId(id);
        save();
    }

    @Override
    public void removeSubtaskOfId(int id) {
        super.removeSubtaskOfId(id);
        save();
    }

    @Override
    public Task getTaskOfId(int id) {
       Task task = super.getTaskOfId(id);
       save();
       return task;
    }

    @Override
    public Epic getEpicOfId(int id) {
        Epic epic = super.getEpicOfId(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskOfId(int id) {
        Subtask subtask = super.getSubtaskOfId(id);
        save();
        return subtask;
    }



    private void save() {
        try {
            Path path = Paths.get(fileName);
            if (!Files.isDirectory(path)) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        try (Writer fileWriter = new FileWriter(fileName,StandardCharsets.UTF_8, true)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : this.getTaskCollection()) {
                fileWriter.write(ConverterTask.taskToCsv(task));
            }
            for (Epic epic : this.getEpicCollection()) {
                fileWriter.write(ConverterTask.epicToCsv(epic));
            }
            for (Subtask subtask : this.getSubtaskCollection()) {
                fileWriter.write(ConverterTask.subtaskToCsv(subtask));
            }
            List<Task> historyTasks = this.getHistoryTasks();
            Collections.reverse(historyTasks);
            for (Task task : historyTasks) {
                fileWriter.write(task.getId() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile(String fileName) {
        if (!Files.exists(Paths.get(fileName))) {
            return new FileBackedTaskManager();
        }
        int idCount = 0;
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            while (fileReader.ready()) {
                String readLine = fileReader.readLine();
                if (readLine.contains(",")) {
                    Task task = fileBackedTaskManager.fromString(readLine);
                    if (task != null) {
                        if (task.getId() > idCount) {
                            idCount = task.getId();
                        }
                    }
                } else {
                    fileBackedTaskManager.historyFromString(readLine);
                }
            }
            fileBackedTaskManager.idCount = idCount;
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
        return fileBackedTaskManager;
    }

    private void historyFromString(String value) {
        int id = Integer.parseInt(value);
        super.getTaskOfId(id);
        super.getEpicOfId(id);
        super.getSubtaskOfId(id);
    }

    private Task fromString(String value) {
        String[] data = value.split(",");
        DateTimeFormatter format = Task.getDataTimeFormat();
        switch (data[1]) {
            case "TASK":
                Task task = new Task(data[2], data[4], TaskStatus.valueOf(data[3]),
                        LocalDateTime.parse(data[5], format),
                        Duration.of(Integer.parseInt(data[6]), ChronoUnit.MINUTES));
                task.setId(Integer.parseInt(data[0]));
                super.tasks.put(task.getId(), task);
                return task;
            case "EPIC": {
                Epic epic = new Epic(data[2], data[4], TaskStatus.valueOf(data[3]));
                epic.setId(Integer.parseInt(data[0]));
                super.epics.put(epic.getId(), epic);
                return epic;
            }
            case "SUBTASK": {
                Epic epic = epics.get(Integer.parseInt(data[5]));
                Subtask subtask = new Subtask(data[2], data[4], TaskStatus.valueOf(data[3]), epic,
                        LocalDateTime.parse(data[6], format),
                        Duration.of(Integer.parseInt(data[7]), ChronoUnit.MINUTES));
                subtask.setId(Integer.parseInt(data[0]));
                super.subtasks.put(subtask.getId(), subtask);
                return subtask;
            }
        }
        return null;
    }

}

