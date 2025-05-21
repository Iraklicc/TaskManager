package multimedia.taskmanager;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken; 

import java.io.FileReader; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Path;
import java.util.HashSet; 

/**
 * H PriorityManager diaxeirizetai tis proraitotites twn tasks
 * apothikevei, fortwnei kai epeksergazetai tis proraitotites xrhsimopoiontas ena json arxeio
 */
public class PriorityManager {
    private static final String PRIORITY_FILE = "medialab/priorities.json"; // path apothikefshs twn proraitotitwn
    private static final String DEFAULT_PRIORITY = "Default"; // Prokathorismeni proraitotita - DEN mpoorei na diagrafei
    private HashSet<String> priorities;

    /**
     * Constructor pou arxikopoiei tis proraitotites kai tis fortwnei apo to json
     * Exasfalizei oti h prokathorismeni proraitotita ("Default") uparxei panta
     */
    public PriorityManager() {
        priorities = new HashSet<>();
        loadPriorities(); 
        priorities.add(DEFAULT_PRIORITY);
    }

    /**
     * epistrefei oles tis proraitotites
     * @return HashSet<String> - oi proraitotites twn tasks
     */
    public HashSet<String> getPriorities() {
        return priorities;
    }

    /**
     * prosthetei nea proraitotita
     * @param priority  h new priority
     * @return boolean - true,false klassikos elegxos
     */
    public boolean addPriority(String priority) {
        if (priorities.contains(priority) || priority.equals(DEFAULT_PRIORITY)) {
            return false; // Den mporei na prosthesei proraitotita pou uparxei hdh h einai "Default"
        }
        priorities.add(priority); 
        savePriorities();
        return true;
    }

    /**
     * Epeksergazetai mia uparxousa proraitotita allazontas to onoma tis
     * @param oldPriority old name
     * @param newPriority new name
     * @return boolean - true/false klassikos elegxos
     */
    public boolean editPriority(String oldPriority, String newPriority) {
        if (!priorities.contains(oldPriority) || priorities.contains(newPriority) || oldPriority.equals(DEFAULT_PRIORITY)) {
            return false; 
        }
        priorities.remove(oldPriority); 
        priorities.add(newPriority);
        savePriorities();
        return true;
    }

    /**
     * Diagrafei mia proraitotita apo ti lista
     * @param priority h proraitotita pros diagrafi
     * @return boolean gia elegxo
     */
    public boolean deletePriority(String priority) {
        if (priority.equals(DEFAULT_PRIORITY)) {
            return false; // Den mporei na diagrafei h prokathorismeni proraitotita
        }
        boolean removed = priorities.remove(priority); // attempt to remove the priority
        if (removed) {
            savePriorities(); // apothikevoume tis allages sto json
        }
        return removed;
    }

    /**
     * Apothikevei tis proraitotites sto json
     * Xrhsimopoiei tin vivliothiki Gson gia na metatrepei to HashSet se json kai na to grapsei sto arxeio
     */
    public void savePriorities() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(PRIORITY_FILE);
            gson.toJson(priorities, writer); 
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to save priorities: " + e.getMessage());
        }
    }

    /**
     * Fortwnei tis proraitotites apo to json
     * An to arxeio yparxei diavazei ta dedomena kai ta metatrepei se HashSet<String>
     */
    private void loadPriorities() {
        try {
            if (Files.exists(Path.of(PRIORITY_FILE))) { // elegxos an yparxei
                Gson gson = new Gson(); // dhmiourgia enos Gson antikeimenou
                FileReader reader = new FileReader(PRIORITY_FILE); 
                priorities = gson.fromJson(reader, new TypeToken<HashSet<String>>() {}.getType());
                reader.close(); 
            }
        } catch (IOException e) {
            System.err.println("Failed to load priorities: " + e.getMessage()); // ektyposi minimatois sflamatos
        }
    }
}
