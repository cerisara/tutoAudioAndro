package com.softmoore.android.graphlib;


import android.graphics.Color;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * This class contains information about the colors, points, labels, and graphs to be drawn,
 * but it is essentially independent of Android graphics details.  Graph objects are created
 * using the nested static subclass Builder (Builder Pattern).
 */
public class Graph
  {
    // list of functions to graph with their colors
    private final List<GraphFunction> functions;

    // list of lists of graphPoints to plot with their colors
    private final List<GraphPoints> graphPoints;

    // list of line graphs to draw with their colors
    private final List<GraphPoints> lineGraphs;

    // default colors for background, axes, functions, and graph points
    private final int bgColor;
    private final int axesColor;

    // bounds for world coordinates
    private final double xMin, xMax, yMin, yMax;

    // origin for axes; both axes pass through this point
    private final double axisX;
    private final double axisY;

    // place tick marks at these positions on the axes
    private final List<Double> xTicks;
    private final List<Double> yTicks;

    // place labels at these positions on the axes (override tick marks)
    private final List<Label> xLabels;
    private final List<Label> yLabels;

    private Graph(Builder builder)
      {
        functions   = builder.functions;
        graphPoints = builder.graphPoints;
        lineGraphs  = builder.lineGraphs;
        bgColor     = builder.bgColor;
        axesColor   = builder.axesColor;
        xMin        = builder.xMin;
        xMax        = builder.xMax;
        yMin        = builder.yMin;
        yMax        = builder.yMax;
        axisX       = builder.axisX;
        axisY       = builder.axisY;
        xTicks      = builder.xTicks;
        yTicks      = builder.yTicks;
        xLabels     = builder.xLabels;
        yLabels     = builder.yLabels;
      }

    public static class Builder
      {
        // list of graph functions to graph
        private List<GraphFunction> functions = new ArrayList<>(5);

        // list of graph graphPoints to plot
        private List<GraphPoints> graphPoints = new ArrayList<>(5);

        // list of line graphs to draw
        private List<GraphPoints> lineGraphs = new ArrayList<>(5);

        // default colors for background, axes, functions, and graphPoints
        private int bgColor    = Color.WHITE;
        private int axesColor  = Color.BLACK;
        private int functColor = Color.BLACK;
        private int pointColor = Color.BLACK;

        // bounds for world coordinates
        private double xMin = -10;
        private double xMax = 10;
        private double yMin = -10;
        private double yMax = 10;

        // origin for axes; both axes pass through this point
        private double axisX = 0;
        private double axisY = 0;

        // place tick marks at these positions on the axes
        Double defaultTicks[] = { -8.0, -6.0, -4.0, -2.0, 2.0, 4.0, 6.0, 8.0 };
        private List<Double> xTicks = Arrays.asList(defaultTicks);
        private List<Double> yTicks = Arrays.asList(defaultTicks);

        // place labels at these positions on the axes (override tick marks)
        private List<Label> xLabels = new ArrayList<>(10);
        private List<Label> yLabels = new ArrayList<>(10);

        /**
         * Default constructor.
         */
        public Builder()
          {
            super();
          }

        /**
         * Add a function to graph and the color to be used for the graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addFunction(Function function, int graphColor)
          {
            functions.add(new GraphFunction(function, graphColor));
            return this;
          }

        /**
         * Add a function to graph to be graphed using the default color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addFunction(Function function)
          {
            functions.add(new GraphFunction(function, functColor));
            return this;
          }

        /**
         * Add an array of points to be plotted and the color for those points.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addPoints(Point[] points, int pointColor)
          {
            this.graphPoints.add(new GraphPoints(Arrays.asList(points), pointColor));
            return this;
          }

        /**
         * Add a list of points to be plotted and the color for those points.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addPoints(List<Point> points, int pointColor)
          {
            this.graphPoints.add(new GraphPoints(points, pointColor));
            return this;
          }

        /**
         * Add an array of points to be plotted using the default color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addPoints(Point[] points)
          {
            graphPoints.add(new GraphPoints(Arrays.asList(points), pointColor));
            return this;
          }

        /**
         * Add a ;ost of points to be plotted using the default color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addPoints(List<Point> points)
          {
            graphPoints.add(new GraphPoints(points, pointColor));
            return this;
          }

        /**
         * Add an array of points for a line graphs to be drawn and the color for the line graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addLineGraph(Point[] points, int lineGraphColor)
          {
            lineGraphs.add(new GraphPoints(Arrays.asList(points), lineGraphColor));
            return this;
          }

        /**
         * Add a list of points for a line graphs to be drawn and the color for the line graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addLineGraph(List<Point> points, int lineGraphColor)
          {
            lineGraphs.add(new GraphPoints(points, lineGraphColor));
            return this;
          }

        /**
         * Add an array of points for a line graph to be drawn using the default color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addLineGraph(Point[] points)
          {
            this.lineGraphs.add(new GraphPoints(Arrays.asList(points), pointColor));
            return this;
          }

        /**
         * Add a list of points for a line graph to be drawn using the default color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder addLineGraph(List<Point> points)
          {
            this.lineGraphs.add(new GraphPoints(points, pointColor));
            return this;
          }

        /**
         * Set the background color for the graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder setBackgroundColor(int bgColor)
          {
            this.bgColor = bgColor;
            return this;
          }

        /**
         * Set the color to be used for the graph's axes.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder setAxesColor(int axesColor)
          {
            this.axesColor = axesColor;
            return this;
          }

        /**
         * Set the default color to be used for graphing functions.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder setFunctionColor(int functColor)
          {
            this.functColor = functColor;
            return this;
          }

        /**
         * Set the default color to be used for plotting points.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder setPointColor(int pointColor)
          {
            this.pointColor = pointColor;
            return this;
          }

        /**
         * Set the world coordinates (window) for the graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public Builder setWorldCoordinates(double xMin, double xMax, double yMin, double yMax)
          {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            return this;
          }

        /**
         * Set the axes to be drawn for this graph.  Note that the value for
         * x determines the y axis and the value for y determines the x axis.
         */
        public Builder setAxes(double axisX, double axisY)
          {
            this.axisX = axisX;
            this.axisY = axisY;
            return this;
          }

        /**
         * Set the tick marks to be used for the x-axis.
         */
        public Builder setXTicks(double[] xTicks)
          {
            List<Double> tempXTicks = new ArrayList<>(xTicks.length);
            for (double xTick : xTicks)
                tempXTicks.add(xTick);

            this.xTicks = tempXTicks;
            return this;
          }

        /**
         * Set the tick marks to be used for the x-axis.
         */
        public Builder setXTicks(List<Double> xTicks)
          {
            this.xTicks = xTicks;
            return this;
          }

        /**
         * Set the tick marks to be used for the y-axis.
         */
        public Builder setYTicks(double[] yTicks)
          {
            List<Double> tempYTicks = new ArrayList<>(yTicks.length);
            for (double yTick : yTicks)
                tempYTicks.add(yTick);

            this.yTicks = tempYTicks;
            return this;
          }

        /**
         * Set the tick marks to be used for the y-axis.
         */
        public Builder setYTicks(List<Double> yTicks)
          {
            this.yTicks = yTicks;
            return this;
          }

        /**
         * Set the labels to be used for the x-axis.
         */
        public Builder setXLabels(Label[] xLabels)
          {
            this.xLabels = Arrays.asList(xLabels);
            return this;
          }

        /**
         * Set the labels to be used for the x-axis.
         */
        public Builder setXLabels(List<Label> xLabels)
          {
            this.xLabels = xLabels;
            return this;
          }

        /**
         * Set the labels to be used for the y-axis.
         */
        public Builder setYLabels(Label[] yLabels)
          {
            this.yLabels = Arrays.asList(yLabels);
            return this;
          }

        /**
         * Set the labels to be used for the y-axis.
         */
        public Builder setYLabels(List<Label> yLabels)
          {
            this.yLabels = yLabels;
            return this;
          }

        /**
         * Creates a Graph with the arguments supplied to this builder.
         */
        public Graph build()
          {
            return new Graph(this);
          }
      }

    public List<GraphFunction> getFunctions()
      {
        return functions;
      }

    public List<GraphPoints> getGraphPoints()
      {
        return graphPoints;
      }

    public List<GraphPoints> getLineGraphs()
      {
        return lineGraphs;
      }

    public int getBackgroundColor()
      {
        return bgColor;
      }

    public int getAxesColor()
      {
        return axesColor;
      }

    public double getXMin()
      {
        return xMin;
      }

    public double getXMax()
      {
        return xMax;
      }

    public double getYMin()
      {
        return yMin;
      }

    public double getYMax()
      {
        return yMax;
      }

    public double getAxisX()
      {
        return axisX;
      }

    public double getAxisY()
      {
        return axisY;
      }

    public List<Double> getXTicks()
      {
        return xTicks;
      }

    public List<Double> getYTicks()
      {
        return yTicks;
      }

    public List<Label> getXLabels()
      {
        return xLabels;
      }

    public List<Label> getYLabels()
      {
        return yLabels;
      }
  }
