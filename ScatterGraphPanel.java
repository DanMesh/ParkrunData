/*
    Scatter Graph Panel object for Parkrun data collector
    Daniel Mesham
    28 January 2017
*/

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

class ScatterGraphPanel extends GraphPanel {

    private GraphPoint[][] dataPoints;

    private static int size = 3;

    private static Color[] COLORS = { Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW };

    public ScatterGraphPanel(int widthIn, int heightIn, double maxX, double maxY, GraphPoint[][] dataPointsIn) {
        super(widthIn, heightIn, maxX, maxY);
        dataPoints = dataPointsIn;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < dataPoints.length; i ++) {
            int colorIndex = i;
            while (colorIndex < COLORS.length) colorIndex -= COLORS.length;
            plot(dataPoints[i], COLORS[colorIndex], g);
        }
    }

    private void plot(GraphPoint[] dataIn, Color colorIn, Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(colorIn);
        for (GraphPoint gp : dataIn) {
            int x = xCoord(gp);
            int y = yCoord(gp);
            int offset = (int) Math.floor(size/2);
            g.fillRect(x-1,y-1,size,size);
        }
        g.setColor(oldColor);
    }

}
