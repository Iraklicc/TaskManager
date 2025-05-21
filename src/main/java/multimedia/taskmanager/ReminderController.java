package multimedia.taskmanager;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * H klasi ReminderController diaxeirizetai to UI twn ypenthymisewn
 */
public class ReminderController {
    private ReminderManager reminderManager;
    private TaskManager taskManager;

    /**
     * Constructor 
     * @param reminderManager gia diaxeirisi twn ypenthymisewn
     * @param taskManager gia diaxeirisi twn tasks
     */
    public ReminderController(ReminderManager reminderManager, TaskManager taskManager) {
        this.reminderManager = reminderManager;
        this.taskManager = taskManager;
    }

    /**
     * Emfanizei ena pop-up parathuro gia tin prosthiki neas ypenthymisis
     */
    public void showAddReminderPopup() {
        if (taskManager.getTasks().isEmpty()) {
            showAlert("Error", "No tasks available. Please add a task first.");
            return;
        }
    
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Reminder");
    
        VBox form = new VBox();
        form.setSpacing(10);
    
        ComboBox<Task> taskComboBox = new ComboBox<>();
        taskComboBox.getItems().addAll(taskManager.getTasks());
        taskComboBox.setPromptText("Select Task");
    
        DatePicker reminderDatePicker = new DatePicker();
        reminderDatePicker.setPromptText("Reminder Date");
    
        TextField messageField = new TextField();
        messageField.setPromptText("Reminder Message");
    
        Button saveButton = new Button("Save Reminder");
        saveButton.setOnAction(e -> {
            Task selectedTask = taskComboBox.getValue();
            LocalDate reminderDate = reminderDatePicker.getValue();
            String message = messageField.getText();
    
            if (selectedTask == null || reminderDate == null || message.isEmpty()) {
                showAlert("Error", "All fields must be filled out!");
                return;
            }
    
            try {
                Reminder newReminder = new Reminder(selectedTask, reminderDate, message);
                reminderManager.addReminder(newReminder);
                reminderManager.saveReminders(); // apothikesfi reminder sto json
                showAlert("Success", "Reminder added successfully!");
                popupStage.close();
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });
    
        form.getChildren().addAll(taskComboBox, reminderDatePicker, messageField, saveButton);
        Scene popupScene = new Scene(form, 300, 250);
        popupStage.setScene(popupScene);
        popupStage.show();
    }
    

    /**
     * Emfanizei ena pop-up parathuro gia tin epeksergasia mias ypenthymisis
     */
    public void showEditReminderPopup() {
        if (reminderManager.getReminders().isEmpty()) { // elegxos
            showAlert("Error", "No reminders available to edit.");
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Reminder");

        VBox form = new VBox();
        form.setSpacing(10);

        ComboBox<Reminder> reminderComboBox = new ComboBox<>(); 
        reminderComboBox.getItems().addAll(reminderManager.getReminders());
        reminderComboBox.setPromptText("Select Reminder");

        DatePicker reminderDatePicker = new DatePicker(); 
        TextField messageField = new TextField();

        reminderComboBox.setOnAction(e -> {
            Reminder selectedReminder = reminderComboBox.getValue();
            if (selectedReminder != null) {
                reminderDatePicker.setValue(selectedReminder.getReminderDate());
                messageField.setText(selectedReminder.getMessage());
            }
        });

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            Reminder selectedReminder = reminderComboBox.getValue();
            LocalDate newReminderDate = reminderDatePicker.getValue();
            String newMessage = messageField.getText();

            if (selectedReminder == null || newReminderDate == null || newMessage.isEmpty()) {
                showAlert("Error", "All fields must be filled out!");
                return;
            }

            try {
                selectedReminder.setReminderDate(newReminderDate);
                selectedReminder.setMessage(newMessage);
                showAlert("Success", "Reminder updated successfully!");
                popupStage.close();
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.getChildren().addAll(reminderComboBox, reminderDatePicker, messageField, saveButton);
        Scene popupScene = new Scene(form, 300, 250);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * dhmiourgei ena pop-up parathuro gia tin diagrafi mias ypenthymisis me epivevaiwsi
     */
    public void showDeleteReminderPopup() {
        if (reminderManager.getReminders().isEmpty()) {
            showAlert("Error", "No reminders available to delete.");
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle("Delete Reminder");

        VBox form = new VBox();
        form.setSpacing(10);

        ComboBox<Reminder> reminderComboBox = new ComboBox<>();
        reminderComboBox.getItems().addAll(reminderManager.getReminders());
        reminderComboBox.setPromptText("Select Reminder");

        Button deleteButton = new Button("Delete Reminder");
        deleteButton.setOnAction(e -> {
            Reminder selectedReminder = reminderComboBox.getValue();

            if (selectedReminder == null) {
                showAlert("Error", "Please select a reminder to delete.");
                return;
            }

            // Popup gia epivevaiwsi
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Are you sure you want to delete this reminder?");
            confirmation.setContentText("This action cannot be undone.");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    reminderManager.removeReminder(selectedReminder);
                    showAlert("Success", "Reminder deleted successfully.");
                    popupStage.close();
                }
            });
        });

        form.getChildren().addAll(reminderComboBox, deleteButton);
        Scene popupScene = new Scene(form, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }


    /**
     * emfanizei ena pop-up minima 
     * @param title titlos parathirou
     * @param message minima
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
