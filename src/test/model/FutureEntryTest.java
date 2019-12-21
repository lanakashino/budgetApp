package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class FutureEntryTest extends EntryTest {

    private static final double POS = 22;
    private static final double NEG = -23;
    private static final String RAND = "jsjdh";
    private static final String EMPTY = "";
    private static final LocalDate NOW = LocalDate.now();
    private static final LocalDate PAST = LocalDate.of(2017, 01, 3);
    private static final LocalDate FUTURE = LocalDate.of(2222, 03, 03);

    private FutureEntry fe1;
    private FutureEntry fe2;

    @BeforeEach
    public void beforeRun() {
        fe1 = new FutureEntry(POS, RAND, NOW);
        fe2 = new FutureEntry(NEG, EMPTY, PAST);
        e1 = fe1;
        e2 = fe2;
    }

    // Tests Entry is made with the correct amount, label and date
    @Test
    public void testConstructor() {
        assertEquals(POS, fe1.getAmount());
        assertEquals(NEG, fe2.getAmount());
        assertEquals(RAND, fe1.getLabel());
        assertEquals(EMPTY, fe2.getLabel());
        assertEquals(NOW, fe1.getDate());
        assertEquals(PAST, fe2.getDate());
        assertEquals(1, fe1.getRecurrence());
        assertEquals(1, fe2.getRecurrence());
        assertEquals(NOW, fe1.getEndDate());
        assertEquals(PAST, fe2.getEndDate());
    }

    // Checks that correct amount is returned
    @Test
    public void testGetAmount() {
        assertEquals(POS, fe1.getAmount());
    }

    // Checks that correct label is returned
    @Test
    public void testGetLabel() {
        assertEquals(RAND, fe1.getLabel());
    }

    // Checks that correct date is returned
    @Test
    public void testGetDate() {
        assertEquals(NOW, fe1.getDate());
    }

    @Test
    public void testAddRecurrenceInvalidRecurrenceHigh() {
        try {
            fe1.addRecurrenceBehaviour(6, FUTURE);
            fail("No exceptions caught...");
        } catch (InvalidRecurrenceException e) {
        }
    }

    @Test
    public void testAddRecurrenceInvalidRecurrenceLow() {
        try {
            fe1.addRecurrenceBehaviour(0, FUTURE);
            fail("No exceptions caught...");
        } catch (InvalidRecurrenceException e) {
        }
    }

    @Test
    public void testAddRecurrenceValidParameters() {
        try {
            fe1.addRecurrenceBehaviour(5, FUTURE);
            assertEquals(5, fe1.getRecurrence());
            assertEquals(FUTURE, fe1.getEndDate());
        } catch (InvalidRecurrenceException e) {
            fail("Recurrence Exception was thrown....");
        }
    }

    // Case:
    //      - fails since no recurrence assigned (1)
    //      - fails since end date is met before next nextDate
    //      - succeeds daily (2)
    //      - succeeds weekly (3)
    //      - succeeds monthly (4)
    //      - succeeds yearly (5)
    @Test
    public void testUpdate() {
        assertFalse(fe1.updateNextDate());
        assertEquals(NOW, fe1.getDate());
        fe1.addRecurrenceWExceptions(2, FUTURE);
        assertTrue(fe1.updateNextDate());
        assertEquals(NOW.plusDays(1), fe1.getDate());
        fe1 = new FutureEntry(POS, RAND, NOW);
        fe1.addRecurrenceWExceptions(3, FUTURE);
        assertTrue(fe1.updateNextDate());
        assertEquals(NOW.plusWeeks(1), fe1.getDate());
        fe1 = new FutureEntry(POS, RAND, NOW);
        fe1.addRecurrenceWExceptions(4, FUTURE);
        assertTrue(fe1.updateNextDate());
        assertEquals(NOW.plusMonths(1), fe1.getDate());
        fe1 = new FutureEntry(POS, RAND, NOW);
        fe1.addRecurrenceWExceptions(5, FUTURE);
        assertTrue(fe1.updateNextDate());
        assertEquals(NOW.plusYears(1), fe1.getDate());
        fe1 = new FutureEntry(POS, RAND, NOW);
        fe1.addRecurrenceWExceptions(4, NOW.plusWeeks(2));
        assertFalse(fe1.updateNextDate());
    }

    @Test
    public void testGetRecurrence() {
        fe2.addRecurrenceWExceptions(5, NOW);
        assertTrue(fe2.keepRecurrence());
        assertFalse(fe1.keepRecurrence());
    }

}
