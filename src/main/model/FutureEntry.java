package model;

import java.time.LocalDate;

public class FutureEntry extends Entry {
    private int recurrence; // 1) No, 2) Daily, 3) Weekly, 4) Monthly, 5) Yearly
    private LocalDate endDate;
    private LocalDate nextDate;
    // in this class field date is the starting date of this future entry

    // EFFECTS: constructs an FutureEntry with amount, label, and date
    //          as corresponding input parameters. Starting state of a
    //          FutureEntry has no recurrence.
    public FutureEntry(double amount, String label, LocalDate date) {
        super(amount, label, date);
        recurrence = 1;
        nextDate = date;
        endDate = date;
    }

    // MODIFIES: this
    // EFFECTS: adds recurrence and endDate to this. Throws an
    // InvalidRecurrenceException if recurrence option is invalid.
    public void addRecurrenceBehaviour(int recurrence, LocalDate endDate) throws InvalidRecurrenceException {
        if (recurrence < 1 || recurrence > 5) {
            throw new InvalidRecurrenceException();
        }
        this.recurrence = recurrence;
        this.endDate = endDate;
    }

    // MODIFIES: this
    // EFFECTS: performs the addRecurrenceBehaviour method, but catches the exceptions if thrown.
    //          (mainly made to shorten the lines of the methods which call for addRecurrenceBehaviour)
    //          prints out recurrence and Local Date in all cases.
    public void addRecurrenceWExceptions(int recurrence, LocalDate endDate) {
        try {
            addRecurrenceBehaviour(recurrence, endDate);
        } catch (InvalidRecurrenceException e) {
            System.out.println("Invalid option for recurrence.");
            e.printStackTrace();
        }
    }

    // EFFECTS: returns date the next BasicEntry will be made
    @Override
    public LocalDate getDate() {
        return nextDate;
    }

    // MODIFIES: this
    // EFFECTS: updates nextDate to the appropriate date after its current nextDate
    //          based off of recurrence then returns true.
    //          returns false if there are no more dates to update and you have
    //          reached the end date.
    public boolean updateNextDate() {
        switch (recurrence) {
            case 2:
                nextDate = nextDate.plusDays(1);
                break;
            case 3:
                nextDate = nextDate.plusWeeks(1);
                break;
            case 4:
                nextDate = nextDate.plusMonths(1);
                break;
            case 5:
                nextDate = nextDate.plusYears(1);
                break;
            default:
        }
        return keepRecurrence();
    }

    // EFFECTS: returns false if nextDate is or has past endDate
    //         returns true otherwise.
    public boolean keepRecurrence() {
        if (nextDate.isAfter(endDate) || nextDate.isEqual(endDate)) {
            return false;
        } else {
            return true;
        }
    }

    // EFFECTS: returns recurrence
    public int getRecurrence() {
        return recurrence;
    }

    // EFFECTS: returns endDate
    public LocalDate getEndDate() {
        return endDate;
    }

    // EFFECTS: returns date
    public LocalDate getStartDate() {
        return date;
    }

    // MODIFIES: this
    // EFFECTS: sets nextDate to parameter value
    public void setNextDate(LocalDate nextDate) {
        this.nextDate = nextDate;
    }

}
