package multimedia.taskmanager; 

import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.stage.Stage;

/**
 * H klasi PriorityController diaxeirizetai to UI twn proraitotitwn
 * perilamvanei methodous gia prosthiki, diagrafi kai epeksergasia proraitotitas
 */
public class PriorityController {
    private PriorityManager priorityManager; // anafora ston PriorityManager gia diaxeirisi twn proraitotitwn

    /**
     * constructor pou arxikopoiei ton PriorityManager
     * @param priorityManager to antikeimeno pou diaxeirizetai tis proraitotites
     */
    public PriorityController(PriorityManager priorityManager) {
        this.priorityManager = priorityManager;
    }

    /**
     * popup gia prosthiki neas proraitotitas
     */
    public void showAddPriorityPopup() {
        Stage popupStage = new Stage(); // new window
        popupStage.setTitle("Add Priority"); 

        VBox form = new VBox(); // katheti diataksi stoixeiwn
        form.setSpacing(10);

        TextField priorityField = new TextField(); 
        priorityField.setPromptText("Priority Name");

        Button saveButton = new Button("Save Priority");
        saveButton.setOnAction(e -> { // orismos event afou patithei to koumpi
            String priority = priorityField.getText();
            if (priority.isEmpty()) { 
                showAlert("Error", "Priority name cannot be empty!"); // error message
                return;
            }
            if (priorityManager.addPriority(priority)) { // prosthiki proraitotitas meso tou PriorityManager
                showAlert("Success", "Priority added successfully!");
            } else {
                showAlert("Error", "Priority already exists or invalid!"); // error handling
            }
            popupStage.close();
        });

        form.getChildren().addAll(priorityField, saveButton); 
        Scene popupScene = new Scene(form, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
     * popup gia epeksergasia proraitotitas
     */
    public void showEditPriorityPopup() {
        if (priorityManager.getPriorities().isEmpty()) { // elegxos an uparxoun proraitotites
            showAlert("Error", "No priorities available to edit.");
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Priority");

        VBox form = new VBox(); // VBox gia katheti diataksi
        form.setSpacing(10);

        ComboBox<String> priorityDropdown = new ComboBox<>(); 
        priorityDropdown.getItems().addAll(priorityManager.getPriorities()); 

        TextField newPriorityField = new TextField();
        newPriorityField.setPromptText("New Priority Name");

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            String selectedPriority = priorityDropdown.getValue(); 
            String newPriority = newPriorityField.getText(); 

            if (selectedPriority == null || newPriority.isEmpty()) { 
                showAlert("Error", "Please select a priority and enter a new name.");
                return;
            }

            if (priorityManager.editPriority(selectedPriority, newPriority)) {
                showAlert("Success", "Priority edited successfully!");
            } else {
                showAlert("Error", "Priority edit failed!");
            }
            popupStage.close();
        });

        form.getChildren().addAll(priorityDropdown, newPriorityField, saveButton);
        Scene popupScene = new Scene(form, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
    * popup gia diagrafi proraitotitas
    */
    public void showDeletePriorityPopup() {
        if (priorityManager.getPriorities().isEmpty()) {
            showAlert("Error", "No priorities available to delete.");
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle("Delete Priority");

        VBox form = new VBox();
        form.setSpacing(10);

        ComboBox<String> priorityDropdown = new ComboBox<>();
        priorityDropdown.getItems().addAll(priorityManager.getPriorities());
        priorityDropdown.setPromptText("Select Priority");

        Button deleteButton = new Button("Delete Priority");
        deleteButton.setOnAction(e -> {
            String selectedPriority = priorityDropdown.getValue();

            if (selectedPriority == null) {
                showAlert("Error", "Please select a priority to delete.");
                return;
            }

            // Popup gia confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Are you sure you want to delete this priority?");
            confirmation.setContentText("Tasks with this priority will be assigned the default priority.");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    priorityManager.deletePriority(selectedPriority);
                    showAlert("Success", "Priority deleted successfully.");
                    popupStage.close();
                }
            });
        });

        form.getChildren().addAll(priorityDropdown, deleteButton);
        Scene popupScene = new Scene(form, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }


    /**
     * Emfanizei ena pop-up minima (alert) me titlo kai minima pou tou dinetai.
     * @param title titlos parathirou notification
     * @param message minima poun tha emfanistei
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
