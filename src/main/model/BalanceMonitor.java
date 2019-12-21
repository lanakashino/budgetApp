package model;

import java.util.Objects;

public class BalanceMonitor implements Observer {
    Double threshold;

    public BalanceMonitor(Double threshold) {
        this.threshold = threshold;
    }

    // EFFECTS: prints a warning when num is over threshold
    @Override
    public void update(Double num) {
        if (threshold > num) {
            System.out.println("YOU'RE BALANCE IS UNDER " + threshold + "!! STOP SPENDING, BUDGET MORE!!");
        }
    }

    public Double getThreshold() {
        return threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BalanceMonitor that = (BalanceMonitor) o;
        return threshold.equals(that.threshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threshold);
    }
}
