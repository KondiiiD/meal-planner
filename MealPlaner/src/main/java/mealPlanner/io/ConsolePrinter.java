package mealPlanner.io;

import mealPlanner.model.Meal;

import java.util.*;

public class ConsolePrinter {

    public void printPlan(Map<String, Set<Meal>> plan) {
        long count = plan.keySet()
                .size();
        if (count == 0)
            printLine("Nothing added to plan yet");
        else {
            printLine("Your plan:");
            for (Map.Entry<String, Set<Meal>> stringListEntry : plan.entrySet()) {
                printLine(stringListEntry.getKey());
                stringListEntry.getValue().forEach(meal -> printLine(meal.toString()));
            }
        }
    }

    public void printMealNamesByCategory(String category, Map<String, Set<Meal>> meals) {
        long count = meals.get(category).size();
        if (count == 0)
            printLine("No meals in requested category.");
        else
            meals.get(category).stream()
                    .map(Meal::getName)
                    .forEach(System.out::println);
    }

    public void printMealsByCategory(String category, Map<String, Set<Meal>> meals) {
        long count = meals.get(category).size();
        if (count == 0)
            printLine("No meals in requested category.");
        else
            meals.get(category).forEach(System.out::println);
    }

    public void printMeals(Map<String, Set<Meal>> meals) {
        long count = meals.values()
                .stream()
                .mapToLong(Collection::size)
                .sum();
        if (count == 0)
            printLine("No meals saved. Add a meal first.");
        else
            meals.values().stream()
                    .flatMap(Collection::stream)
                    .forEach(meal -> System.out.println(meal.toString()));
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
