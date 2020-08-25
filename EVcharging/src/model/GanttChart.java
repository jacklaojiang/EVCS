package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class GanttChart extends ApplicationFrame{
	public GanttChart(ArrayList<Gantt> ganttData, int EVNum) {

        super("Gantt Chart");

        final IntervalCategoryDataset dataset = createDataset(ganttData);
        final JFreeChart chart = createChart(dataset,EVNum);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
    
    /**
     * Creates a gantt dataset for a Gantt chart.
     *
     * @return The dataset.
     */
    public static IntervalCategoryDataset createDataset(ArrayList<Gantt> ganttData) {

    	final TaskSeriesCollection collection = new TaskSeriesCollection();
    	for(Gantt gantt : ganttData)
    	{
    		final TaskSeries s1 = new TaskSeries(gantt.getCustomerID()+"");
    		Date endDate = new Date(gantt.getStartTime().getTime()+gantt.getDuration()*60*1000); //diff
    		s1.add(new Task(gantt.getChargingPoint()+"",
               new SimpleTimePeriod(gantt.getStartTime(), endDate)));
    		collection.add(s1);
    	}    

        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * not use
     */
    private static Date date(final int hour, final int min) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(hour,min);
        final Date result = calendar.getTime();
        return result;

    }
        
    /**
     * Creates a chart and set variable name
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset, int EVNum) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Number of reservation: "+EVNum,  // chart title
            "ChargingPoint",              // domain axis label
            "Time",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );    
//        chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
        return chart;    
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void createGantt(ArrayList<Gantt> ganttData, int EVNum) {

        final GanttChart demo = new GanttChart(ganttData,EVNum);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
}
