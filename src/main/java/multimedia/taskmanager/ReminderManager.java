//remindersmanager

package multimedia.taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * i class ReminderManager diaxeirizetai tis ypenthimiseis twn tasks
 *  apothikevei, fortwnei kai epeksergazetai tis ypenthimiseis
 */
public class ReminderManager {
    private static final String REMINDER_FILE = "medialab/reminders.json"; // path gia to arxeio json
    private List<Reminder> reminders; // lista me ola ta reminders

    /**
     * constructor tis klasis
     * arxikopoiei tis ypenthimiseis kai tis fortwnei apo to json
     */
    public ReminderManager() {
        this.reminders = new ArrayList<>();
        loadReminders(); // fortosi twn reminders apo to json
    }

    /**
     * prosthetei mia nea ypenthimisi sti lista kai apothikevei ta dedomena
     * @param reminder h ypenthimisi pou tha prostethei
     */
    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
        saveReminders();
    }

    /**
     * diagrafei mia ypenthimisi apo ti lista kai apothikevei ta dedomena
     * @param reminder reminder pros diagrafi
     */
    public void removeReminder(Reminder reminder) {
        reminders.remove(reminder);
        saveReminders();
    }

    /**
     * diagrafei oles tis ypenthimiseis pou sxetizontai me mia sigkekrimeni ergasia
     * afti i methodos kaleitai otan mia ergasia allazei katastasi se Completed
     * @param task to task tou opoiou ta reminders tha diagrafoun
     */
    public void removeRemindersForTask(Task task) {
        reminders.removeIf(reminder -> reminder.getTask().equals(task));
        saveReminders(); 
    }

    /**
     * epistrefei oles tis ypenthimiseis
     * @return List<Reminder> - lista me ola ta reminders
     */
    public List<Reminder> getReminders() {
        return reminders;
    }

    /**
     * apothekevei tis ypenthimiseis sto arxeio json
     */
    public void saveReminders() {
        try {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            
            FileWriter writer = new FileWriter(REMINDER_FILE);
            gson.toJson(reminders, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to save reminders: " + e.getMessage());
        }
    }

    /**
     * fortwnei tis ypenthimiseis apo to json
     */
    private void loadReminders() {
        try {
            if (Files.exists(Path.of(REMINDER_FILE))) { // elegxos an yparxei to arxeio
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
                        .create();

                FileReader reader = new FileReader(REMINDER_FILE);
                reminders = gson.fromJson(reader, new TypeToken<List<Reminder>>() {}.getType()); // metatropi piso se list
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to load reminders: " + e.getMessage());
        }
    }
    
}
