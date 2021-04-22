package com.softmoore.android.graphlib;

import java.util.Collections;
import java.util.List;

/**
 * An array of points together with a color used to plot them.  This
 * class is used for both plotting points and drawing line graphs.
 */
class GraphPoints
  {
    private List<Point> points;
    private int color;

    GraphPoints(List<Point> points, int color)
      {
        this.points = points;
        this.color = color;
        Collections.sort(points);
      }

    List<Point> getPoints()
      {
        return points;
      }

    int getColor()
      {
        return color;
      }
  }
