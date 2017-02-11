/*
    Bar Graph Panel object for Parkrun data collector
    Daniel Mesham
    28 January 2017
*/

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

class BarGraphPanel extends GraphPanel {

    private GraphBar[] dataBars;
    private Color color = Color.RED;

    public BarGraphPanel(int widthIn, int heightIn, double maxX, double maxY, GraphBar[] dataBarsIn) {
        super(widthIn, heightIn, maxX, maxY);
        dataBars = dataBarsIn;
    }

    //@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        plot(dataBars, color, g);
    }

    private void plot(GraphBar[] dataIn, Color colorIn, Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(colorIn);
        for (GraphBar gb : dataIn) {
            int width = lengthToX(gb.getWidth());
            int height = lengthToY(gb.getHeight());
            int x = valToX(gb.getX());
            int y = valToY(0) - height;
            g.fillRect(x,y,width,height);
        }
        g.setColor(oldColor);
    }
}
