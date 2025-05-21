package multimedia.taskmanager;

import javafx.scene.control.*;
import javafx.stage.Stage; // gia ta popups
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

/**
 * me tin class CategoryController dixeirizomaste to UI ton categories
 * Exoume methodous gia na prosthetei, na diagrafei kai na allazei categories
 */
public class CategoryController {
    private CategoryManager categoryManager;

    /**
     * constructor pou arxikopoiei tin CategoryManager.
     * @param categoryManager gia diaxeirisi ton categories
     */
    public CategoryController(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    // pop-up gia prosthiki neas category
    public void showAddCategoryPopup() {
        Stage popupStage = new Stage(); 
        popupStage.setTitle("Add Category");

        VBox form = new VBox(); 
        form.setSpacing(10);

        TextField categoryField = new TextField(); 
        categoryField.setPromptText("Category Name"); 

        Button saveButton = new Button("Save Category"); 
        saveButton.setOnAction(e -> {
            String category = categoryField.getText(); 
            if (category.isEmpty()) { // amintikos programmatismos
                showAlert("Error", "Category name cannot be empty!");
                return;
            }
            if (categoryManager.addCategory(category)) { 
                showAlert("Success", "Category added successfully!");
            } else {
                showAlert("Error", "Category already exists!");
            }
            popupStage.close();
        });

        form.getChildren().addAll(categoryField, saveButton); 
        Scene popupScene = new Scene(form, 300, 200); 
        popupStage.setScene(popupScene); 
        popupStage.show();
    }

    /**
     * pop-up gia epeksergasia se mia katigoria
     */
    public void showEditCategoryPopup() {
        if (categoryManager.getCategories().isEmpty()) { // amintikos programmatismos
            showAlert("Error", "No categories available to edit."); 
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Category");

        VBox form = new VBox();
        form.setSpacing(10);

        ComboBox<String> categoryDropdown = new ComboBox<>(); // Drop-down list me tis kathgories
        categoryDropdown.getItems().addAll(categoryManager.getCategories());

        TextField newCategoryField = new TextField();
        newCategoryField.setPromptText("New Category Name");

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            String selectedCategory = categoryDropdown.getValue();
            String newCategory = newCategoryField.getText(); 

            if (selectedCategory == null || newCategory.isEmpty()) {
                showAlert("Error", "Please select a category and enter a new name.");
                return;
            }

            if (categoryManager.editCategory(selectedCategory, newCategory)) { 
                showAlert("Success", "Category edited successfully!");
            } else {
                showAlert("Error", "Category edit failed!");
            }
            popupStage.close();
        });

        form.getChildren().addAll(categoryDropdown, newCategoryField, saveButton);
        Scene popupScene = new Scene(form, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    /**
    * popup gia diagrafi katigorias
    */
    public void showDeleteCategoryPopup() {
        if (categoryManager.getCategories().isEmpty()) {
            showAlert("Error", "No categories available to delete.");
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle("Delete Category");

        VBox form = new VBox();
        form.setSpacing(10);

        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll(categoryManager.getCategories());
        categoryDropdown.setPromptText("Select Category");

        Button deleteButton = new Button("Delete Category");
        deleteButton.setOnAction(e -> {
            String selectedCategory = categoryDropdown.getValue();

            if (selectedCategory == null) {
                showAlert("Error", "Please select a category to delete.");
                return;
            }

            // Popup gia confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Are you sure you want to delete this category?");
            confirmation.setContentText("All tasks in this category will also be deleted.");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    categoryManager.deleteCategory(selectedCategory);
                    showAlert("Success", "Category deleted successfully.");
                    popupStage.close();
                }
            });
        });

        form.getChildren().addAll(categoryDropdown, deleteButton);
        Scene popupScene = new Scene(form, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }


    /**
     * popup gia eidopoiisi 
     * @param title gia titlo parathirou
     * @param message to minima pou tha emfanistei
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // eidopoiisi typou "Information"
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait(); // emfanisi tou alert kai anamoni gia kleisimo
    }
}
