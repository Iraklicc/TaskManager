package multimedia.taskmanager; 

import java.time.LocalDate;

/**
 * i Task anaparista ena task mesa ston task manager
 * Perilamvanei plirofories opos titlo, perigrafi, kathgoria, proteraiotita, prothesmia kai katastasi
 */
public class Task {
    private String title;
    private String description;
    private String category; 
    private String priority; 
    private LocalDate deadline;
    private String status;

    // prokathorismeni katastasi gia kathe neo task
    public static final String DEFAULT_STATUS = "Open";

    /**
     * Constructor tis klasis Task
     * arxikopoiei ena neo task me ta dedomena pou dinontai
     * @param title task title
     * @param description task description
     * @param category task category
     * @param priority task priority
     * @param deadline task deadline
     */
    public Task(String title, String description, String category, String priority, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.deadline = deadline;
        this.status = DEFAULT_STATUS; // prokathorismeni katastasi
    }

    /**
     * enimerwnei ta stoixeia enos task
     * @param title new task title
     * @param description new task description
     * @param category new task category
     * @param priority new task priority
     * @param deadline new task deadline
     * @param status new task status
     */
    public void updateTask(String title, String description, String category, String priority, LocalDate deadline, String status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters and Setters

    /**
     * Epistrefei ton titlo tou task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Orizei nea titlo gia to task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * epistrefei tin perigrafi tou task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Orizei nea perigrafi gia to task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * epistrefei tin kathgoria tou task
     */
    public String getCategory() {
        return category;
    }

    /**
     * Orizei nea kathgoria gia to task
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * epistrefei tin proteraiotita tou task
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Orizei nea proteraiotita gia to task
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * epistrefei to deadline tou task
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Orizei neo deadline gia to task
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * epistrefei to status tou task
     */
    public String getStatus() {
        return status;
    }

    /**
     * oriizei neo status gia to task
     */
    public void setStatus(String status) {
        this.status = status;
    }

    // morfopoihsi eksodou

    /**
     * epistrefei mia anaparastasi tou task se morfi String
     * @return string pou exei ola ta stoixeia tou task
     */
    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", deadline=" + deadline +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * epistrefei kalytera to tasks se string
     */
    public String prettyString() {
        return "Title: " + title + "\n" +
               "Description: " + description + "\n" +
               "Category: " + category + " | " +
               "Priority: " + priority + " | " +
               "Deadline: " + deadline + " | " +
               "Status: " + status;
    }
}
