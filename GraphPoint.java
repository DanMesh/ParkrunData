/*
    Graph Point object for Parkrun data collector
    Daniel Mesham
    25 January 2017
*/

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

class GraphPoint {
    private double x, y;

    public GraphPoint(double xIn, double yIn) {
        x = xIn;
        y = yIn;
    }

    public double getX() { return x; }
    public double getY() { return y; }

}
