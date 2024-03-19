package mealPlanner;

import mealPlanner.exception.*;
import mealPlanner.io.ConsolePrinter;
import mealPlanner.io.DataReader;
import mealPlanner.io.file.FileManager;
import mealPlanner.io.file.FileManagerStarter;
import mealPlanner.model.Database;
import mealPlanner.model.Meal;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;

class MealController {
    private final ConsolePrinter consolePrinter = new ConsolePrinter();
    private final DataReader dataReader = new DataReader(consolePrinter);
    private FileManager fileManager;
    private Database database;

    MealController() {
        fileManager = new FileManagerStarter(consolePrinter, dataReader).start();
        try {
            database = fileManager.importData();
            consolePrinter.printLine("Data imported to file");
        } catch (DataImportException | InvalidDataException e) {
            consolePrinter.printLine(e.getMessage());
            consolePrinter.printLine("New database initiated");
            database = new Database();
        }
    }

    void controlLoop() {
        Option option;
        do {
            option = chooseOption();
            executeOption(option);
        } while (option != Option.EXIT);
    }

    private void executeOption(Option option) {
        switch (option) {
            case ADD_MEAL -> addMeal();
            case SHOW_MEALS -> showMeals();
            case SHOW_MEALS_BY_CATEGORY -> showMealsByCategory();
            case REMOVE_MEAL -> removeMeal();
            case PLAN -> planWeek();
            case PRINT_PLAN -> showPlan();
            case EXIT -> exitProgram();
        }
    }

    void addMeal() {
        try {
            consolePrinter.printLine("Which meal do you want to add (breakfast, lunch, dinner)?");
            String category = chooseCategory().getDescription();
            Meal meal = dataReader.readAndCreateMeal(category);
            database.addMeal(meal);
            consolePrinter.printLine(meal.toString());
            consolePrinter.printLine("The meal has been added!");
        } catch (InputMismatchException e) {
            consolePrinter.printLine("Unable to create meal, invalid data provided");
        }
    }

    private void planWeek() {
        Day chosenDay;
        do {
            chosenDay = chooseDay();
            if (chosenDay != Day.EXIT)
                addMealToPlan(chosenDay);
            else
                consolePrinter.printLine("Plan created successfully");
        } while (chosenDay != Day.EXIT);
    }

    private void addMealToPlan(Day chosenDay) {
        String day = chosenDay.getDescription();
        try {
            printMealNamesFromSpecificCategory();
            Optional<Meal> mealByName = database.getMealByName(dataReader.getString());
            if (mealByName.isEmpty()) {
                throw new NoSuchElementException();
            }
            database.inputMealPlan(day, mealByName.get());
            consolePrinter.printLine("Meal added successfully!");
        } catch (NoSuchElementException e) {
            consolePrinter.printLine("Unable to find meal");
        }
    }

    private void printMealNamesFromSpecificCategory() {
        consolePrinter.printLine("Which category are you interested in? (breakfast, lunch, dinner)");
        String mealCategory = chooseCategory().description;
        consolePrinter.printLine("Type in meal from below, which you wanna add to your plan");
        consolePrinter.printMealNamesByCategory(mealCategory, database.getMeals());
    }

    private void showPlan() {
        consolePrinter.printPlan(database.getPlan());
    }

    private void showMeals() {
        consolePrinter.printMeals(database.getMeals());
    }

    private void showMealsByCategory() {
        consolePrinter.printLine("Meals of what category you're looking for? (breakfast, lunch, dinner)");
        String category = chooseCategory().getDescription();
        consolePrinter.printMealsByCategory(category, database.getMeals());
    }

    private void removeMeal() {
        try {
            consolePrinter.printLine("Provide category");
            String category = chooseCategory().getDescription();
            consolePrinter.printLine("Provide meal name to remove");
            String mealName = dataReader.getString();
            database.removeMeal(category, mealName);
            consolePrinter.printLine(String.format("%s has been removed successfully!", mealName));
        } catch (NoSuchMealException e) {
            consolePrinter.printLine("Meal with provided name doesn't exist");
        }
    }

