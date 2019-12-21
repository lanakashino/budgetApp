package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceMonitorTest {

    BalanceMonitor bm;

    @BeforeEach
    public void runBefore() {
        bm = new BalanceMonitor(0.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, bm.getThreshold());
    }

}
