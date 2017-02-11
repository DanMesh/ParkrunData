/*
    Graph Bar object for Parkrun data collector
    Daniel Mesham
    28 January 2017
*/

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

class GraphBar {

    private double x, width, height;

    public GraphBar(double xIn, double widthIn, double heightIn) {
        x = xIn;
        width = widthIn;
        height = heightIn;
    }

    public double getX() { return x; }

    public double getWidth() { return width; }

    public double getHeight() { return height; }

    public void incrementHeight() { height++; }

}