    private void exitProgram() {
        try {
            fileManager.exportData(database);
            consolePrinter.printLine("Data exported successfully");
        } catch (DataExportException e) {
            consolePrinter.printLine(e.getMessage());
        }
        dataReader.close();
        consolePrinter.printLine("Program closed properly, bye!");
    }

    private void printOptions() {
        Arrays.stream(Option.values()).forEach(System.out::println);
    }

    private void printDayOptions() {
        Arrays.stream(Day.values()).forEach(System.out::println);
    }

    private Day chooseDay() {
        Optional<Day> option = Optional.empty();
        do {
            try {
                consolePrinter.printLine("Choose day of the week");
                printDayOptions();
                option = Day.createFromInt(dataReader.getInt());
            } catch (InputMismatchException e) {
                consolePrinter.printLine("Provided incorrect format type, provide number");
            } catch (NoSuchOptionException e) {
                consolePrinter.printLine(e.getMessage() + ", try again:");
            }
        } while (option.isEmpty());
        return option.get();
    }

    private Option chooseOption() {
        Optional<Option> option = Optional.empty();
        do {
            try {
                consolePrinter.printLine("What would you like to do:");
                printOptions();
                option = Option.createFromInt(dataReader.getInt());
            } catch (InputMismatchException e) {
                consolePrinter.printLine("Provided incorrect format type, provide number");
            } catch (NoSuchOptionException e) {
                consolePrinter.printLine(e.getMessage() + ", try again:");
            }
        } while (option.isEmpty());
        return option.get();
    }

    private Category chooseCategory() {
        Optional<Category> category = Optional.empty();
        do {
            try {
                category = Category.createFromString(dataReader.getString());
            } catch (NoSuchOptionException e) {
                consolePrinter.printLine(e.getMessage());
            }
        } while (category.isEmpty());
        return category.get();
    }

    private enum Day {
        MONDAY(1, "Monday"),
        TUESDAY(2, "Tuesday"),
        WEDNESDAY(3, "Wednesday"),
        THURSDAY(4, "Thursday"),
        FRIDAY(5, "Friday"),
        SATURDAY(6, "Saturday"),
        SUNDAY(7, "Sunday"),
        EXIT(8,"Finish plan");

        private final int value;
        private final String description;

        Day(int value, String description) {
            this.value = value;
            this.description = description;
        }


        public String getDescription() {
            return description;
        }

        static Optional<Day> createFromInt(int option) {
            try {
                Day[] values = Day.values();
                for (Day day : values) {
                    if (day.value == option)
                        return Optional.of(day);
                }
                throw new NoSuchElementException();
            } catch (NoSuchElementException e) {
                throw new NoSuchOptionException("There is no option " + option);
            }
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }
    }

    private enum Category {
        BREAKFAST("breakfast"),
        LUNCH("lunch"),
        DINNER("dinner");

        private final String description;

        public String getDescription() {
            return description;
        }

        Category(String description) {
            this.description = description;
        }

        static Optional<Category> createFromString(String request) throws NoSuchOptionException {
            try {
                Category[] values = Category.values();
                for (Category value : values) {
                    if (value.description.equals(request.toLowerCase()))
                        return Optional.of(value);
                }
                throw new InputMismatchException();
            } catch (InputMismatchException e) {
                throw new NoSuchOptionException("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }
    }

    enum Option {
        ADD_MEAL(0, "Add meal"),
        SHOW_MEALS(1, "Show all meals"),
        SHOW_MEALS_BY_CATEGORY(2, "Show meals of requested category:"),
        REMOVE_MEAL(3,"Remove meal"),
        PLAN(4,"Plan your week"),
        PRINT_PLAN(5, "Show plan"),
        EXIT(6, "Exit");

        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        static Optional<Option> createFromInt(int option) {
            try {
                Option[] values = Option.values();
                for (Option property : values) {
                    if (property.value == option)
                        return Optional.of(property);
                }
                throw new NoSuchElementException();
            } catch (NoSuchElementException e) {
                throw new NoSuchOptionException("There is no option " + option);
            }
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }
    }

}