package stats;

import java.util.List;

public class RunningSummary implements StatSummary {
    // Fields to maintain running calculations
    private int count;
    private double sum;
    private double sumSquares;

    public RunningSummary() {
        count = 0;
        sum = 0.0;
        sumSquares = 0.0;
    }

    @Override
    public double mean() {
        if (count == 0) {
            throw new NotEnoughDataException("Cannot calculate mean of empty dataset");
        }
        return sum / count;
    }

    @Override
    public int n() {
        return count;
    }

    @Override
    public double sum() {
        return sum;
    }

    @Override
    public double standardDeviation() {
        if (count < 2) {
            throw new NotEnoughDataException("Need at least 2 values for standard deviation");
        }

        // Bessel's correction applied: divide by (count - 1) instead of count
        double meanOfSquares = sumSquares / count;
        double squareOfMean = (sum / count) * (sum / count);
        return Math.sqrt((sumSquares - (sum * sum) / count) / (count - 1));
    }


    @Override
    public StatSummary add(double value) {
        count++;
        sum += value;
        sumSquares += value * value;
        return this;
    }

    @Override
    public StatSummary add(Number value) {
        return add(value.doubleValue());
    }

    @Override
    public StatSummary add(List<? extends Number> values) {
        for (Number value : values) {
            add(value.doubleValue());
        }
        return this;
    }
}