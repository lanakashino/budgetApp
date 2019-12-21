package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicEntryTest extends EntryTest{

    private static final double POS = 22;
    private static final double NEG = -23;
    private static final String RAND = "jsjdh";
    private static final String EMPTY = "";
    private static final LocalDate NOW = LocalDate.now();
    private static final LocalDate PAST = LocalDate.of(2017, 01, 3);

    private BasicEntry be1;
    private BasicEntry be2;

    @BeforeEach
    public void beforeRun() {
        be1 = new BasicEntry(POS, RAND, NOW);
        be2 = new BasicEntry(NEG, EMPTY, PAST);
        e1 = be1;
        e2 = be2;
    }

    // Tests Entry is made with the correct amount, label and date
    @Test
    public void testConstructor() {
        assertEquals(POS, be1.getAmount());
        assertEquals(NEG, be2.getAmount());
        assertEquals(RAND, be1.getLabel());
        assertEquals(EMPTY, be2.getLabel());
        assertEquals(NOW, be1.getDate());
        assertEquals(PAST, be2.getDate());
    }

    // Checks that correct amount is returned
    @Test
    public void testGetAmount() {
        assertEquals(POS, be1.getAmount());
    }

    // Checks that correct label is returned
    @Test
    public void testGetLabel() {
        assertEquals(RAND, be1.getLabel());
    }

    // Checks that correct date is returned
    @Test
    public void testGetDate() {
        assertEquals(NOW, be1.getDate());
    }

}
