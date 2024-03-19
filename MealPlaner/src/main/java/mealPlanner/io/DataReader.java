package mealPlanner.io;

import mealPlanner.model.Meal;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataReader {
    private Scanner input = new Scanner(System.in);
    private ConsolePrinter printer;

    public DataReader(ConsolePrinter printer) {
        this.printer = printer;
    }

    public void close() {
        input.close();
    }

    public String getString() {
        return input.nextLine();
    }

    public int getInt() {
        try {
            return input.nextInt();
        } finally {
            input.nextLine();
        }
    }


    public Meal readAndCreateMeal(String category) {
        printer.printLine("Input the meal's name:");
        String mealsName = input.nextLine();
        printer.printLine("Input the ingredients: (ing1,ing2,ing3...)");
        String ingredientsInLine = input.nextLine();
        List<String> ingredients = mapIngredientsToList(ingredientsInLine);
        return new Meal(category, mealsName, ingredients);
    }

    private List<String> mapIngredientsToList(String ingredients) {
        return Arrays.asList(ingredients.split(","));
    }


}
