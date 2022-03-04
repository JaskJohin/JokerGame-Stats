package plh24_ge3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import model.AverageDistributedPrizeCat;
import model.BonusOccurrence;
import model.QueriesSQL;
import model.WinningNumberOccurrence;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;

/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class CreateChart extends ApplicationFrame {
    private final JFreeChart CHART;
    private final ChartPanel CHART_PANEL;
    
    /**
     * Creates a new demo.
     * @param title  the frame title.
     * @throws java.text.ParseException
     */
    public CreateChart(CategoryDataset dataSet, final String title, String xLabel, String yLabel) throws ParseException {
        super(title);
        CHART = createChart(dataSet, title, xLabel, yLabel);
        CHART_PANEL = new ChartPanel(CHART);
        CHART_PANEL.setPreferredSize(new Dimension(700, 370));
        setContentPane(CHART_PANEL);
    }
 
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    public static CategoryDataset createTop5WinningNDataset(String fromDate, String toDate) throws ParseException {
        List<WinningNumberOccurrence> wnOccList = QueriesSQL.topFiveWinningNumbersOccurred(fromDate, toDate);
        // column keys...
        final String type = "Top 5 Winning Numbers by Occurrence";
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // row keys...
        wnOccList.forEach((wnOcc) -> {
            final String series = "Number: " + wnOcc.getWinningNumber();

            dataset.addValue(wnOcc.getOccurrences(), series, type);
        });
        return dataset;         
    }
    
    public static CategoryDataset createTop5BonusNDataset(String fromDate, String toDate) throws ParseException {
        List<BonusOccurrence> bonusOccList = QueriesSQL.topFiveBonusesOccurred(fromDate, toDate);
        // column keys...
        final String type = "Top 5 Bonus Numbers by Occurrence";
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // row keys...
        bonusOccList.forEach((bonusOcc) -> {
            final String series = "Number: " + bonusOcc.getBonus();

            dataset.addValue(bonusOcc.getOccurrences(), series, type);
        });
        return dataset;         
    }
    
    
    public static CategoryDataset createAverageDistrPerCategoryDataset(String fromDate, String toDate) throws ParseException {
        List<AverageDistributedPrizeCat> averageDistrList = QueriesSQL.averageDistributedPerCategory(fromDate, toDate);
        // column keys...
        final String type = "Average winnings distributed per Category";
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // row keys...
        averageDistrList.forEach((averageDistr) -> {
            final Integer series = averageDistr.getCategoryId();

            dataset.addValue(averageDistr.getAverageDistributed(), series, type);
        });
        return dataset;         
    }
    
    /**
     * Creates a sample chart.
     * @param dataset a dataset.
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset, String title, String xAxisLabel, String yAxisLabel) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            title,   // chart title
            xAxisLabel,         // domain axis label
            yAxisLabel,             // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );
 
        chart.setBackgroundPaint(Color.white);
 
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        
        NumberFormat number = NumberFormat.getNumberInstance();
        number.setMaximumFractionDigits(0);
        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setNumberFormatOverride(number);
        rangeAxis.setAutoRangeIncludesZero(true);
 
        // customise the renderer...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER));
        renderer.setBaseItemLabelsVisible(true);
        //renderer.setDrawShapes(true);
 
        renderer.setSeriesStroke(
            0, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            )
        );
        renderer.setSeriesStroke(
            1, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            )
        );
        renderer.setSeriesStroke(
            2, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f
            )
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
    }
}

