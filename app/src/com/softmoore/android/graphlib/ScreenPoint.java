package com.softmoore.android.graphlib;


/**
 * Point encapsulates a pair of integer values representing a point on the screen of
 * an Android device.  This class is basically equivalent to android.graphics.Point.
 */
class ScreenPoint
  {
    private int x;
    private int y;

    public ScreenPoint(int x, int y)
      {
        this.x = x;
        this.y = y;
      }

    public int getX()
      {
        return x;
      }

    public int getY()
      {
        return y;
      }
  }
