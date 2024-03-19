package mealPlanner.io.file;

import mealPlanner.exception.DataExportException;
import mealPlanner.exception.DataImportException;
import mealPlanner.model.CsvConvertible;
import mealPlanner.model.Database;
import mealPlanner.model.Meal;
import mealPlanner.model.Plan;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CsvFileManager implements FileManager {
    private static final String MEAL_DATA = "mealPlannerData.cvs";
    private static final String MEAL_PLAN = "mealPlan.csv";

    @Override
    public void exportData(Database database) {
        exportMeals(database);
        exportPlan(database);
    }

    @Override
    public Database importData() {
        Database database = new Database();
        importMeals(database);
        return database;
    }

    private void exportPlan(Database database) {
        Set<Plan> plans = database.getPlan().entrySet().stream()
                .map(plan -> new Plan(plan.getKey(), plan.getValue()))
                .collect(Collectors.toSet());
        exportToCsv(plans, MEAL_PLAN);
    }

    private void exportMeals(Database database) {
        Set<Meal> meals = database.getMeals().values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        exportToCsv(meals, MEAL_DATA);
    }

    private <T extends CsvConvertible> void exportToCsv(Collection<T> collection, String file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (T element : collection) {
                bufferedWriter.write(element.toCsv());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new DataExportException("Error during saving data to file " + file);
        }
    }

    private void importMeals(Database database) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(MEAL_DATA))) {
            bufferedReader.lines()
                    .map(this::createMealFromString)
                    .forEach(database::addMeal);
        } catch (FileNotFoundException e) {
            throw new DataImportException("File not found " + MEAL_DATA);
        } catch (IOException e) {
            throw new DataImportException("Error during loading file " + MEAL_DATA);
        }
    }

    private Meal createMealFromString(String csvLine) {
        String[] data = csvLine.split(";");
        return new Meal(data[0], data[1], Arrays.asList(data[2].split(",")));
    }

}
