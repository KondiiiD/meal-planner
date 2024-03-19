package mealPlanner;

public class MealPlannerApp {
    public static void main(String[] args) {
        MealController mealController = new MealController();
        mealController.controlLoop();
    }
}