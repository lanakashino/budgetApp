package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BudgetApp;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubjectTest {
    protected Subject subject;

    @BeforeEach
    public void runBefore() {
        subject = new BudgetApp();
    }

    @Test
    public void testConstructor() {
        List<Observer> observers = subject.getObservers();
        assertEquals(1, observers.size());
    }

    @Test
    public void testNotifyObserver() {
        subject.addObserver(new BalanceMonitor(0.0));
        subject.notifyObserver(20.0);
    }

    @Test
    public void testAddObserver() {
        subject.addObserver(new BalanceMonitor(1.0));
        List<Observer> observers = subject.getObservers();
        assertEquals(2, observers.size());
        subject.addObserver(new BalanceMonitor(0.0));
        observers = subject.getObservers();
        assertEquals(2, observers.size());
        assertTrue(observers.contains(new BalanceMonitor(0.0)));

    }

}
