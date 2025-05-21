package multimedia.taskmanager;

import java.time.LocalDate;

/**
 * H Reminder class einai mia ypenthimisi pou syndeetai me mia ergasia.
 * Perilamvanei tin ergasia stin opoia anaferetai, tin imerominia tis ypenthimisis kai ena minima.
 */
public class Reminder {
    private Task task;
    private LocalDate reminderDate; 
    private String message;

    /**
     * Constructor tis klasis Reminder.
     * @param task to task pou anaferetai i ypenthimisi
     * @param reminderDate h imerominia tis ypenthimisis
     * @param message to minima tis ypenthimisis
     * @throws IllegalArgumentException an i imerominia tis ypenthimisis einai meta tis prothesmias tou task
     */
    public Reminder(Task task, LocalDate reminderDate, String message) {
        if (reminderDate.isAfter(task.getDeadline())) {
            throw new IllegalArgumentException("Reminder date cannot be after the task deadline.");
        }
        this.task = task;
        this.reminderDate = reminderDate;
        this.message = message;
    }

    /**
     * Epistrefei tin ergasia pou sxetizetai me tin ypenthimisi
     * @return task ypenthimisis
     */
    public Task getTask() {
        return task;
    }

    
    //Epistrefei tin imerominia tis ypenthimisis
    public LocalDate getReminderDate() {
        return reminderDate;
    }

    
    //Epistrefei to minima tis ypenthimisis
    public String getMessage() {
        return message;
    }

    /**
     * Orizei nea imerominia ypenthimisis
     * @param reminderDate h nea imerominia
     * @throws IllegalArgumentException elegxos an i imerominia einai meta tis prothesmias tis ergasias
     */
    public void setReminderDate(LocalDate reminderDate) {
        if (reminderDate.isAfter(task.getDeadline())) { // elegxos
            throw new IllegalArgumentException("Reminder date cannot be after the task deadline.");
        }
        this.reminderDate = reminderDate;
    }

    /**
     * Orizei nea minima ypenthimisis
     * @param message minima
     */
    public void setMessage(String message) {
        this.message = message;
    }

    //epistrefei tin ypenthimisi se morfi String
    @Override
    public String toString() {
        return "Reminder for Task: " + task.getTitle() +
               " | Date: " + reminderDate +
               " | Message: " + message;
    }
}
