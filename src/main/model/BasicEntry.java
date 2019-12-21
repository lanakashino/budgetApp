package model;

import java.time.LocalDate;

public class BasicEntry extends Entry {

    // EFFECTS: constructs an Entry with amount, label, and date
    //          as corresponding input parameters
    public BasicEntry(double amount, String label, LocalDate date) {
        super(amount, label, date);
    }

    // EFFECTS: returns date the entry was made
    @Override
    public LocalDate getDate() {
        return date;
    }
}
