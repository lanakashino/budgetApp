package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {
    private List<Entry> entries;
    private String name;

    // EFFECTS: Creates Category with name of parameter passed with an empty entries
    public Category(String name) {
        entries = new ArrayList<>();
        this.name = name;
    }

    // MODIFIES: this and e
    // EFFECTS: Adds e in entries if e is not already in entries,
    // also makes sure that the category of e is this
    public void add(Entry e) {
        if (!entries.contains(e)) {
            entries.add(e);
            e.addCategory(this);
        }
    }

    // MODIFIES: this and e
    // EFFECTS: Removes e from entries if e is in entries, makes sure that the category of e is null
    public void remove(Entry e) {
        if (entries.contains(e)) {
            entries.remove(e);
            e.removeCategory(this);
        }
    }

    public String getName() {
        return name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    // EFFECTS: returns the active sum of the amounts in entries
    public double getSum() {
        double sum = 0;
        for (Entry e: entries) {
            if (e instanceof BasicEntry) {
                sum += e.getAmount();
            }
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
