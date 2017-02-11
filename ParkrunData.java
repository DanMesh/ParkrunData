/*
    Parkrun Data Collector
    Daniel Mesham
    11 February 2017
*/

/*
    TODO:
     - Make it possible to choose which Parkrun & event to fetch
     - Add local storage to save time
     - Better UI
     - Titles, headings etc. on graphs
*/

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ParkrunData {

    private String latestResultsURL = "http://www.parkrun.co.za/constantiagreenbelt/results/latestresults/";

    private Result[] results;

    public ParkrunData() {

        try {
            // Fetch & store latest results
            storeData(getData());
        } catch (Exception e) {
            System.out.println("FAILED\nERROR: Could not connect to parkrun.co.za!\nSee below for details...");
            e.printStackTrace();
            System.exit(0);
        }

        // Menu
        Scanner scan = new Scanner(System.in);
        int resp = -1;
        while (resp != 0) {
            System.out.println("PARKRUN DATA COLLECTOR");
            System.out.println("===========================");
            System.out.println("1) Show table of results");
            System.out.println("2) Show graph of estimated age vs time");
            System.out.println("3) Show graph of time vs number of runners");
            System.out.println("0) Quit\n");
            System.out.print("Response: ");
            String in = scan.nextLine();
            resp = Integer.parseInt(in.substring(0,1));

            switch (resp) {
                case 0:
                    System.exit(0);
                case 1:
                    showTableWindow();
                    break;
                case 2:
                    // Show agae vs time plot
                    showPlotWindow("Age vs Time", graphAgeVsTime(pointsAgeVsTime(results)));
                    break;
                case 3:
                    // Show time vs num. runners graph
                    showPlotWindow("Time vs Number Of Runners", graphTimeVsRunners(barsTimeVsRunners(results)));
                    break;
                default:
            }
        }

    }

    /**
        Returns the line in the HTML source that contains all the run data
    */
    private String getData() throws Exception {
        System.out.print("Fetching data from website...");
        // Reader for URL
        URL url = new URL(latestResultsURL);

        // Create connection that appears to be from Mozilla (otherwise access denied :( )
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

        // Reader
        BufferedReader in = new BufferedReader( new InputStreamReader( httpcon.getInputStream() ) );

        // Try to skip 13000 characters (for ±3s less time :) )
        try {
            in.skip(13000);
        } catch (Exception e) {}

        // Read in lines until a line contains "#explainTable"
        // This line has the data.
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("#explainTable")) break;
        }
        in.close();
        System.out.println("Done");
        return inputLine;
    }

    /**
        Extracts the useful data from the input line into an array of results.
    */
    private void storeData(String data) {
        System.out.print("Storing data to array...");

        // Remove unwanted data at the beginning
        // Note: also removes first delimiter '<tr>'
        data = data.split("<tbody><tr>")[1];
        // Remove unwanted data at the end
        data = data.split("</tbody>")[0];

        // Split into rows (a row is a result) and init results array
        String rows[] = data.split("<tr>");
        int numEntries = rows.length;
        results = new Result[numEntries];

        // Iterate through rows and convert to Result objects
        for (int i = 0; i < numEntries; i ++) {
            results[i] = new Result(rows[i]);
        }

        System.out.println("Done");
    }

    /**
        Prints the results to the console.
    */
    private void printTable() {
        System.out.println("TABLE OF RESULTS\n*********************");
        System.out.println(String.format("%-4s %-40s %-6s %-8s %-4s", "Pos", "Name", "Time", "AgeCat", "Runs"));
        System.out.println("------------------------------------------------------------------------------");

        for (Result r : results) {
            System.out.println(r.tableRow());
        }

    }


    /**
        Creates a JFrame that displays the data.
        TODO: Should really be its own class...
    */
    private void showTableWindow() {
        JFrame frame = new JFrame("Parkrun Data");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create content pane
        //JPanel panel = new JPanel();
        //panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //frame.setContentPane(panel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVisible(true);

        // Create table
        JTable table = new JTable(results.length, 5);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);
        table.getColumnModel().getColumn(0).setHeaderValue("Position");
        table.getColumnModel().getColumn(1).setHeaderValue("Name");
        table.getColumnModel().getColumn(2).setHeaderValue("Time");
        table.getColumnModel().getColumn(3).setHeaderValue("Age Cat.");
        table.getColumnModel().getColumn(4).setHeaderValue("Runs");
        table.setVisible(true);
        table.setEnabled(false);

        scrollPane.setViewportView(table);
        frame.add(scrollPane, BorderLayout.CENTER);


        // Populate table
        for (int i = 0; i < results.length; i++) {
            table.setValueAt(results[i].getPosition() + "", i, 0);
            table.setValueAt(results[i].getName(), i, 1);
            if (results[i].isUnknown()) continue;
            table.setValueAt(results[i].getTime(), i, 2);
            table.setValueAt(results[i].getAgeCat(), i, 3);
            table.setValueAt(results[i].getNumRuns() + "", i, 4);
        }

        frame.setVisible(true);
    }


    /**
        Creates a JFrame with an age vs time graph
        TODO: Should also be its own class...
    */
    private void showPlotWindow(String title, GraphPanel graph) {
        JFrame frame = new JFrame(title);
        frame.setSize(800,522);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //frame.getContentPane().setBackground(BACKGROUND_COLOUR);

        // Create graph panel
        //BarGraphPanel graph = new GraphPanel(800, 500, 100, 70);

        // Plot graph
        // TODO: MAke this method merely store the data to be plotted, which the graph will do when it is ready
        //graph.plot(points_averageY(points_AGEvsTIME(results)), Color.GREEN);


        // Add graph & spacers
        /*JPanel spacer = new JPanel();frame.add(spacer, BorderLayout.NORTH);
        spacer = new JPanel();frame.add(spacer, BorderLayout.SOUTH);
        spacer = new JPanel();frame.add(spacer, BorderLayout.EAST);
        spacer = new JPanel();frame.add(spacer, BorderLayout.WEST);*/

        frame.add(graph, BorderLayout.CENTER);

        frame.setVisible(true);


    }

    /**
        Creates a ScatterGraphPanel that plots age vs time
    */
    private ScatterGraphPanel graphAgeVsTime(GraphPoint[] data) {
        GraphPoint[][] arr = {data};
        return new ScatterGraphPanel(800, 500, 100, 80, arr);
    }

    /**
        Creates a BarGraphPanel that plots time vs number of runners
    */
    private BarGraphPanel graphTimeVsRunners(GraphBar[] data) {
        return new BarGraphPanel(800, 500, 80, 40, data);
    }

    /**
        Creates an array of GraphPoints from the data.
        Age is x-val and Time is y-val
    */
    private GraphPoint[] pointsAgeVsTime(Result[] data) {
        GraphPoint[] points = new GraphPoint[data.length];
        int numPoints = 0;
        for (int i = 0; i < data.length; i ++) {
            if (data[i].isUnknown()) continue;
            points[numPoints] = new GraphPoint(data[i].estAge(), data[i].getTimeDouble());
            numPoints++;
        }
        // resize output array:
        GraphPoint[] output = new GraphPoint[numPoints];
        for (int i = 0; i < numPoints; i++) output[i] = points[i];

        return output;
    }

    /**
        Modifies an array of Graph Points so that the y-values are the
        AVERAGES of all the given y-values.
    */
    private GraphPoint[] pointsAverageY(GraphPoint[] data) {
        ArrayList<GraphPoint> out = new ArrayList<GraphPoint>();
        ArrayList<Double> xVals = new ArrayList<Double>();

        for (GraphPoint gp : data) {
            double x = gp.getX();
            // If the x value hasnt been averaged yet:
            if (!xVals.contains(x)) {
                xVals.add(x);           // Add x
                // Average all points with x
                GraphPoint[] dataWithX = pointsWithX(x, data);
                double totalY = 0.0;
                for (GraphPoint gp2 : dataWithX) {
                    totalY += gp2.getY();
                }
                double avgY = totalY/dataWithX.length;
                out.add(new GraphPoint(x, avgY));
            }
        }

        return out.toArray(new GraphPoint[0]);
    }

    /**
        Returns all the points in a given GraphPoint array
        with a given X value.
    */
    private GraphPoint[] pointsWithX(double xVal, GraphPoint[] data) {
        ArrayList<GraphPoint> out = new ArrayList<GraphPoint>();

        for (GraphPoint gp : data) {
            if (gp.getX() == xVal) out.add(gp);
        }

        return out.toArray(new GraphPoint[0]);
    }

    /**
        Returns an array of graph bars representing the
        number of runners finishing within each minute.
        The data MUST still be ordered by time!
    */
    private GraphBar[] barsTimeVsRunners(Result[] data) {
        ArrayList<GraphBar> bars = new ArrayList<GraphBar>();

        //Process the first result
        bars.add(new GraphBar(data[0].getMinutes(), 1, 1));
        GraphBar last = bars.get(bars.size()-1);        // The 'previous' bar

        for (int i = 1; i < data.length; i++) {
            Result r = data[i];                         // The next result
            if (r.isUnknown()) continue;                // Do nothing if unknown

            // If the result has the same time as the previous one, just increment the previous bar's height
            if (r.getMinutes() == last.getX()) {
                last.incrementHeight();
            }
            // The result has a later time, create a new bar
            else {
                bars.add(new GraphBar(data[i].getMinutes(), 1, 1));
                last = bars.get(bars.size()-1);
            }
        }
        GraphBar[] ret = new GraphBar[bars.size()];
        bars.toArray(ret);
        return ret;
    }

    public static void main(String[] args) {
        new ParkrunData();
    }

}
