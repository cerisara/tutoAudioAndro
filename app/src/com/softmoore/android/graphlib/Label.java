package com.softmoore.android.graphlib;

/**
 * Label encapsulates a double value and a string, where the double value
 * represents a point on an axis and the string is used to label that point.
 */
public class Label
  {
    private double tick;
    private String label;

    public Label(double tick, String label)
      {
        this.tick = tick;
        this.label = label;
      }

    public double getTick()
      {
        return tick;
      }

    public String getLabel()
      {
        return label;
      }
  }
