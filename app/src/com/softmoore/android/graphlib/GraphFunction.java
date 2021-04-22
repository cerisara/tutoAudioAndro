package com.softmoore.android.graphlib;

/**
 * A graph function is a function together with a color used to graph it.
 */
class GraphFunction
  {
    private Function f;
    private int color;

    GraphFunction(Function f, int color)
      {
        this.f = f;
        this.color = color;
      }

    Function getFunction()
      {
        return f;
      }

    public int getColor()
      {
        return color;
      }
  }
