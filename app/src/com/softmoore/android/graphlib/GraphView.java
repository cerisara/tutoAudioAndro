package com.softmoore.android.graphlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.List;

public class GraphView extends View
  {
    private Paint paint;
    private int pointRadius;
    private int tickOffset;
    private int labelOffset;
    private int textSize;

    // the graph to be drawn with all its properties
    private Graph g = new Graph.Builder().build();

    public GraphView(Context context)
      {
        super(context);
        init();
      }

    public GraphView(Context context, AttributeSet attrs)
      {
        super(context, attrs);
        init();
      }

    protected void init()
      {
          System.out.println("GGGGGG init0");
        // adjust point radius, tick offset, text size, and stroke width based on display density
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int densityDpi = metrics.densityDpi;
        int strokeWidth;

        // adjust properties based on screen density
        if (densityDpi <= DisplayMetrics.DENSITY_LOW)
          {
            pointRadius = 3;
            tickOffset  = 3;
            labelOffset = 3;
            textSize    = 7;
            strokeWidth = 1;
          }
        else if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM)
          {
            pointRadius = 3;
            tickOffset  = 4;
            labelOffset = 3;
            textSize    = 8;
            strokeWidth = 1;
          }
        else if (densityDpi <= DisplayMetrics.DENSITY_HIGH)
          {
            pointRadius = 4;
            tickOffset  = 7;
            labelOffset = 5;
            textSize    = 15;
            strokeWidth = 1;
          }
        else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH)
          {
            pointRadius = 6;
            tickOffset  = 7;
            labelOffset = 5;
            textSize    = 20;
            strokeWidth = 2;
          }
        else if (densityDpi <= DisplayMetrics.DENSITY_XXHIGH)
          {
            pointRadius = 8;
            tickOffset  = 9;
            labelOffset = 7;
            textSize    = 30;
            strokeWidth = 2;
          }
        else
          {
            pointRadius = 10;
            tickOffset  = 10;
            labelOffset = 9;
            textSize    = 35;
            strokeWidth = 3;
          }

        // set default values
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
          System.out.println("GGGGGG initf");
      }

    /**
     * Set the graph to be drawn.
     */
    public void setGraph(Graph g)
      {
        this.g = g;
        invalidate();
      }

    /**
     * Converts the world x coordinate to the screen x coordinate
     */
    public int toScreenX(double x)
      {
        double slope = getWidth()/(g.getXMax() - g.getXMin());
        return (int)(slope*(x - g.getXMin()));
      }

    /**
     * Converts the world y coordinate to the screen y coordinate
     */
    public int toScreenY(double y)
      {
        double slope  = getHeight()/(g.getYMin() - g.getYMax());
        return (int)(slope*(y - g.getYMax()));
      }

    /**
     * Converts the screen x coordinate to the world x coordinates.
     */
    public double toWorldX(int x)
      {
        double slope = (g.getXMax() - g.getXMin())/getWidth();
        return slope*x + g.getXMin();
      }

    /**
     * Converts the screen y coordinate to the world y coordinates.
     */
    public double toWorldY(int y)
      {
        double slope = (g.getYMin() - g.getYMax())/getHeight();
        return slope*y + g.getYMax();
      }

    protected void drawViewFrame(Canvas canvas)
      {
        canvas.drawColor(g.getBackgroundColor());
        paint.setColor(g.getAxesColor());
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
      }

    private void drawAxes(Canvas canvas)
      {
        int axisX = toScreenX(g.getAxisX());
        int axisY = toScreenY(g.getAxisY());

        Rect bounds = new Rect();

        paint.setColor(g.getAxesColor());
        paint.setTextSize(textSize);

        // draw x axis with tick marks and labels
        if (isOnScreenY(axisY))
          {
            canvas.drawLine(0, axisY, getWidth(), axisY, paint);

            paint.setTextAlign(Paint.Align.CENTER);

            // draw x labels if they exist; otherwise draw x tick labels
            if (g.getXLabels().size() > 0)
              {
                for (Label label : g.getXLabels())
                  {
                    String xTickLabel = label.getLabel();
                    paint.getTextBounds(xTickLabel, 0, xTickLabel.length(), bounds);

                    int xTickLabelHeight = bounds.height();
                    int screenXTick = toScreenX(label.getTick());
                    if (isOnScreenX(screenXTick))
                      {
                        canvas.drawLine(screenXTick, axisY - tickOffset, screenXTick, axisY + tickOffset, paint);
                        canvas.drawText(xTickLabel, screenXTick, axisY + tickOffset + labelOffset + xTickLabelHeight, paint);
                      }
                  }
              }
            else
              {
                for (Double xTick : g.getXTicks())
                  {
                    String xTickLabel = Double.toString(xTick);
                    if (xTick == Math.rint(xTick))
                        xTickLabel = Long.toString(Math.round(xTick));

                    paint.getTextBounds(xTickLabel, 0, xTickLabel.length(), bounds);
                    int xTickLabelHeight = bounds.height();
                    int screenXTick = toScreenX(xTick);

                    if (isOnScreenX(screenXTick))
                      {
                        canvas.drawLine(screenXTick, axisY - tickOffset, screenXTick, axisY + tickOffset, paint);
                        canvas.drawText(xTickLabel, screenXTick, axisY + tickOffset + labelOffset + xTickLabelHeight, paint);
                      }
                  }
              }
          }

        // draw y axis with tick marks and labels
        if (isOnScreenX(axisX))
          {
            canvas.drawLine(axisX, 0, axisX, getHeight(), paint);

            paint.setTextAlign(Paint.Align.CENTER);

            // draw y labels if they exist; otherwise draw y tick labels
            if (g.getYLabels().size() > 0)
              {
                for (Label label : g.getYLabels())
                  {
                    String yTickLabel = label.getLabel();
                    paint.getTextBounds(yTickLabel, 0, yTickLabel.length(), bounds);

                    int yTickLabelHeight = bounds.height();
                    int yTickLabelWidth  = bounds.width();
                    int screenYTick = toScreenY(label.getTick());
                    if (isOnScreenY(screenYTick))
                      {
                        canvas.drawLine(axisX - tickOffset, screenYTick, axisX + tickOffset, screenYTick, paint);
                        canvas.drawText(yTickLabel, axisX - tickOffset - labelOffset - yTickLabelWidth / 2,
                            screenYTick + yTickLabelHeight / 2, paint);
                      }
                  }
              }
            else
              {
                for (double yTick : g.getYTicks())
                  {
                    String yTickLabel = Double.toString(yTick);
                    if (yTick == Math.rint(yTick))
                        yTickLabel = Long.toString(Math.round(yTick));
                    paint.getTextBounds(yTickLabel, 0, yTickLabel.length(), bounds);

                    int yTickLabelHeight = bounds.height();
                    int yTickLabelWidth  = bounds.width();
                    int screenYTick = toScreenY(yTick);
                    if (isOnScreenY(screenYTick))
                      {
                        canvas.drawLine(axisX - tickOffset, screenYTick, axisX + tickOffset, screenYTick, paint);
                        canvas.drawText(yTickLabel, axisX - tickOffset - labelOffset - yTickLabelWidth / 2,
                            screenYTick + yTickLabelHeight / 2, paint);
                      }
                  }
              }
          }
      }

    private void drawFunctions(Canvas canvas)
      {
        for (GraphFunction graphFunction : g.getFunctions())
            drawFunction(graphFunction, canvas);
      }

    private void drawFunction(GraphFunction graphFunction, Canvas canvas)
      {
        Function function = graphFunction.getFunction();

        List<ScreenPoint> screenPoints = getScreenPointsForFunction(function);

        // use a path of line segments for the graph
        Path path = new Path();

        if (screenPoints.size() > 0)
          {
            // move path to first screen point
            ScreenPoint screenPoint = screenPoints.get(0);
            int screenX = screenPoint.getX();
            path.moveTo(screenPoint.getX(), screenPoint.getY());

            for (int i = 1;  i < screenPoints.size();  ++i)
              {
                screenPoint = screenPoints.get(i);

                if (screenPoint.getX() == screenX + 1)
                    path.lineTo(screenPoint.getX(), screenPoint.getY());
                else
                    path.moveTo(screenPoint.getX(), screenPoint.getY());

                screenX = screenPoint.getX();
              }
          }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(graphFunction.getColor());
        canvas.drawPath(path, paint);
      }

    private void drawGraphPoints(Canvas canvas)
      {
        for (GraphPoints graphPoint : g.getGraphPoints())
            drawPoints(graphPoint, canvas);
      }

    private void drawPoints(GraphPoints graphPoint, Canvas canvas)
      {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(graphPoint.getColor());

        for (Point point : graphPoint.getPoints())
          {
            int screenX = toScreenX(point.getX());
            int screenY = toScreenY(point.getY());

            if (isNearScreenX(screenX) && isNearScreenY(screenY))
                canvas.drawCircle(screenX, screenY, pointRadius, paint);
          }
      }

    private void drawLineGraphs(Canvas canvas)
      {
        for (GraphPoints graphPoints : g.getLineGraphs())
            drawLineGraph(graphPoints, canvas);
      }

    private void drawLineGraph(GraphPoints graphPoints, Canvas canvas)
      {
        // draw each individual point before drawing the line segments
        drawPoints(graphPoints, canvas);

        // use a path for the line segments of the line graph.
        Path path = new Path();

        List<Point> points = graphPoints.getPoints();

        // find first point
        Point point = points.get(0);
        int screenX = toScreenX(point.getX());
        int screenY = toScreenY(point.getY());
        path.moveTo(screenX, screenY);

        // find remaining points
        for (int i = 1; i < points.size(); ++i)
          {
            point = points.get(i);
            screenX = toScreenX(point.getX());
            screenY = toScreenY(point.getY());
            path.lineTo(screenX, screenY);
          }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(graphPoints.getColor());
        canvas.drawPath(path, paint);
      }

    /**
     * Returns the list of screen points for the specified function.
     */
    private List<ScreenPoint> getScreenPointsForFunction(Function f)
      {
        List<ScreenPoint> screenPoints = new ArrayList<>(getWidth() + 2);
        int screenY;

        // construct list of all possible screen points
        for (int screenX = -1; screenX <= getWidth();  ++screenX)
          {
            double x = toWorldX(screenX);
            double y = f.apply(x);

            if (isFinite(y))
              {
                screenY = toScreenY(y);

                if (isNearScreenY(screenY))
                    screenPoints.add(new ScreenPoint(screenX, screenY));
              }
          }

        return screenPoints;
      }

    /**
     * Finds the next screen point starting with the specified x value.
     */
    private ScreenPoint nextPoint(int screenX, Function f)
      {
        int screenY = Integer.MAX_VALUE;   // initialize to an invalid value

        do
          {
            double x = toWorldX(screenX);
            double y = f.apply(x);

            if (isFinite(y))
              {
                screenY = toScreenY(y);

                if (!isNearScreenY(screenY))
                    ++screenX;
              }
            else
                ++screenX;
          }
        while (screenX <= getWidth() && !isNearScreenY(screenY));

        return new ScreenPoint(screenX, screenY);
      }

    /**
     * Returns true when the specified screenX value represents
     * a screenX coordinate for a point on the screen.
     */
    private boolean isOnScreenX(int screenX)
      {
        return screenX >=0 && screenX < getWidth();
      }

    /**
     * Returns true when the specified screenY value represents
     * a screenY coordinate for a point on the screen.
     */
    private boolean isOnScreenY(int screenY)
      {
        return screenY >= 0 && screenY <= getHeight();
      }

    /**
     * Returns true when the specified screenX value represents
     * a screenX coordinate for a point near the screen.
     */
    private boolean isNearScreenX(int screenX)
      {
        return Math.abs(screenX) <= 2*getWidth();
      }

    /**
     * Returns true when the specified screenY value represents
     * a screenY coordinate for a point near the screen.
     */
    private boolean isNearScreenY(int screenY)
      {
        return Math.abs(screenY) <= 2*getHeight();
      }

    /**
     * Replacement method for Double.isFinite(), which was not defined until Java 8.
     */
    private boolean isFinite(double d)
      {
        // as implemented in class java.lang.Double
        return Math.abs(d) <= Double.MAX_VALUE;
      }

    protected void onDraw(Canvas canvas)
      {
        drawViewFrame(canvas);
        drawAxes(canvas);
        drawFunctions(canvas);
        drawGraphPoints(canvas);
        drawLineGraphs(canvas);
      }
  }
