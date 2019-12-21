package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

// Represents a list of entries
public class EntryList implements Loadable, Saveable {
    private List<Entry> data;

    // EFFECT: constructs an EntryList that is empty
    public EntryList() {
        data = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds e to this
    public void add(Entry e) {
        data.add(e);
    }

    // MODIFIES: this
    // EFFECTS: removes e from this, if e is not in this, throws and InvalidEntryException
    public void remove(Entry e) throws InvalidEntryException {
        if (!this.contains(e)) {
            throw new InvalidEntryException();
        }
        data.remove(e);
    }

    // MODIFIES: this
    // EFFECTS: removes Entries in this that are listed in removables,
    // if any Entry is not in this, throws and InvalidEntryException
    public void remove(EntryList removables) throws  InvalidEntryException {
        int i = 0;
        Entry e;
        while (i < removables.size()) {
            e = removables.getElement(i);
            remove(e);
            i += 1;
        }
    }

    // EFFECTS: returns the amount sum of the entries in this
    public double sumAmount() {
        double total = 0;
        for (Entry e: data) {
            total = total + e.getAmount();
        }
        return total;
    }

    // EFFECTS: returns this size
    public int size() {
        return data.size();
    }

    // EFFECT: returns true if e is in this
    public boolean contains(Entry e) {
        return data.contains(e);
    }

    // EFFECT: returns ith element of data
    public Entry getElement(int i) {
        return data.get(i);
    }

    // REQUIRES: a text file with the name data.txt and entries in each line
    //          in the following format,
    //          amount label date key
    // MODIFIES: this
    // EFFECTS: Loads previous BudgetApp data onto entryList
    @Override
    public List<String> load(String fileName) throws IOException {
        List<String> keyList = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("data/" + fileName));
        for (String line: lines) {
            ArrayList<String> partsOfLine = splitOnSpace(line);
            if (partsOfLine.size() == 4) {
                LocalDate date = LocalDate.parse(partsOfLine.get(2));
                this.add(new BasicEntry(Double.parseDouble(partsOfLine.get(0)), partsOfLine.get(1), date));
                keyList.add(partsOfLine.get(3));
            } else {
                LocalDate date = LocalDate.parse(partsOfLine.get(2));
                FutureEntry fe = new FutureEntry(Double.parseDouble(partsOfLine.get(0)), partsOfLine.get(1), date);
                fe.addRecurrenceWExceptions(Integer.parseInt(partsOfLine.get(3)), LocalDate.parse(partsOfLine.get(4)));
                fe.setNextDate(LocalDate.parse(partsOfLine.get(5)));
                this.add(fe);
                keyList.add(partsOfLine.get(6));
            }
        }
        return keyList;
    }

    // EFFECTS: splits line on spaces. returns the separated line as a list of strings
    public static ArrayList<String> splitOnSpace(String line) {
        String[] splits = line.split(" ");
        return new ArrayList<>(Arrays.asList(splits));
    }

    // MODIFIES: data.txt file
    // EFFECTS: Saves BudgetApp entryList to a text file named data.txt
    //          such that each line is a entryList, formatted as follows,
    //          amount label date key
    @Override
    public void save(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("data/" + fileName,"UTF-8");
        for (Entry entry: data) {
            Category category = entry.getCategory();
            if (entry instanceof BasicEntry) {
                String line = Double.toString(entry.getAmount()) + " " + entry.getLabel() + " " + entry.getDate();
                writer.println(line + " " + category.getName());
            } else {
                FutureEntry futureEntry = (FutureEntry) entry;
                String line1 = Double.toString(futureEntry.getAmount()) + " " + futureEntry.getLabel() + " ";
                String line2 = futureEntry.getStartDate() + " " + Integer.toString(futureEntry.getRecurrence()) + " ";
                String line3 = futureEntry.getEndDate() + " " + futureEntry.getDate();
                writer.println(line1 + line2 + line3 + " " + category.getName());
            }
        }
        writer.close();
    }


    // TODO: make a method to shift through the EntryList

    //    // EFFECTS: returns a new EntryList with Entries from that Month
//    public EntryList subsetMonth(Month month, Year year) {
//        return new EntryList();
//        //return empty of none found
//    }
//
//    // EFFECTS: returns a new EntryList with Entries with label
//    public EntryList subsetLabel(String label) {
//        return new EntryList();
//    }

}
