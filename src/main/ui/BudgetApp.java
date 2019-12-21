package ui;

import model.*;
import model.Observer;
import network.ReadWebPage;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.io.IOException;
import java.util.List;

public class BudgetApp extends Subject implements Loadable, Saveable {

    private EntryList entryList;
    private EntryList futureEntryList;
    private Map<String, Category> categories;
    private Scanner scanner;
    private ReadWebPage web;

    // EFFECTS: this is constructed with a empty EntryList and a Scanner
    public BudgetApp() {
        super();
        addObserver(new BalanceMonitor(0.0));
        entryList = new EntryList();
        futureEntryList = new EntryList();
        scanner = new Scanner(System.in);
        categories = new HashMap<>();
        categories.put("Other", new Category("Other"));
        web = new ReadWebPage();

    }

    // MODIFIES: this
    // EFFECTS: greets user with welcome statement and allows for the user to pick
    //          from options to: add a new Entry to this, check the amount sum of this
    //          or exit, which is proceeded by an exit statement.
    public void runApp() throws IOException {
        load();
        System.out.println(" ");
        System.out.println("Welcome to Budget App, here is the Drag Queen Quote of the day:");
        web.printQuote();
        notifyObserver(entryList.sumAmount());
        executeOption();
        System.out.println("Closing Budget App.");
        System.out.println(" ");
        save();
    }

    public void runAppGui() throws IOException {
        load();
        Gui gui = new Gui(this);
        gui.mainPanel();
    }

    public void printHomePage(JPanel panel) throws IOException {
        Label welcome = new Label("Welcome to Budget App, here is the Drag Queen Quote of the day:");
        welcome.setBounds(150, 50, 400, 30);
        panel.add(welcome);
        web.printQuote(panel);
    }

