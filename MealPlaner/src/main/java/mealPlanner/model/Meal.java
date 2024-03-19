package mealPlanner.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Meal implements CsvConvertible, Serializable {
    private String category;
    private String name;
    private List<String> ingredients;

    public Meal(String category, String name, List<String> ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder result = stringBuilder.append("Category: ").append(category).append(System.lineSeparator())
                .append("Name: ").append(name).append(System.lineSeparator())
                .append("Ingredients:").append(System.lineSeparator());
        for (String ingredient : getIngredients()) {
            result.append(">").append(ingredient).append(System.lineSeparator());
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(name, meal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toCsv() {
        StringBuilder builder = new StringBuilder();
        return builder.append(getCategory()).append(";")
                .append(getName()).append(";")
                .append(ingridientsToString(getIngredients())).append(";").toString();
    }

    public String ingridientsToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String ingredient : list) {
            builder.append(ingredient).append(",");
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

}
