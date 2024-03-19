package mealPlanner.model;

import mealPlanner.exception.NoSuchMealException;

import java.io.Serializable;
import java.util.*;

public class Database implements Serializable {

    private final Map<String, Set<Meal>> meals = new HashMap<>();
    private final Map<String, Set<Meal>> plans = new HashMap<>();

    public void removeMeal(String category, String mealName) {
        Set<Meal> categoryMeals = meals.get(category);
        boolean foundMeal = false;
        Iterator<Meal> mealIterator = categoryMeals.iterator();
        while (mealIterator.hasNext()) {
            Meal meal = mealIterator.next();
            if (meal.getName().equals(mealName)) {
                mealIterator.remove();
                foundMeal = true;
            }
        }
        if (!foundMeal) {
            throw new NoSuchMealException();
        }
            meals.put(category, categoryMeals);
    }

    public void addMeal(Meal meal) {
        if (!meals.containsKey(meal.getCategory())) {
            HashSet<Meal> mealHashSet = new HashSet<>();
            mealHashSet.add(meal);
            meals.put(meal.getCategory(), mealHashSet);
        } else {
            Set<Meal> categoryMeals = meals.get(meal.getCategory());
            categoryMeals.add(meal);
            meals.put(meal.getCategory(), categoryMeals);
        }
    }

    public void inputMealPlan(String day, Meal meal) {
        if (!plans.containsKey(day)) {
            Set<Meal> mealPlan = new HashSet<>();
            mealPlan.add(meal);
            plans.put(day, mealPlan);
        } else {
            Set<Meal> mealPlan = plans.get(day);
            mealPlan.add(meal);
            plans.put(day, mealPlan);
        }
    }

    public Map<String, Set<Meal>> getPlan() {
        return plans;
    }

    public Map<String, Set<Meal>> getMeals() {
        return meals;
    }

    public Optional<Meal> getMealByName(String mealName) {
        return meals.values().stream()
                .flatMap(Collection::stream)
                .filter(m -> m.getName().equals(mealName))
                .findAny();
    }
}
