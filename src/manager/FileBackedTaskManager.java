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
        saveHistory();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        saveHistory();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        saveHistory();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
        saveHistory();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
        saveHistory();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
        saveHistory();
    }

    @Override
    public void removeTaskOfId(int id) {
        super.removeTaskOfId(id);
        save();
        saveHistory();
    }

    @Override
    public void removeEpicOfId(int id) {
        super.removeEpicOfId(id);
        save();
        saveHistory();
    }

    @Override
    public void removeSubtaskOfId(int id) {
        super.removeSubtaskOfId(id);
        save();
        saveHistory();
    }

    @Override
    public Task getTaskOfId(int id) {
       Task task = super.getTaskOfId(id);
       saveHistory();
       return task;
    }

    @Override
    public Epic getEpicOfId(int id) {
        Epic epic = super.getEpicOfId(id);
        saveHistory();
        return epic;
    }

    @Override
    public Subtask getSubtaskOfId(int id) {
        Subtask subtask = super.getSubtaskOfId(id);
        saveHistory();
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
                fileWriter.write(task.toString());
            }
            for (Epic epic : this.getEpicCollection()) {
                fileWriter.write(epic.toString());
            }
            for (Subtask subtask : this.getSubtaskCollection()) {
                fileWriter.write(subtask.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile() {
        if (!Files.exists(Paths.get(fileName))) {
            return new FileBackedTaskManager();
        }
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            while (fileReader.ready()) {
                String readLine = fileReader.readLine();
                fileBackedTaskManager.fromString(readLine);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        if (!Files.exists(Paths.get(fileHistoryName))) {
            return new FileBackedTaskManager();
        }
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileHistoryName))) {
            while (fileReader.ready()) {
                String readLine = fileReader.readLine();
                fileBackedTaskManager.historyFromString(readLine);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return fileBackedTaskManager;
    }

    private void saveHistory() {
        try {
            Path path = Paths.get(fileHistoryName);
            if (!Files.isDirectory(path)) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        try (Writer fileWriter = new FileWriter(fileHistoryName,StandardCharsets.UTF_8, true)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            List<Task> historyTasks = this.getHistoryTasks();
            Collections.reverse(historyTasks);
            for (Task task : historyTasks) {
                fileWriter.write(task.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private void historyFromString(String value) {
        String[] data = value.split(",");
        switch (data[1]) {
            case "TASK":
                super.getTaskOfId(Integer.parseInt(data[0]));
                break;
            case "EPIC": {
                super.getEpicOfId(Integer.parseInt(data[0]));
                break;
            }
            case "SUBTASK": {
                super.getSubtaskOfId(Integer.parseInt(data[0]));
                break;
            }
        }
    }

    private void fromString(String value) {
        String[] data = value.split(",");
        switch (data[1]) {
            case "TASK":
                Task task = new Task(data[2], data[4], TaskStatus.valueOf(data[3]));
                task.setId(Integer.parseInt(data[0]));
                super.createTask(task);
                break;
            case "EPIC": {
                Epic epic = new Epic(data[2], data[4], TaskStatus.valueOf(data[3]));
                epic.setId(Integer.parseInt(data[0]));
                super.createEpic(epic);
                break;
            }
            case "SUBTASK": {
                Epic epic = super.getEpicOfId(Integer.parseInt(data[5]));
                Subtask subtask = new Subtask(data[2], data[4], TaskStatus.valueOf(data[3]), epic);
                subtask.setId(Integer.parseInt(data[0]));
                super.createSubtask(subtask);
                break;
            }
        }
    }

}

