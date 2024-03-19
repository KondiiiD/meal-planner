package mealPlanner.model;

import java.io.Serializable;
import java.util.Set;

public class Plan implements CsvConvertible, Serializable {
    private String day;
    private Set<Meal> meals;

    public Plan(String day, Set<Meal> meals) {
        this.day = day;
        this.meals = meals;
    }

    @Override
    public String toCsv() {
        StringBuilder builder = new StringBuilder();
        for (Meal meal : meals) {
            builder.append(day).append(";")
                    .append(meal.getCategory()).append(";")
                    .append(meal.getName()).append(";")
                    .append(meal.ingridientsToString(meal.getIngredients())).append(";").append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(">").append(day.toUpperCase()).append(":").append(System.lineSeparator());
        for (Meal meal : meals) {
            stringBuilder.append("  - Category: ").append(meal.getCategory()).append(System.lineSeparator())
                    .append("    Name: ").append(meal.getName()).append(System.lineSeparator())
                    .append("    Ingredients: ").append(meal.ingridientsToString(meal.getIngredients())).append(System.lineSeparator());
        }
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }
}
