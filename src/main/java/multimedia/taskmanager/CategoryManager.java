package multimedia.taskmanager;

// vivliothiki Gson gia ti diaxeirisi json arxeion
import com.google.gson.Gson; // metatropi java se json kai antistrofa
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader; 
import java.io.FileWriter;
import java.nio.file.Files; 
import java.nio.file.Path;
import java.util.HashSet; // na min exoume diplotipa

/*
 * H CategoryManager diaxeirizetai tis katigories twn tasks
 * Apothiokevei kai fortwnei tis katigories se json arxeio
 */
public class CategoryManager {
    private static final String CATEGORY_FILE = "medialab/categories.json"; // path gia to arxeio json
    private HashSet<String> categories;

    /**
     * Constructor pou arxikopoiei tis katigories kai tis fortwnei apo to json
     */
    public CategoryManager() {
        categories = new HashSet<>();
        loadCategories();
    }

    /**
     * epistrefei oles tis katigories
     * @return HashSet<String> - sinolo categories
     */
    public HashSet<String> getCategories() {
        return categories;
    }

    /**
     * prosthetei nea category
     * @return boolean - true an prosthethei epityxws, false an uparxei hdh
     */
    public boolean addCategory(String category) {
        if (categories.contains(category)) {
            return false;
        }
        categories.add(category);
        return true;
    }

    /**
     * Epeksergazetai mia uparxousa category allazontas to onoma tis
     * @param oldCategory palio onoma tis katigorias
     * @param newCategory neo onoma tis katigorias
     * @return boolean - true egine ontws h allagh, false an i palia den uparxei h i nea uparxei hdh
     */
    public boolean editCategory(String oldCategory, String newCategory) {
        if (!categories.contains(oldCategory) || categories.contains(newCategory)) {
            return false; 
        }
        categories.remove(oldCategory);
        categories.add(newCategory);
        return true;
    }

    /**
     * diagrafei mia katigoria apo ti lista
     * @param category category pros diagrafi
     * @return boolean - true an ontws diagrafike, false den vrethike
     */
    public boolean deleteCategory(String category) {
        return categories.remove(category);
    }

    // apothikefsi twn katigoriwn sto json 
    public void saveCategories() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(CATEGORY_FILE);
            gson.toJson(categories, writer);
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to save categories: " + e.getMessage()); // error handling
        }
    }

    // fortwnei tis katigories apo to json  
    private void loadCategories() {
        try {
            if (Files.exists(Path.of(CATEGORY_FILE))) {
                Gson gson = new Gson();
                FileReader reader = new FileReader(CATEGORY_FILE);
                categories = gson.fromJson(reader, new TypeToken<HashSet<String>>() {}.getType());
                reader.close();
            }
        } catch (Exception e) {
            System.err.println("Failed to load categories: " + e.getMessage()); // error handling
        }
    }
}
