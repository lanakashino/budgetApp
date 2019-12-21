package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EntryListTest {

    private static final double POS = 22;
    private static final double NEG = -23;
    private static final String RAND = "jsjdh";
    private static final String EMPTY = "";
    private static final LocalDate NOW = LocalDate.now();
    private static final LocalDate PAST = LocalDate.of(2017, 01, 3);

    private Entry e1;
    private Entry e2;
    private Entry e3;
    private EntryList list;

    @BeforeEach
    public void runBefore() {
        e1 = new BasicEntry(POS, RAND, NOW);
        e2 = new BasicEntry(NEG, EMPTY, PAST);
        e3 = new BasicEntry(NEG, RAND, NOW);
        e1.setCategory(new Category("Cat"));
        e2.setCategory(new Category("Cat"));
        e3.setCategory(new Category("Laundry"));

        list = new EntryList();
    }

    // Tests that an empty EntryList is created
    @Test
    public void testConstructor() {
        assertEquals(0, list.size());
    }

    // Tests multiple entries can added, checking for correct item and list size between every entry
    @Test
    public void testAdd() {
        list.add(e1);
        assertTrue(list.contains(e1));
        assertEquals(1, list.size());
        list.add(e2);
        assertTrue(list.contains(e1));
        assertTrue(list.contains(e2));
        assertEquals(2, list.size());
    }

    // Tests that duplicate entries can be made
    @Test
    public void testAddDuplicates() {
        list.add(e1);
        list.add(e1);
        assertTrue(list.contains(e1));
        assertEquals(2, list.size());
    }

    // Tests that an entry can be removed;
    @Test
    public void testRemove(){
        list.add(e1);
        list.add(e2);
        list.add(e3);
        try {
            list.remove(e2);
            assertTrue(list.contains(e1));
            assertTrue(list.contains(e3));
            assertEquals(2, list.size());
        } catch (InvalidEntryException e) {
            fail("not in list");
        }
    }

    @Test
    public void testRemoveFail(){
        list.add(e1);
        list.add(e3);
        try {
            list.remove(e2);
            fail("Should throw exception");
        } catch (InvalidEntryException e) {
        }
    }


    // Tests that an single copy of a duplicate entry can be removed;
    @Test
    public void testRemoveDuplicate(){
        list.add(e1);
        list.add(e1);
        try {
            list.remove(e1);
            assertTrue(list.contains(e1));
            assertEquals(1, list.size());
        } catch (InvalidEntryException e) {
            fail("not in list");
        }
    }

    // Tests that correct sum is calculated in a EntryList
    @Test
    public void testSumAmount() {
        list.add(e1);
        list.add(e2);
        list.add(e3);
        assertEquals(NEG+NEG+POS, list.sumAmount());
    }

    // Tests that if EntryList is empty, sum returned is 0
    @Test
    public void testEmptySumAmount() {
        assertEquals(0, list.sumAmount());
    }


    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.add(e1);
        list.add(e2);
        list.add(e3);
        assertEquals(3, list.size());
    }

    // Test that the correct number of Entries contained in EntryList is returned
    // case:
    // - empty list
    // - contains an object
    // - does not contains an object
    @Test
    public void testContains() {
        assertFalse(list.contains(e1));
        list.add(e1);
        list.add(e2);
        assertTrue(list.contains(e1));
        assertFalse(list.contains(e3));
    }

    @Test
    public void testLoad() throws IOException {
        List<String> keys = list.load("load.txt");
        assertEquals(new ArrayList<>(Arrays.asList("Cat", "Cat", "Laundry")), keys);

        assertEquals(3, list.size());

        Entry entry1 = list.getElement(0);
        Entry entry2 = list.getElement(1);
        Entry entry3 = list.getElement(2);

        assertEquals(39, entry1.getAmount());
        assertEquals(-300, entry2.getAmount());
        assertEquals(30, entry3.getAmount());

        assertEquals("color", entry1.getLabel());
        assertEquals("hey", entry2.getLabel());
        assertEquals("wow", entry3.getLabel());

        assertEquals(LocalDate.of(2019,06,04), entry1.getDate());
        assertEquals(LocalDate.of(1996, 12, 05), entry2.getDate());
        assertEquals(LocalDate.of(2013, 10, 05), entry3.getDate());
    }

    @Test
    public void testSaveFuture() throws IOException {
        FutureEntry fe1 = new FutureEntry(POS, RAND, NOW);
        FutureEntry fe2 = new FutureEntry(NEG, EMPTY, PAST);
        FutureEntry fe3 = new FutureEntry(NEG, RAND, NOW);

        fe1.setCategory(new Category("Cat"));
        fe2.setCategory(new Category("Cat"));
        fe3.setCategory(new Category("Laundry"));

        list.add(fe1);
        list.add(fe2);
        list.add(fe3);

        list.save("saveFuture.txt");

        List<String> lines = Files.readAllLines(Paths.get("data/saveFuture.txt"));;
        assertEquals("22.0 jsjdh "  + NOW + " 1 " + NOW + " " + NOW + " Cat", lines.get(0));
        assertEquals("-23.0  " + PAST + " 1 " + PAST + " " + PAST + " Cat", lines.get(1));
        assertEquals("-23.0 jsjdh " + NOW + " 1 " + NOW + " " + NOW + " Laundry", lines.get(2));
    }

    @Test
    public void testLoadFuture() throws IOException {
        List<String> keys = list.load("loadFuture.txt");
        assertEquals(new ArrayList<>(Arrays.asList("Cat", "Cat", "Laundry")), keys);

        assertEquals(3, list.size());

        FutureEntry entry1 = (FutureEntry) list.getElement(0);
        FutureEntry entry2 = (FutureEntry) list.getElement(1);
        FutureEntry entry3 = (FutureEntry) list.getElement(2);

        assertEquals(39, entry1.getAmount());
        assertEquals(-300, entry2.getAmount());
        assertEquals(30, entry3.getAmount());

        assertEquals("color", entry1.getLabel());
        assertEquals("hey", entry2.getLabel());
        assertEquals("wow", entry3.getLabel());

        assertEquals(LocalDate.of(2019,06,04), entry1.getStartDate());
        assertEquals(LocalDate.of(1996, 12, 05), entry2.getStartDate());
        assertEquals(LocalDate.of(2013, 10, 05), entry3.getStartDate());

        assertEquals(1, entry1.getRecurrence());
        assertEquals(2, entry2.getRecurrence());
        assertEquals(4, entry3.getRecurrence());

        assertEquals(LocalDate.of(2019,06,04), entry1.getDate());
        assertEquals(LocalDate.of(2019,06,04), entry2.getDate());
        assertEquals(LocalDate.of(2013, 10, 05), entry3.getDate());

        assertEquals(LocalDate.of(2019,06,04), entry1.getEndDate());
        assertEquals(LocalDate.of(2019,06,04), entry2.getEndDate());
        assertEquals(LocalDate.of(2013, 10, 05), entry3.getEndDate());
    }

    @Test
    public void testSave() throws IOException {
        list.add(e1);
        list.add(e2);
        list.add(e3);

        list.save("save.txt");

        List<String> lines = Files.readAllLines(Paths.get("data/save.txt"));;
        assertEquals("22.0 jsjdh "  + NOW  + " Cat", lines.get(0));
        assertEquals("-23.0  " + PAST  + " Cat", lines.get(1));
        assertEquals("-23.0 jsjdh " + NOW  + " Laundry", lines.get(2));
    }

    @Test
    public void testRemoveList() {
        list.add(e1);
        list.add(e2);
        list.add(e3);

        EntryList removables = new EntryList();
        removables.add(e1);
        removables.add(e3);
        try {
            list.remove(removables);
            assertTrue(list.contains(e2));
            assertEquals(1, list.size());
        } catch (InvalidEntryException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testRemoveListFail() {
        list.add(e1);
        list.add(e2);

        EntryList removables = new EntryList();
        removables.add(e1);
        removables.add(e3);

        try {
            list.remove(removables);
            fail("No exception thrown");
        } catch (InvalidEntryException e) {
        }
    }


//    // Tests that all and only the entries in September is returned
//    // cases:
//    //      - empty
//    //      - none with that month
//    //      - some with that month
//    //      - all with that month
//    @Test
//    public void testSubsetMonth() {
//
//    }
//
//    // Tests that all and only the entries with label "" is returned
//    // cases:
//    //      - empty
//    //      - none with that month
//    //      - some with that month
//    //      - all with that month
//    @Test
//    public void testSubsetLabel() {
//
//    }

}
