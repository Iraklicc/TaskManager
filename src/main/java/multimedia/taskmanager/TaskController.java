//taskcontroller

package multimedia.taskmanager;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

/**
 * to taskcontroller diaxeirizetai to UI twn tasks
 */
public class TaskController {
    private TaskManager taskManager;
    private CategoryManager categoryManager;
    private PriorityManager priorityManager;
    private ListView<String> taskListView;

    /**
     * constructor gia ton TaskController
     */
    public TaskController(TaskManager taskManager, CategoryManager categoryManager, PriorityManager priorityManager, ListView<String> taskListView) {
        this.taskManager = taskManager;
        this.categoryManager = categoryManager;
        this.priorityManager = priorityManager;
        this.taskListView = taskListView;
    }

    /**
     * emfanizei ena popup gia prosthiki neou task
     */
    public void showAddTaskPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Add New Task");

        VBox form = new VBox();
        form.setSpacing(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(categoryManager.getCategories());
        categoryComboBox.setPromptText("Category");

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(priorityManager.getPriorities());
        priorityComboBox.setPromptText("Priority");

        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Deadline");

        Button saveButton = new Button("Save Task");
        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();

            if (title.isEmpty() || description.isEmpty() || category == null || priority == null || deadline == null) {
                showAlert("Error", "All fields must be filled out!");
                return;
            }

            Task newTask = new Task(title, description, category, priority, deadline);
            taskManager.addTask(newTask);
            updateTaskListView();
            popupStage.close();
        });

        form.getChildren().addAll(titleField, descriptionField, categoryComboBox, priorityComboBox, deadlinePicker, saveButton);
        Scene popupScene = new Scene(form, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * emfanizei ena popup gia epeksergasia enos task
     */
    public void showEditTaskPopup() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Error", "No task selected!");
            return;
        }

        Task selectedTask = taskManager.getTasks().get(selectedIndex);

        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Task");

        VBox form = new VBox();
        form.setSpacing(10);

        TextField titleField = new TextField(selectedTask.getTitle());
        TextField descriptionField = new TextField(selectedTask.getDescription());

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(categoryManager.getCategories());
        categoryComboBox.setValue(selectedTask.getCategory());

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(priorityManager.getPriorities());
        priorityComboBox.setValue(selectedTask.getPriority());

        DatePicker deadlinePicker = new DatePicker(selectedTask.getDeadline());

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();

            if (title.isEmpty() || description.isEmpty() || category == null || priority == null || deadline == null) {
                showAlert("Error", "All fields must be filled out!"); // error message
                return;
            }

            Task updatedTask = new Task(title, description, category, priority, deadline);
            taskManager.updateTask(selectedIndex, updatedTask);
            updateTaskListView();
            popupStage.close();
        });

        form.getChildren().addAll(titleField, descriptionField, categoryComboBox, priorityComboBox, deadlinePicker, saveButton);
        Scene popupScene = new Scene(form, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * diagrafei tin epilgmeni ergasia me epivevaiwsi apo ton xristi
     */
    public void deleteSelectedTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Error", "No task selected!"); // error message
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this task?"); //epivevaiwsh diagrafis
        confirmation.setContentText("This action cannot be undone.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                taskManager.deleteTask(selectedIndex);
                updateTaskListView();
                showAlert("Success", "Task deleted successfully.");
            }
        });
    }

    /**
     * enimerwnei tin lista ergasion sto UI me mia kathorismeni lista ergasion
     * @param tasksToShow h lista twn task pou emfanizetai
     */
    public void updateTaskListView(List<Task> tasksToShow) {
        taskListView.getItems().clear();
        for (Task task : tasksToShow) {
            taskListView.getItems().add(task.prettyString());
        }
    }

    /**
     * enhmerwnei tin lista ergasion sto UI me ola ta tasks apo to TaskManager
     */
    public void updateTaskListView() {
        updateTaskListView(taskManager.getTasks());
    }

    /**
     * emfanizei ena pop-up gia tin enimerwsi tis katastasis enos task
     */
    public void showUpdateStatusPopup() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Error", "No task selected!");
            return;
        }

        Task selectedTask = taskManager.getTasks().get(selectedIndex);

        Stage popupStage = new Stage();
        popupStage.setTitle("Update Task Status");

        VBox form = new VBox();
        form.setSpacing(10);

        Label label = new Label("Select new status:");
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Open", "In Progress", "Completed");
        statusComboBox.setValue(selectedTask.getStatus());

        Button updateButton = new Button("Update Status");
        updateButton.setOnAction(e -> {
            String newStatus = statusComboBox.getValue();
            if (!selectedTask.getStatus().equals(newStatus)) {
                selectedTask.setStatus(newStatus);
                taskManager.updateTask(selectedIndex, selectedTask);
                updateTaskListView();
            }
            popupStage.close();
        });

        form.getChildren().addAll(label, statusComboBox, updateButton);
        Scene popupScene = new Scene(form, 300, 150);
        popupStage.setScene(popupScene);
        popupStage.show();
    }


    /**
     * emfanizei ena pop-up gia tin prosthiki ypenthymisis se ena task
     */
    public void showSearchTaskPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Search Tasks");

        VBox form = new VBox();
        form.setSpacing(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Search by Title");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(categoryManager.getCategories());
        categoryComboBox.setPromptText("Category (Optional)");

        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(priorityManager.getPriorities());
        priorityComboBox.setPromptText("Priority (Optional)");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String title = titleField.getText().isEmpty() ? null : titleField.getText();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();

            List<Task> results = taskManager.searchTasks(title, category, priority);
            updateTaskListView(results);
            popupStage.close();
        });

        // Koumpi gia episrofi se ola task prin tin anazitisi
        Button resetButton = new Button("Reset Search");
        resetButton.setOnAction(e -> {
            updateTaskListView(taskManager.getTasks()); // kanei return all tasks back
            popupStage.close();
        });

        form.getChildren().addAll(titleField, categoryComboBox, priorityComboBox, searchButton, resetButton);
        Scene popupScene = new Scene(form, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.show();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
