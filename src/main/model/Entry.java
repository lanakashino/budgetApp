package model;

import java.time.LocalDate;

// Represents a expense/income entry in a budgeting app
public abstract class Entry {

    private double amount;
    private String label;
    protected LocalDate date;
    private Category category;

    // EFFECTS: constructs an Entry with amount, label, and date
    //          as corresponding input parameters
    public Entry(double amount, String label, LocalDate date) {
        this.amount = amount;
        this.label = label;
        this.date = date;
        this.category = null;
    }

    // EFFECTS: returns amount on this
    public double getAmount() {
        return amount;
    }

    // EFFECTS: returns label associated with this
    public String getLabel() {
        return label;
    }

    // EFFECTS: returns date the entry was made
    public abstract LocalDate getDate();

    public Category getCategory() {
        return category;
    }

    // MODIFIES: this and category
    // EFFECTS: changes this category into the parameter category
    // ensures that this is removed from precious category and is in new category
    public void setCategory(Category category) {
        removeCategory(this.category);
        addCategory(category);
    }

    // MODIFIES: this and category
    // EFFECTS: if this category is null then changes this category to the parameter category
    // ensures that this is added to category.
    public void addCategory(Category category) {
        if (this.category == null) {
            this.category = category;
            category.add(this);
        }
    }

    // MODIFIES: this and category
    // EFFECTS: if this category is the parameter category, changes this category to null
    // ensures that this is removed from category.
    public void removeCategory(Category category) {
        if (this.category != null) {
            if (this.category == category) {
                this.category = null;
                category.remove(this);
            }
        }
    }

    // TODO: edit amount, label, date methods

}
