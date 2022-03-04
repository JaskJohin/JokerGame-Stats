package plh24_ge3;


/**
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */


import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.List;

public class Graph_Form extends ApplicationFrame {

   public Graph_Form( String applicationTitle , String chartTitle , List<Integer> numbers, List<Integer> occurency ) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         "Numbers","Occurency",
         createDataset(numbers, occurency),
         PlotOrientation.VERTICAL,
         true,true,false);
      
         
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
      chartPanel.setVisible( true );
      
   }
   
   public Graph_Form( String applicationTitle , List<Integer> categoryIds, List<Long> dist, String chartTitle) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         "Categories","Distributions",
         createDatasetForDistribution(categoryIds, dist),
         PlotOrientation.VERTICAL,
         true,true,false);
      
         
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
      chartPanel.setVisible( true );
      
   }
   
   private DefaultCategoryDataset createDatasetForDistribution(List<Integer> ids, List<Long> dists) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      dataset.addValue( dists.get(0) , "Category" , ids.get(0) );      
      dataset.addValue( dists.get(1) , "Category" , ids.get(1) );   
      dataset.addValue( dists.get(2) , "Category" , ids.get(2) );   
      dataset.addValue( dists.get(3) , "Category" , ids.get(3) );   
      dataset.addValue( dists.get(4) , "Category" , ids.get(4) );   
      dataset.addValue( dists.get(5) , "Category" , ids.get(5) );   
      dataset.addValue( dists.get(6) , "Category" , ids.get(6) );   
      dataset.addValue( dists.get(7) , "Category" , ids.get(7) );   
            
      return dataset;
   } 
  

   private DefaultCategoryDataset createDataset(List<Integer> numbers, List<Integer> occurency ) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      dataset.addValue( occurency.get(0) , "number" , numbers.get(0) );
      dataset.addValue( occurency.get(1) , "number" , numbers.get(1) );
      dataset.addValue( occurency.get(2) , "number" , numbers.get(2) );
      dataset.addValue( occurency.get(3) , "number" , numbers.get(3) );
      dataset.addValue( occurency.get(4) , "number" , numbers.get(4) );
      
      

      return dataset;
   } 
}