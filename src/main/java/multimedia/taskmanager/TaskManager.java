//taskmanager

package multimedia.taskmanager;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken; 

import java.io.FileReader; 
import java.io.FileWriter; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List; 
import java.util.stream.Collectors;

/**
 * h klasi TaskManager diaxeirizetai tis ergasies tis efarmogis
 * parexei leitourgies gia apothikeusi, fortwsi, prosthiki, epeksergasia kai diagrafi ergasiwn
 */
public class TaskManager {
    private static final String TASK_FILE = "medialab/tasks.json"; // path apothikefsis
    private ArrayList<Task> tasks;

    /**
     * Constructor tis klasis
     * arxikoopoiei ti lista twn ergasiwn kai fortwnei dedomena apo to JSON arxeio
     */
    public TaskManager() {
        tasks = new ArrayList<>(); // arxikopoihsh listas ergasiwn
        loadTasks(); // load apo json arxeio
    }

    /**
     * epistrefei oles tis ergasies
     * @return List<Task> - list me oles tis ergasies
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * prosthetei mia nea ergasia sti lista
     * @param task new task pou tha prostethei
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * epeksergazetai mia uparxousa ergasia me mia nea
     * @param index h thesi tou task pou tha enhmerwthei
     * @param updatedTask to neo task pou tha antikatastisei to palio
     */
    public void updateTask(int index, Task updatedTask) {
        if (index >= 0 && index < tasks.size()) { // elegxos an einai egkyrh h thesi
            tasks.set(index, updatedTask); 
            saveTasks();
        }
    }

    /**
     * diagrafei mia ergasia apo ti lista
     * @param index thesi tis ergasias pou tha diagrafei
     */
    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    /**
     * apothikevei oles tis ergasies sto json arxeio
     * xrhsimopoiei th gson vivliothiki gia na metatrepei ti lista twn ergasiwn se JSON
     */
    public void saveTasks() {
        try {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
                    .create();

            FileWriter writer = new FileWriter(TASK_FILE); 
            gson.toJson(tasks, writer); 
            writer.close(); 
        } catch (Exception e) {
            System.err.println("Failed to save tasks: " + e.getMessage()); // error handling
        }
    }

    /**
     * fortoni tis ergasies apo to json arxeio
     * an to arxeio yparxei, diavazei ta dedomena kai ta metatrepei se mia lista ergasiwn
     */
    private void loadTasks() {
        try {
            if (Files.exists(Path.of(TASK_FILE))) { 
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
                        .create();

                FileReader reader = new FileReader(TASK_FILE); 
                tasks = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());
                reader.close(); 
            }

            // enimerwsi tasks pou exoyn perasei to deadline
            LocalDate today = LocalDate.now(); // simerini hmeromhnia
            for (Task task : tasks) { 
                if (!task.getStatus().equals("Completed") && task.getDeadline().isBefore(today)) { 
                    // an to task den einai olokliromeno kai i prothesmia tou exei perasei
                    task.setStatus("Delayed"); // orizoume to status se Delayed
                }
            }

        } catch (Exception e) {
            System.err.println("Failed to load tasks: " + e.getMessage()); // sfalma
        }
    }

    /**
     * anazhta ergasies me vasi ta dedomena pou dinontai
    * @param title gramma h olos o titlos apo task - mporei na einai kai null
    * @param category epilogi category - mporei na einai kai null
    * @param priority epilogi priority - mporei na einai kai null
    * @return lista pou tairiazei me tin anazitisi
    */
    public List<Task> searchTasks(String title, String category, String priority) {
        return tasks.stream()
                .filter(task -> (title == null || task.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(task -> (category == null || task.getCategory().equalsIgnoreCase(category)))
                .filter(task -> (priority == null || task.getPriority().equalsIgnoreCase(priority)))
                .collect(Collectors.toList());
    }
}
