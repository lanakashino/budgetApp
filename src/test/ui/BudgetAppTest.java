package ui;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BudgetAppTest extends SubjectTest{
    BudgetApp ba;

    @BeforeEach
    public void runBefore() {
        ba = new BudgetApp();
        subject = ba;
    }

    // Tests that BudgetApp has an empty EntryList
    @Test
    public void testConstructor() {
        EntryList entryList = ba.getEntryList();
        assertEquals(0, entryList.size());
    }

    // Tests that -amount is returned if an expense and +amount if not an expense
    @Test
    public void testComputeValue() {
        assertEquals(-6, ba.computeValue(6, true));
        assertEquals(0, ba.computeValue(0, false));
        assertEquals(888, ba.computeValue(888, false));
    }


    @Test
    public void testAddEntry() {
        ba.addBasicEntry(39.0, "color", LocalDate.of(2019,06,04), new Category("Cat"));
        ba.addBasicEntry(-300.0, "hey", LocalDate.of(1996, 12, 05), new Category( "Cat"));

        EntryList entryList = ba.getEntryList();
        assertEquals(2, entryList.size());

        Entry entry1 = entryList.getElement(0);
        Entry entry2 = entryList.getElement(1);

        assertEquals(39, entry1.getAmount());
        assertEquals(-300, entry2.getAmount());

        assertEquals("color", entry1.getLabel());
        assertEquals("hey", entry2.getLabel());

        assertEquals(LocalDate.of(2019,06,04), entry1.getDate());
        assertEquals(LocalDate.of(1996, 12, 05), entry2.getDate());
    }

    // case where there is nothing to update
    @Test
    public void testUpdateListNoUpdate() {
        LocalDate now = LocalDate.now();

        FutureEntry f1 = new FutureEntry(20.0, "", now.plusDays(3));
        FutureEntry f2 = new FutureEntry(21.0, "ll", now.plusDays(1));
        FutureEntry f3 = new FutureEntry(22.0, "hi", now.plusDays(5));
        ba.addFutureEntry(f1);
        ba.addFutureEntry(f2);
        ba.addFutureEntry(f3);
        ba.updateLists();
        EntryList entryList = ba.getEntryList();
        EntryList futureEntryList = ba.getFutureEntryList();
        assertEquals(0, entryList.size());
        assertEquals(3, futureEntryList.size());
    }

    // multiple things to update from today and prev day
    @Test
    public void testUpdateListMultiple() {
        LocalDate now = LocalDate.now();

        FutureEntry f4 = new FutureEntry(20.0, "nope", now.plusDays(3));
        FutureEntry f5 = new FutureEntry(21.0, "nah", now);
        FutureEntry f6 = new FutureEntry(22.0, "", now.minusDays(3));
        f4.setCategory(new Category("Other"));
        f5.setCategory(new Category("Other"));
        f6.setCategory(new Category("Other"));
        ba.addFutureEntry(f4);
        ba.addFutureEntry(f5);
        ba.addFutureEntry(f6);
        ba.updateLists();
        EntryList entryList = ba.getEntryList();
        EntryList futureEntryList = ba.getFutureEntryList();
        assertEquals(2, entryList.size());
        assertEquals(43, entryList.sumAmount());
        Entry e = entryList.getElement(0);
        assertEquals("nah", e.getLabel());
        assertEquals(21.0, e.getAmount());
        assertEquals(1, futureEntryList.size());
        assertEquals(20, futureEntryList.sumAmount());
        assertFalse(futureEntryList.contains(f5));
        assertFalse(futureEntryList.contains(f6));
        assertTrue(futureEntryList.contains(f4));
    }

    @Test
    public void testSetUpCategories() {
        LocalDate now = LocalDate.now();
        FutureEntry f1 = new FutureEntry(20.0, "", now.plusDays(3));
        FutureEntry f2 = new FutureEntry(21.0, "ll", now.plusDays(1));
        FutureEntry f3 = new FutureEntry(22.0, "hi", now.plusDays(5));
        ba.addFutureEntry(f1);
        ba.addFutureEntry(f2);
        ba.addFutureEntry(f3);
        BasicEntry e1 = new BasicEntry(39.0, "color", LocalDate.of(2019,06,04));
        BasicEntry e2 = new BasicEntry(-300.0, "hey", LocalDate.of(1996, 12, 05));
        ba.addBasicEntry(e1);
        ba.addBasicEntry(e2);
        List<String> futureKeys = new ArrayList<>(Arrays.asList("Cat", "Cat", "Laundry"));
        List<String> keys = new ArrayList<>(Arrays.asList("Dog", "Cat"));
        ba.setUpCategories(keys, futureKeys);
        Map<String, Category> categories = ba.getCategories();
        assertEquals(4, categories.size());
        assertEquals(new Category("Cat"), f1.getCategory());
        assertEquals(new Category("Cat"), f2.getCategory());
        assertEquals(new Category("Laundry"), f3.getCategory());
        assertEquals(new Category("Dog"), e1.getCategory());
        assertEquals(new Category("Cat"), e2.getCategory());
        Category c = f1.getCategory();
        List cList = c.getEntries();
        assertEquals(3, cList.size());
        assertTrue(cList.contains(f1));
        assertTrue(cList.contains(f2));
        assertTrue(cList.contains(e2));
        assertTrue(f1.getCategory() == e2.getCategory());
    }

    }
