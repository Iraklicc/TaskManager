//App.java

package multimedia.taskmanager;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    // diloseis ton managers pou diaxeirizontai ta tasks, ta categories, ta priorities kai ta reminders   
    private TaskManager taskManager;
    private CategoryManager categoryManager;
    private PriorityManager priorityManager;
    private ReminderManager reminderManager;

    // diloseis ton controllers pou diaxeirizontai ta tasks, ta categories, ta priorities kai ta reminders
    private TaskController taskController;
    private CategoryController categoryController;
    private PriorityController priorityController;
    private ReminderController reminderController;

    // gia emfanisi tasks se lista
    private ListView<String> taskListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Manager");

        // Arxikopoioume managers
        taskManager = new TaskManager();
        categoryManager = new CategoryManager();
        priorityManager = new PriorityManager();
        reminderManager = new ReminderManager();

        // Arxikopoioume controllers kai sindeoume me managers kai lista
        taskController = new TaskController(taskManager, categoryManager, priorityManager, taskListView);
        categoryController = new CategoryController(categoryManager);
        priorityController = new PriorityController(priorityManager);
        reminderController = new ReminderController(reminderManager, taskManager);

        // layout me BorderPane
        BorderPane root = new BorderPane();
  
        // panel gia medialb assistant
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));

        // medialab assistant
        Label totalTasksLabel = new Label("Total Tasks: " + taskManager.getTasks().size());
        Label completedTasksLabel = new Label("Completed Tasks: " + taskManager.getTasks().stream().filter(task -> task.getStatus().equals("Completed")).count());
        Label delayedTasksLabel = new Label("Delayed Tasks: " + taskManager.getTasks().stream().filter(task -> task.getStatus().equals("Delayed")).count());
        Label upcomingTasksLabel = new Label("Upcoming Tasks (7 days): " + taskManager.getTasks().stream().filter(task -> !task.getStatus().equals("Completed") && task.getDeadline().isBefore(LocalDate.now().plusDays(7))).count());

        // prosthiki etiketon sto panel
        topPanel.getChildren().addAll(totalTasksLabel, completedTasksLabel, delayedTasksLabel, upcomingTasksLabel);
        root.setTop(topPanel);

        // rithmisi upsous tis listas ergasion kai prosthiki tis sto kentro tou layout
        taskListView.setPrefHeight(400);
        root.setCenter(taskListView);
        taskController.updateTaskListView();

        // Elegxos gia delayed tasks kai eidopoihsh
        long delayedTasks = taskManager.getTasks().stream()
                .filter(task -> task.getStatus().equals("Delayed"))
                .count();
        if (delayedTasks > 0) {
            showAlert("Attention", "You have " + delayedTasks + " delayed tasks!");
        }

        // dimiourgia menu bar
        MenuBar menuBar = createMenuBar();
        root.setTop(new VBox(menuBar, topPanel));

        // add task sto kato meros tou layout
        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(e -> taskController.showAddTaskPopup());

        HBox buttonBox = new HBox(addTaskButton);
        buttonBox.setSpacing(10);
        root.setBottom(buttonBox);

        // scene kai emfanisi window
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // apothikefsi olon ton dedomenon sto kleisimo
        primaryStage.setOnCloseRequest(e -> {
            taskManager.saveTasks();
            categoryManager.saveCategories();
            priorityManager.savePriorities();
            reminderManager.saveReminders();
        });
    }

    // method gia dimiourgia menu bar
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // menu gia diaxeirisi tasks
        Menu taskMenu = new Menu("Task");
        MenuItem editTask = new MenuItem("Edit Selected Task");
        editTask.setOnAction(e -> taskController.showEditTaskPopup());
        MenuItem deleteTask = new MenuItem("Delete Selected Task");
        deleteTask.setOnAction(e -> taskController.deleteSelectedTask());
        taskMenu.getItems().addAll(editTask, deleteTask);

        // menu gia diaxeirisi categories
        Menu categoryMenu = new Menu("Category");
        MenuItem addCategory = new MenuItem("Add Category");
        addCategory.setOnAction(e -> categoryController.showAddCategoryPopup());
        MenuItem editCategory = new MenuItem("Edit Category");
        editCategory.setOnAction(e -> categoryController.showEditCategoryPopup());
        MenuItem deleteCategory = new MenuItem("Delete Category");
        deleteCategory.setOnAction(e -> categoryController.showDeleteCategoryPopup());
        categoryMenu.getItems().addAll(addCategory, editCategory, deleteCategory);

        // menu gia diaxeirisi priorities
        Menu priorityMenu = new Menu("Priority");
        MenuItem addPriority = new MenuItem("Add Priority");
        addPriority.setOnAction(e -> priorityController.showAddPriorityPopup());
        MenuItem editPriority = new MenuItem("Edit Priority");
        editPriority.setOnAction(e -> priorityController.showEditPriorityPopup());
        MenuItem deletePriority = new MenuItem("Delete Priority");
        deletePriority.setOnAction(e -> priorityController.showDeletePriorityPopup());
        priorityMenu.getItems().addAll(addPriority, editPriority, deletePriority);

        // menu gia diaxeirisi reminders
        Menu reminderMenu = new Menu("Reminders");
        MenuItem addReminder = new MenuItem("Add Reminder");
        addReminder.setOnAction(e -> reminderController.showAddReminderPopup());
        MenuItem editReminder = new MenuItem("Edit Reminder");
        editReminder.setOnAction(e -> reminderController.showEditReminderPopup());
        MenuItem deleteReminder = new MenuItem("Delete Reminder");
        deleteReminder.setOnAction(e -> reminderController.showDeleteReminderPopup());
        reminderMenu.getItems().addAll(addReminder, editReminder, deleteReminder);

        // vazoume ta parapano menu sto menu bar
        menuBar.getMenus().addAll(taskMenu, categoryMenu, priorityMenu, reminderMenu);

        // namu gia anazitisi tasks
        Menu searchMenu = new Menu("Search");
        MenuItem searchTask = new MenuItem("Search Tasks");
        searchTask.setOnAction(e -> taskController.showSearchTaskPopup());
        searchMenu.getItems().add(searchTask);

        // vazoume kai to menu gia anazitisi sto menu bar
        menuBar.getMenus().add(searchMenu);

        // enimerosi katastasis ergasion
        MenuItem updateStatus = new MenuItem("Update Task Status");
        updateStatus.setOnAction(e -> taskController.showUpdateStatusPopup());
        taskMenu.getItems().add(updateStatus);

        return menuBar;
    }

    // method gia emfanisi eidopoiisis
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ekkinisi tou GUI
    public static void main(String[] args) {
        launch(args);
    }
}
