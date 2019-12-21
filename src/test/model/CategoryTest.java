package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    private Category c;
    private Entry e1;
    private Entry e2;
    private Entry e3;

    @BeforeEach
    public void runBefore() {
        c = new Category("Food");
        e1 = new BasicEntry(0.0, "", LocalDate.now());
        e2 = new FutureEntry(11.0, "", LocalDate.now());
        e3 = new BasicEntry(24.0, "", LocalDate.now());
    }

    @Test
    public void testConstructor() {
        assertEquals("Food", c.getName());
        List<Entry> entries = c.getEntries();
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testAdd() {
        c.add(e1);
        List<Entry> entries = c.getEntries();
        assertEquals(1, entries.size());
        assertTrue(entries.contains(e1));
        assertEquals(e1.getCategory(), c);
        c.add(e3);
        entries = c.getEntries();
        assertEquals(2, entries.size());
        assertTrue(entries.contains(e3));
        assertEquals(e3.getCategory(), c);
    }

    @Test
    public void testAddAlreadyIn() {
        c.add(e1);
        List<Entry> entries = c.getEntries();
        assertEquals(1, entries.size());
        assertTrue(entries.contains(e1));
        assertEquals(e1.getCategory(), c);
        c.add(e1);
        entries = c.getEntries();
        assertEquals(1, entries.size());
        assertTrue(entries.contains(e1));
        assertEquals(e1.getCategory(), c);
    }

    @Test
    public void testRemove() {
        c.add(e1);
        c.add(e2);
        c.add(e3);
        List<Entry> entries = c.getEntries();
        assertEquals(3, entries.size());
        c.remove(e2);
        entries = c.getEntries();
        assertEquals(2, entries.size());
        assertFalse(entries.contains(e2));
        assertEquals(c,e1.getCategory());
        assertNull(e2.getCategory());
        assertEquals(c,e3.getCategory());
    }

    @Test
    public void testRemoveNone() {
        c.add(e1);
        c.add(e2);
        List<Entry> entries = c.getEntries();
        assertEquals(2, entries.size());
        c.remove(e3);
        entries = c.getEntries();
        assertEquals(2, entries.size());
        assertFalse(entries.contains(e3));
        assertEquals(c,e1.getCategory());
        assertEquals(c, e2.getCategory());
        assertNull(e3.getCategory());
    }

    @Test
    public void testHashEquals() {
        assertEquals(c, new Category("Food"));
    }

    @Test
    public void testSum() {
        assertEquals(0.0, c.getSum());
        c.add(e1);
        c.add(e2);
        c.add(e3);
        assertEquals(24.0, c.getSum());
    }

}