    public void executeOption() {
        loop: while (true) {
            displaySelectionOptions();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    addEntry();
                    break;
                case "2":
                    printFinancialReport();
                    break;
                case "3":
                    break loop;
                default:
                    executeOptionMore();
            }
        }
    }

    // this method is required to adhere to checkstyle rules
    // EFFECTS: prints out test to relay possible input options to user
    public void displaySelectionOptions() {
        displayOp("Please select an option:", "Add new entry", "Print financial report", "Exit app", null, null, true);
    }

    public void executeOptionMore() {
        displayOp("Please select an option:", "Add new Category", "Add new Balance Monitor", null, null, null, true);;
        String option = scanner.nextLine();
        switch (option) {
            case "1":
                addNewCategory();
                break;
            case "2":
                addNewBalanceMonitor();
                break;
            default:
        }
    }

    private void addNewBalanceMonitor() {
        System.out.println("After going past what balance value would you like to be additionally notified?");
        Double threshold = scanner.nextDouble();
        addObserver(new BalanceMonitor(threshold));
    }

    public void addNewBalanceMonitor(String input) {
        addObserver(new BalanceMonitor(Double.parseDouble(input)));
    }

    // EFFECTS: prints total account balance and the break down summary of each individual category
    private void printFinancialReport() {
        System.out.println("Your current balance is: " + entryList.sumAmount() + "$");
        Set<String> keys = categories.keySet();
        for (String key: keys) {
            Category category = categories.get(key);
            System.out.println("    " + key + ": " + category.getSum());
        }
    }

    // EFFECTS: prints total account balance and the break down summary of each individual category
    public void printFinancialReport(JPanel panel) {
        int y = 50;
        Label totalSum = new Label("Your current balance is: " + entryList.sumAmount() + "$");
        totalSum.setBounds(10, y, 300, 30);
        panel.add(totalSum);
        Set<String> keys = categories.keySet();
        for (String key: keys) {
            y += 30;
            Category category = categories.get(key);
            Label label = new Label("    " + key + ": " + category.getSum());
            label.setBounds(10, y, 130, 30);
            panel.add(label);
        }
    }

    private void addNewCategory() {
        System.out.println("Name of the new Category:");
        String key = scanner.nextLine();
        categories.put(key, new Category(key));
    }

    public void addNewCategory(String input) {
        categories.put(input, new Category(input));
    }

    // MODIFIES: this
    // EFFECTS: prints welcome statement and loads and updates previous data
    public void load() {
        try {
            List<String> keyList = entryList.load("data.txt");
            List<String> keyListFuture = futureEntryList.load("futureData.txt");
            setUpCategories(keyList, keyListFuture);
            load("observers.txt");
        } catch (IOException e) {
            System.out.println("Start up failed: File not found");
            System.out.println();
        }
        updateLists();
        printComplete("load");
    }

    public List<String> load(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("data/" + fileName));
        for (String line: lines) {
            Double threshold = Double.parseDouble(line);
            if (!observers.contains(new BalanceMonitor(threshold))) {
                observers.add(new BalanceMonitor(threshold));
            }
        }
        return null;
    }


    public void printComplete(String s) {
        System.out.println("-------" + s + " complete-------");
    }

    // MODIFIES: this
    // EFFECTS: adds unique categories to map with its name as a key.
    // sets categories for each Entry in futureEntryList and entryList
    // where futureKeys, and keys are lists of names of Categories that
    // correspond to the EntryLists respectively
    public void setUpCategories(List<String> keys, List<String> futureKeys) {
        Set<String> uniqueKeys = new HashSet<>(keys);
        uniqueKeys.addAll(futureKeys);
        for (String key: uniqueKeys) {
            categories.put(key, new Category(key));
        }
        int i = 0;
        for (String key: keys) {
            Entry e = entryList.getElement(i);
            e.setCategory(categories.get(key));
            i++;
        }
        i = 0;
        for (String key: futureKeys) {
            Entry e = futureEntryList.getElement(i);
            e.setCategory(categories.get(key));
            i++;
        }
    }

    // MODIFIES: this
    // EFFECTS: checks if any FutureEntries in futureEntry list has nextDay = today
    //          or has already past.
    //          if so, adds a BasicEntry that corresponds to the FutureEntry to entryList,
    //          updates FutureEntry's nextDay. If false is returned in this attempt, it removes
    //          that FutureEntry from futureEntryList.
    public void updateLists() {
        EntryList removeFE = new EntryList();
        for (int i = 0; i < futureEntryList.size(); i++) {
            FutureEntry fe = (FutureEntry) futureEntryList.getElement(i);
            LocalDate nextDate = fe.getDate();
            if (nextDate.isEqual(LocalDate.now()) || nextDate.isBefore(LocalDate.now())) {
                addBasicEntry(fe.getAmount(), fe.getLabel(), fe.getDate(), fe.getCategory());
                if (!fe.updateNextDate()) {
                    removeFE.add(fe);
                    fe.removeCategory(fe.getCategory());
                }
            }
        }
        try {
            futureEntryList.remove(removeFE);
        } catch (InvalidEntryException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: prints exit statement and saves app data
    public void save() {
        Saveable savableEntryList = entryList;
        try {
            savableEntryList.save("data.txt");
            futureEntryList.save("futureData.txt");
            save("observers.txt");
        } catch (IOException e) {
            System.out.println("Saving failed: File not found");
            System.out.println();
        }
        printComplete("save");
    }

    public void save(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("data/" + fileName,"UTF-8");
        for (Observer observer: observers) {
            BalanceMonitor bm = (BalanceMonitor) observer;
            String line = Double.toString(bm.getThreshold());
            writer.println(line);
        }
        writer.close();
    }

    // REQUIRES: user input is specified Type
    //          ( Boolean for "Expense? ..."
    //            double for "Entry amount ..."
    //            String for "Add a label ..." )
    // MODIFIES: this
    // EFFECTS: takes user input with specified parameters and adds new entry into this
    public void addEntry() {
        Boolean today = entryTypeOptionSelection();
        System.out.println("Expense? (True or False)");
        boolean expense = scanner.nextBoolean();
        System.out.println("Entry amount is $ _______");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Add a note");
        String label = scanner.nextLine();
        Category category = chooseCategory();
        amount = computeValue(amount, expense);
        if (today) {
            addBasicEntry(amount, label, LocalDate.now(), category);
        } else {
            addFutureEntry(amount, label, category);
        }
        notifyObserver(entryList.sumAmount());
    }

    // EFFECTS: prompts user to choose an existing Category and returns category
    private Category chooseCategory() {
        System.out.println("Choose Category");
        System.out.println(categories.keySet());
        String key = scanner.nextLine();
        Category category = categories.get(key);
        return category;
    }

    // MODIFIES: this
    // EFFECTS: adds a BasicEntry to entryList
    public void addBasicEntry(BasicEntry e) {
        entryList.add(e);
    }

    // MODIFIES: this
    // EFFECTS: adds new entry with amount, label, and date in entryList
    public void addBasicEntry(Double amount, String label, LocalDate date, Category category) {
        Entry e = new BasicEntry(amount, label, date);
        e.setCategory(category);
        entryList.add(e);
        System.out.println("Entry of " + e.getAmount() + "$ made");
    }

    // MODIFIES: this
    // EFFECTS: adds new entry with amount, label, and date in entryList
    public void addBasicEntry(Double amount, String label, LocalDate date, String key) {
        Category category = categories.get(key);
        Entry e = new BasicEntry(amount, label, date);
        e.setCategory(category);
        entryList.add(e);
    }


    // REQUIRES: user input is specified Type and format
    // MODIFIES: this
    // EFFECTS: adds a new FutureEntry to futureEntryList with amount and label.
    //          endDate, nextDate, date, and recurrence is determined and set through user input.
    public void addFutureEntry(Double amount, String label, Category category) {
        System.out.println("Date in YYYY-MM-DD");
        String date = scanner.nextLine();
        FutureEntry e = new FutureEntry(amount, label, LocalDate.parse(date));
        e.setCategory(category);
        displayOp("Repeat entry?", "No", "Daily", "Weekly", "Monthly", "Yearly", false);
        int recurrence = scanner.nextInt();
        if (recurrence != 1) {
            scanner.nextLine();
            System.out.println("repeats until the date of: YYYY-MM-DD");
            String endDate = scanner.nextLine();
            e.addRecurrenceWExceptions(recurrence, LocalDate.parse(endDate));
        } else {
            scanner.nextLine();
        }
        futureEntryList.add(e);
        System.out.println("Entry of " + e.getAmount() + "$ made for " + e.getDate());
    }

    public void addFutureEntry(Double amount, String label, String category, int recurrence,Date date, Date endDate) {
        FutureEntry e = new FutureEntry(amount, label, convertToLocalDateViaInstant(date));
        e.setCategory(categories.get(category));
        if (recurrence != 1) {
            e.addRecurrenceWExceptions(recurrence, convertToLocalDateViaInstant(endDate));
        }
        futureEntryList.add(e);
    }

    // MODIFIES: this
    // EFFECTS: adds a FutureEntry to futureEntryList
    public void addFutureEntry(FutureEntry f) {
        futureEntryList.add(f);
    }

    // Taken from https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // EFFECTS: returns -amount if an expense and +amount if not an expense
    public double computeValue(double amount, boolean expense) {
        if (expense) {
            return -amount;
        } else {
            return amount;
        }
    }

    // REQUIRES: user input is 1 or 2
    // EFFECTS: prints out input options for user to decide whether to make
    //          an Entry for today or not.
    public boolean entryTypeOptionSelection() {
        displayOp("Entry For Today?", "Yes", "No", null, null, null, false);
        int option = scanner.nextInt();
        if (option == 1) {
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: prints question and options in correct format. if an option is null, does not print that option
    // if moreOp is true then prints a statement saying press any key for more options
    public void displayOp(String question, String op1, String op2, String op3, String op4, String op5, boolean moreOp) {
        System.out.println();
        System.out.println(question);
        printValidOption("1", op1);
        printValidOption("2", op2);
        printValidOption("3", op3);
        printValidOption("4", op4);
        printValidOption("5", op5);
        if (moreOp) {
            printValidOption("*", "Press any other key for more options");
        }
    }

    // EFFECTS: Prints option in the correct format if option is not null
    public void printValidOption(String i, String option) {
        if (option != null) {
            System.out.println("    " + i + ") " + option);
        }
    }

    // EFFECTS: returns entryList
    public EntryList getEntryList() {
        return entryList;
    }

    // EFFECTS: returns futureEntryList
    public EntryList getFutureEntryList() {
        return futureEntryList;
    }

    public Map<String, Category> getCategories() {
        return  categories;
    }
}


