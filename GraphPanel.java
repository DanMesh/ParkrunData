/*
    Graph Panel object for Parkrun data collector
    Daniel Mesham
    23 January 2017
*/

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

class GraphPanel extends JPanel {

    protected GraphPoint origin;

    private double MAX_X = 0.0;
    private  double MAX_Y = 0.0;

    private int originX = 50;
    private int originY = 0;

    private int width = 0;
    private int height = 0;

    protected GraphPanel(int widthIn, int heightIn, double maxX, double maxY) {
        super();
        width = widthIn;
        height = heightIn;
        MAX_X = maxX;
        MAX_Y = maxY;
        setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    private void drawAxes(Graphics g) {
        originX = 50;
        originY = height - 50;
        int endX = width - 50;
        int endY = 50;

        g.drawLine(originX, originY, endX, originY);    // x-axis
        g.drawLine(originX, originY, originX, endY);    // y-axis

        // Choose maximum x & y values
        MAX_X = (int) 10*(Math.ceil(MAX_X/10) + 1);
        MAX_Y = (int) 10*(Math.ceil(MAX_Y/10) + 1);

        // Choose tick mark intervals
        int xInterval = tickInterval(MAX_X);
        int yInterval = tickInterval(MAX_Y);

        // Check marks & axis values
        for (int x = 0; x <= MAX_X; x += xInterval) {
            // x axis
            g.drawLine(valToX(x), valToY(0.0), valToX(x), valToY(0.0)+5);
            g.drawString(x + "", valToX(x)-4, valToY(0.0) + 20);
        }
        for (int y = 0; y <= MAX_Y; y += yInterval) {
            // y axis
            g.drawLine(valToX(0), valToY(y), valToX(0)-5, valToY(y));
            g.drawString(y + "", valToX(0.0)-35, valToY(y) + 5);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        drawAxes(g);
    }

    protected int xCoord(GraphPoint p) {
        return valToX(p.getX());
    }
    protected int yCoord(GraphPoint p) {
        return valToY(p.getY());
    }

    protected int valToX(double xVal) {
        return originX + lengthToX(xVal);
    }
    protected int valToY(double yVal) {
        return originY - lengthToY(yVal);
    }

    /** Converts a raw data value interval size to the equivalent width on the graph */
    protected int lengthToX(double xLength) {
        return (int) Math.round((width-100)*xLength/MAX_X);
    }
    protected int lengthToY(double yLength) {
        return (int) Math.round((height-100)*yLength/MAX_Y);
    }

    /** Generates the interval of tick marks best suited to the given maximum axis vale */
    private int tickInterval(double maxVal) {
        int powInt = (int) Math.ceil(Math.log10(maxVal) - 1);   // The approximate power of the tick (maxVal/10)
        int interval = (int) Math.pow(10, powInt);              // The first guess at an interval
        double numTicks = maxVal/Math.pow(10.0, powInt);        // The number of ticks this interval would give

        if (numTicks <= 2) {                    // If there are <= 2 ticks, divide interval by 5
            interval = (int) interval/5;
        }
        else if (numTicks <= 5) {               // If there are <= 5 ticks, divide interval by 2
            interval = (int) interval/2;
        }
        return interval;
    }
}
