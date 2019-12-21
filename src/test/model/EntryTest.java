package model;

import org.junit.jupiter.api.Test;

import javax.smartcardio.CardTerminal;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class EntryTest {
    protected Entry e1;
    protected Entry e2;

    @Test
    public void setCategoryNull() {
        assertNull(e1.getCategory());
        Category c = new Category("Food");
        e1.setCategory(c);
        assertEquals(c, e1.getCategory());
        List<Entry> entries = c.getEntries();
        assertTrue(entries.contains(e1));
    }

    @Test
    public void setCategoryAlreadySet() {
        assertNull(e1.getCategory());
        Category c = new Category("Food");
        e1.setCategory(c);
        assertEquals(c, e1.getCategory());
        List<Entry> entries = c.getEntries();
        assertTrue(entries.contains(e1));
        e1.setCategory(c);
        assertEquals(c, e1.getCategory());
        entries = c.getEntries();
        assertTrue(entries.contains(e1));
        assertEquals(1, entries.size());
    }

    @Test
    public void setCategoryChangeCategory() {
        assertNull(e1.getCategory());
        Category c = new Category("Food");
        e1.setCategory(c);
        assertEquals(c, e1.getCategory());
        List<Entry> entries = c.getEntries();
        assertTrue(entries.contains(e1));
        Category c2 = new Category("Cat");
        e1.setCategory(c2);
        assertEquals(c2, e1.getCategory());
        entries = c.getEntries();
        assertFalse(entries.contains(e1));
        List<Entry> entries2 = c2.getEntries();
        assertTrue(entries2.contains(e1));
    }


//    // MODIFIES: this and category
//    // EFFECTS: changes this category into the parameter category
//    // ensures that this is removed from precious category and is in new category
//    public void setCategory(Category category) {
//        removeCategory(this.category);
//        addCategory(category);
//    }



}
