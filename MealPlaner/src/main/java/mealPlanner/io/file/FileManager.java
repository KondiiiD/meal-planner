package mealPlanner.io.file;

import mealPlanner.model.Database;

public interface FileManager {
    Database importData();
    void exportData(Database dataBase);
}
