package mealPlanner.io.file;

import mealPlanner.exception.DataExportException;
import mealPlanner.exception.DataImportException;
import mealPlanner.model.Database;

import java.io.*;

public class SerializableFileManager implements FileManager{
    private static final String MEAL_DATA = "mealPlanner.o";
    @Override
    public Database importData() {
        try (FileInputStream fis = new FileInputStream(MEAL_DATA);
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (Database) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new DataImportException("File not found " + MEAL_DATA);
        } catch (IOException e) {
            throw new DataImportException("Error during loading file " + MEAL_DATA);
        } catch (ClassNotFoundException e) {
            throw new DataImportException("Incorrect data type in file " + MEAL_DATA);
        }
    }

    @Override
    public void exportData(Database dataBase) {
        try (FileOutputStream fos = new FileOutputStream(MEAL_DATA);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(dataBase);
        } catch (FileNotFoundException e) {
            throw new DataExportException("File not found " + MEAL_DATA);
        } catch (IOException e) {
            throw new DataExportException("Error during saving data to file " + MEAL_DATA);
        }
    }
}
