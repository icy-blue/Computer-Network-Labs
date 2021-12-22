package exp502;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class Plot {
    public static void main(String[] args) {
        double[] xData = new double[]{9.33, 11.0, 15.0, 20.33, 22.0, 25.0};
        double[] yData = new double[]{0.7847, 0.7319, 0.68, 0.6683, 0.6557, 0.5825};
        XYChart chart = QuickChart.getChart("Relationship", "Error Rate/%", "Throughput",
                "relationship", xData, yData);
        new SwingWrapper(chart).displayChart();
    }
}

/*
error = 9.33% throughput = 0.7847
error = 11.00% throughput = 0.7319
error = 15.00% throughput = 0.6800
error = 20.33% throughput = 0.6683
error = 22.00% throughput = 0.6557
error = 25.00% throughput = 0.5825
 */
