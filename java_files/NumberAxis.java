// 
// Decompiled by Procyon v0.5.36
// 

package org.jfree.chart.axis;

import clinitrewriter.Clinit;
import org.jfree.util.ObjectUtils;
import java.awt.font.LineMetrics;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.util.Locale;
import java.text.DecimalFormat;
import java.awt.Shape;
import java.util.Iterator;
import java.awt.geom.Line2D;
import org.jfree.ui.RefineryUtilities;
import java.awt.Insets;
import java.awt.Graphics2D;
import org.jfree.ui.RectangleEdge;
import java.awt.geom.Rectangle2D;
import org.jfree.data.Range;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.event.AxisChangeEvent;
import java.text.NumberFormat;
import java.io.Serializable;

public class NumberAxis extends ValueAxis implements Cloneable, Serializable
{
    public static final boolean DEFAULT_AUTO_RANGE_INCLUDES_ZERO = true;
    public static final boolean DEFAULT_AUTO_RANGE_STICKY_ZERO = true;
    public static final NumberTickUnit DEFAULT_TICK_UNIT;
    public static final boolean DEFAULT_VERTICAL_TICK_LABELS = false;
    private boolean autoRangeIncludesZero;
    private boolean autoRangeStickyZero;
    private NumberTickUnit tickUnit;
    private NumberFormat numberFormatOverride;
    private MarkerAxisBand markerBand;
    
    public NumberAxis() {
        this(null);
    }
    
    public NumberAxis(final String s) {
        super(s, createStandardTickUnits());
        this.autoRangeIncludesZero = true;
        this.autoRangeStickyZero = true;
        this.tickUnit = NumberAxis.DEFAULT_TICK_UNIT;
        this.numberFormatOverride = null;
        this.markerBand = null;
    }
    
    public boolean autoRangeIncludesZero() {
        return this.autoRangeIncludesZero;
    }
    
    public void setAutoRangeIncludesZero(final boolean autoRangeIncludesZero) {
        if (this.autoRangeIncludesZero != autoRangeIncludesZero) {
            this.autoRangeIncludesZero = autoRangeIncludesZero;
            if (this.isAutoRange()) {
                this.autoAdjustRange();
            }
            this.notifyListeners(new AxisChangeEvent((Axis)this));
        }
    }
    
    public boolean autoRangeStickyZero() {
        return this.autoRangeStickyZero;
    }
    
    public void setAutoRangeStickyZero(final boolean autoRangeStickyZero) {
        if (this.autoRangeStickyZero != autoRangeStickyZero) {
            this.autoRangeStickyZero = autoRangeStickyZero;
            if (this.isAutoRange()) {
                this.autoAdjustRange();
            }
            this.notifyListeners(new AxisChangeEvent((Axis)this));
        }
    }
    
    public NumberTickUnit getTickUnit() {
        return this.tickUnit;
    }
    
    public void setTickUnit(final NumberTickUnit numberTickUnit) {
        this.setTickUnit(numberTickUnit, true, true);
    }
    
    public void setTickUnit(final NumberTickUnit tickUnit, final boolean b, final boolean b2) {
        this.tickUnit = tickUnit;
        if (b2) {
            this.setAutoTickUnitSelection(false, false);
        }
        if (b) {
            this.notifyListeners(new AxisChangeEvent((Axis)this));
        }
    }
    
    public NumberFormat getNumberFormatOverride() {
        return this.numberFormatOverride;
    }
    
    public void setNumberFormatOverride(final NumberFormat numberFormatOverride) {
        this.numberFormatOverride = numberFormatOverride;
        this.notifyListeners(new AxisChangeEvent((Axis)this));
    }
    
    public MarkerAxisBand getMarkerBand() {
        return this.markerBand;
    }
    
    public void setMarkerBand(final MarkerAxisBand markerBand) {
        this.markerBand = markerBand;
        this.notifyListeners(new AxisChangeEvent((Axis)this));
    }
    
    protected boolean isCompatiblePlot(final Plot plot) {
        return plot instanceof ValueAxisPlot;
    }
    
    public void configure() {
        if (this.isAutoRange()) {
            this.autoAdjustRange();
        }
    }
    
    protected void autoAdjustRange() {
        final Plot plot = this.getPlot();
        if (plot == null) {
            return;
        }
        if (plot instanceof ValueAxisPlot) {
            Range dataRange = ((ValueAxisPlot)plot).getDataRange((ValueAxis)this);
            if (dataRange == null) {
                dataRange = new Range(0.0, 1.0);
            }
            double n = dataRange.getUpperBound();
            double lowerBound = dataRange.getLowerBound();
            final double n2 = n - lowerBound;
            final double fixedAutoRange = this.getFixedAutoRange();
            double n3;
            if (fixedAutoRange > 0.0) {
                n3 = n - fixedAutoRange;
            }
            else {
                final double autoRangeMinimumSize = this.getAutoRangeMinimumSize();
                if (n2 < autoRangeMinimumSize) {
                    final double n4 = (autoRangeMinimumSize - n2) / 2.0;
                    n += n4;
                    lowerBound -= n4;
                }
                if (this.autoRangeIncludesZero()) {
                    if (this.autoRangeStickyZero()) {
                        if (n <= 0.0) {
                            n = 0.0;
                        }
                        else {
                            n += this.getUpperMargin() * n2;
                        }
                        if (lowerBound >= 0.0) {
                            n3 = 0.0;
                        }
                        else {
                            n3 = lowerBound - this.getLowerMargin() * n2;
                        }
                    }
                    else {
                        n = Math.max(0.0, n + this.getUpperMargin() * n2);
                        n3 = Math.min(0.0, lowerBound - this.getLowerMargin() * n2);
                    }
                }
                else if (this.autoRangeStickyZero()) {
                    if (n <= 0.0) {
                        n = Math.min(0.0, n + this.getUpperMargin() * n2);
                    }
                    else {
                        n += this.getUpperMargin() * n2;
                    }
                    if (lowerBound >= 0.0) {
                        n3 = Math.max(0.0, lowerBound - this.getLowerMargin() * n2);
                    }
                    else {
                        n3 = lowerBound - this.getLowerMargin() * n2;
                    }
                }
                else {
                    n += this.getUpperMargin() * n2;
                    n3 = lowerBound - this.getLowerMargin() * n2;
                }
            }
            this.setRange(new Range(n3, n), false, false);
        }
    }
    
    public double translateValueToJava2D(final double n, final Rectangle2D rectangle2D, final RectangleEdge rectangleEdge) {
        final Range range = this.getRange();
        final double lowerBound = range.getLowerBound();
        final double upperBound = range.getUpperBound();
        double n2 = 0.0;
        double n3 = 0.0;
        if (RectangleEdge.isTopOrBottom(rectangleEdge)) {
            n2 = rectangle2D.getX();
            n3 = rectangle2D.getMaxX();
        }
        else if (RectangleEdge.isLeftOrRight(rectangleEdge)) {
            n3 = rectangle2D.getMinY();
            n2 = rectangle2D.getMaxY();
        }
        if (this.isInverted()) {
            return n3 - (n - lowerBound) / (upperBound - lowerBound) * (n3 - n2);
        }
        return n2 + (n - lowerBound) / (upperBound - lowerBound) * (n3 - n2);
    }
    
    public double translateJava2DtoValue(final float n, final Rectangle2D rectangle2D, final RectangleEdge rectangleEdge) {
        final Range range = this.getRange();
        final double lowerBound = range.getLowerBound();
        final double upperBound = range.getUpperBound();
        double n2 = 0.0;
        double n3 = 0.0;
        if (RectangleEdge.isTopOrBottom(rectangleEdge)) {
            n2 = rectangle2D.getX();
            n3 = rectangle2D.getMaxX();
        }
        else if (RectangleEdge.isLeftOrRight(rectangleEdge)) {
            n2 = rectangle2D.getMaxY();
            n3 = rectangle2D.getY();
        }
        if (this.isInverted()) {
            return upperBound - (n - n2) / (n3 - n2) * (upperBound - lowerBound);
        }
        return lowerBound + (n - n2) / (n3 - n2) * (upperBound - lowerBound);
    }
    
    public double calculateLowestVisibleTickValue() {
        final double size = this.getTickUnit().getSize();
        return Math.ceil(this.getRange().getLowerBound() / size) * size;
    }
    
    public double calculateHighestVisibleTickValue() {
        final double size = this.getTickUnit().getSize();
        return Math.floor(this.getRange().getUpperBound() / size) * size;
    }
    
    public int calculateVisibleTickCount() {
        final double size = this.getTickUnit().getSize();
        final Range range = this.getRange();
        return (int)(Math.floor(range.getUpperBound() / size) - Math.ceil(range.getLowerBound() / size) + 1.0);
    }
    
    public AxisSpace reserveSpace(final Graphics2D graphics2D, final Plot plot, final Rectangle2D rectangle2D, final RectangleEdge rectangleEdge, AxisSpace axisSpace) {
        if (axisSpace == null) {
            axisSpace = new AxisSpace();
        }
        this.reservedForAxisLabel = 0.0;
        this.reservedForTickLabels = 0.0;
        if (!this.isVisible()) {
            return axisSpace;
        }
        final double fixedDimension = this.getFixedDimension();
        if (fixedDimension > 0.0) {
            axisSpace.ensureAtLeast(fixedDimension, rectangleEdge);
        }
        double reservedForTickLabels = 0.0;
        double reservedForTickLabels2 = 0.0;
        if (this.isTickLabelsVisible()) {
            graphics2D.setFont(this.getTickLabelFont());
            this.refreshTicks(graphics2D, 0.0, rectangle2D, rectangle2D, rectangleEdge);
            final Insets tickLabelInsets = this.getTickLabelInsets();
            if (RectangleEdge.isTopOrBottom(rectangleEdge)) {
                reservedForTickLabels = tickLabelInsets.top + tickLabelInsets.bottom + this.getMaxTickLabelHeight(graphics2D, rectangle2D, this.isVerticalTickLabels());
                this.reservedForTickLabels = reservedForTickLabels;
            }
            else if (RectangleEdge.isLeftOrRight(rectangleEdge)) {
                reservedForTickLabels2 = tickLabelInsets.left + tickLabelInsets.right + this.getMaxTickLabelWidth(graphics2D, rectangle2D);
                this.reservedForTickLabels = reservedForTickLabels2;
            }
        }
        double height = 0.0;
        if (this.markerBand != null) {
            height = this.markerBand.getHeight(graphics2D);
        }
        final Rectangle2D labelEnclosure = this.getLabelEnclosure(graphics2D, rectangleEdge);
        if (RectangleEdge.isTopOrBottom(rectangleEdge)) {
            axisSpace.add((this.reservedForAxisLabel = labelEnclosure.getHeight()) + reservedForTickLabels + height, rectangleEdge);
        }
        else if (RectangleEdge.isLeftOrRight(rectangleEdge)) {
            axisSpace.add((this.reservedForAxisLabel = labelEnclosure.getWidth()) + reservedForTickLabels2, rectangleEdge);
        }
        return axisSpace;
    }
    
    public double draw(final Graphics2D graphics2D, double n, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        if (!this.isVisible()) {
            this.refreshTicks(graphics2D, n, rectangle2D, rectangle2D2, rectangleEdge);
            return 0.0;
        }
        final double drawTickMarksAndLabels = this.drawTickMarksAndLabels(graphics2D, n, rectangle2D, rectangle2D2, rectangleEdge);
        if (rectangleEdge == RectangleEdge.TOP || rectangleEdge == RectangleEdge.LEFT) {
            n -= drawTickMarksAndLabels;
        }
        else if (rectangleEdge == RectangleEdge.BOTTOM || rectangleEdge == RectangleEdge.RIGHT) {
            n += drawTickMarksAndLabels;
        }
        return drawTickMarksAndLabels + this.drawLabel(this.getLabel(), graphics2D, n, rectangle2D, rectangle2D2, rectangleEdge);
    }
    
    protected double drawTickMarksAndLabels(final Graphics2D graphics2D, final double n, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        if (this.isAxisLineVisible()) {
            this.drawAxisLine(graphics2D, n, rectangle2D2, rectangleEdge);
        }
        final double n2 = this.getTickMarkOutsideLength();
        final double n3 = this.getTickMarkInsideLength();
        this.refreshTicks(graphics2D, n, rectangle2D, rectangle2D2, rectangleEdge);
        graphics2D.setFont(this.getTickLabelFont());
        for (final Tick tick : this.getTicks()) {
            final float n4 = (float)this.translateValueToJava2D(tick.getNumericalValue(), rectangle2D2, rectangleEdge);
            if (this.isTickLabelsVisible()) {
                graphics2D.setPaint(this.getTickLabelPaint());
                if (this.isVerticalTickLabels()) {
                    RefineryUtilities.drawRotatedString(tick.getText(), graphics2D, tick.getX(), tick.getY(), -1.5707963267948966);
                }
                else {
                    graphics2D.drawString(tick.getText(), tick.getX(), tick.getY());
                }
            }
            if (this.isTickMarksVisible()) {
                Shape shape = null;
                graphics2D.setStroke(this.getTickMarkStroke());
                graphics2D.setPaint(this.getTickMarkPaint());
                if (rectangleEdge == RectangleEdge.LEFT) {
                    shape = new Line2D.Double(n - n2, n4, n + n3, n4);
                }
                else if (rectangleEdge == RectangleEdge.RIGHT) {
                    shape = new Line2D.Double(n + n2, n4, n - n3, n4);
                }
                else if (rectangleEdge == RectangleEdge.TOP) {
                    shape = new Line2D.Double(n4, n - n2, n4, n + n3);
                }
                else if (rectangleEdge == RectangleEdge.BOTTOM) {
                    shape = new Line2D.Double(n4, n + n2, n4, n - n3);
                }
                graphics2D.draw(shape);
            }
        }
        return this.reservedForTickLabels;
    }
    
    public static TickUnits createStandardTickUnits() {
        final TickUnits tickUnits = new TickUnits();
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-7, (NumberFormat)new DecimalFormat("0.0000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-6, (NumberFormat)new DecimalFormat("0.000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-5, (NumberFormat)new DecimalFormat("0.00000")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-4, (NumberFormat)new DecimalFormat("0.0000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.001, (NumberFormat)new DecimalFormat("0.000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.01, (NumberFormat)new DecimalFormat("0.00")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.1, (NumberFormat)new DecimalFormat("0.0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(10.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(100.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(10000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(100000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1000000.0, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E7, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E8, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E9, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E10, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E11, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-7, (NumberFormat)new DecimalFormat("0.00000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-6, (NumberFormat)new DecimalFormat("0.0000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-5, (NumberFormat)new DecimalFormat("0.000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-4, (NumberFormat)new DecimalFormat("0.00000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.0025, (NumberFormat)new DecimalFormat("0.0000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.025, (NumberFormat)new DecimalFormat("0.000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.25, (NumberFormat)new DecimalFormat("0.00")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5, (NumberFormat)new DecimalFormat("0.0")));
        tickUnits.add((TickUnit)new NumberTickUnit(25.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(250.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2500.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(25000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(250000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2500000.0, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E7, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E8, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E9, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E10, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E11, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-7, (NumberFormat)new DecimalFormat("0.0000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-6, (NumberFormat)new DecimalFormat("0.000000")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-5, (NumberFormat)new DecimalFormat("0.00000")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-4, (NumberFormat)new DecimalFormat("0.0000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.005, (NumberFormat)new DecimalFormat("0.000")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.05, (NumberFormat)new DecimalFormat("0.00")));
        tickUnits.add((TickUnit)new NumberTickUnit(0.5, (NumberFormat)new DecimalFormat("0.0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(50.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(500.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(50000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(500000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5000000.0, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E7, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E8, (NumberFormat)new DecimalFormat("#,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E9, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E10, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E11, (NumberFormat)new DecimalFormat("#,###,###,##0")));
        return tickUnits;
    }
    
    public static TickUnits createIntegerTickUnits() {
        final TickUnits tickUnits = new TickUnits();
        tickUnits.add((TickUnit)new NumberTickUnit(1.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(10.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(20.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(50.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(100.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(200.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(500.0, (NumberFormat)new DecimalFormat("0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(10000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(20000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(50000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(100000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(200000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(500000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1000000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2000000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5000000.0, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E7, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0E7, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E7, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E8, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0E8, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E8, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E9, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0E9, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E9, (NumberFormat)new DecimalFormat("#,##0")));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E10, (NumberFormat)new DecimalFormat("#,##0")));
        return tickUnits;
    }
    
    public static TickUnits createStandardTickUnits(final Locale inLocale) {
        final TickUnits tickUnits = new TickUnits();
        final NumberFormat numberInstance = NumberFormat.getNumberInstance(inLocale);
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-6, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-5, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E-4, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.001, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.01, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.1, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(10.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(100.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(10000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(100000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1000000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E8, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E9, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E10, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-6, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-5, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E-4, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.0025, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.025, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.25, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(25.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(250.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2500.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(25000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(250000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2500000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E8, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E9, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.5E10, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-6, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-5, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E-4, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.005, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.05, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(0.5, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(50.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(500.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(50000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(500000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5000000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E8, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E9, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E10, numberInstance));
        return tickUnits;
    }
    
    public static TickUnits createIntegerTickUnits(final Locale inLocale) {
        final TickUnits tickUnits = new TickUnits();
        final NumberFormat numberInstance = NumberFormat.getNumberInstance(inLocale);
        tickUnits.add((TickUnit)new NumberTickUnit(1.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(10.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(20.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(50.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(100.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(200.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(500.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(10000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(20000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(50000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(100000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(200000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(500000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1000000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2000000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5000000.0, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0E7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E7, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E8, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0E8, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E8, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E9, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(2.0E9, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(5.0E9, numberInstance));
        tickUnits.add((TickUnit)new NumberTickUnit(1.0E10, numberInstance));
        return tickUnits;
    }
    
    protected double getMaxTickLabelHeight(final Graphics2D graphics2D, final Rectangle2D rectangle2D, final boolean b) {
        final Font tickLabelFont = this.getTickLabelFont();
        graphics2D.setFont(tickLabelFont);
        final FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        double width = 0.0;
        if (b) {
            final Iterator<Tick> iterator = this.getTicks().iterator();
            while (iterator.hasNext()) {
                final Rectangle2D stringBounds = tickLabelFont.getStringBounds(iterator.next().getText(), fontRenderContext);
                if (stringBounds.getWidth() > width) {
                    width = stringBounds.getWidth();
                }
            }
        }
        else {
            width = tickLabelFont.getLineMetrics("Sample", fontRenderContext).getHeight();
        }
        return width;
    }
    
    protected double estimateMaximumTickLabelHeight(final Graphics2D graphics2D) {
        final Insets tickLabelInsets = this.getTickLabelInsets();
        return tickLabelInsets.top + tickLabelInsets.bottom + (double)this.getTickLabelFont().getLineMetrics("123", graphics2D.getFontRenderContext()).getHeight();
    }
    
    protected double estimateMaximumTickLabelWidth(final Graphics2D graphics2D, final TickUnit tickUnit) {
        final Insets tickLabelInsets = this.getTickLabelInsets();
        final double n = tickLabelInsets.left + tickLabelInsets.right;
        final Font tickLabelFont = this.getTickLabelFont();
        final FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        double n2;
        if (this.isVerticalTickLabels()) {
            n2 = n + tickLabelFont.getStringBounds("0", fontRenderContext).getHeight();
        }
        else {
            final Range range = this.getRange();
            n2 = n + Math.max(tickLabelFont.getStringBounds(tickUnit.valueToString(range.getLowerBound()), fontRenderContext).getWidth(), tickLabelFont.getStringBounds(tickUnit.valueToString(range.getUpperBound()), fontRenderContext).getWidth());
        }
        return n2;
    }
    
    protected void selectAutoTickUnit(final Graphics2D graphics2D, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        if (RectangleEdge.isTopOrBottom(rectangleEdge)) {
            this.selectHorizontalAutoTickUnit(graphics2D, rectangle2D, rectangle2D2, rectangleEdge);
        }
        else if (RectangleEdge.isLeftOrRight(rectangleEdge)) {
            this.selectVerticalAutoTickUnit(graphics2D, rectangle2D, rectangle2D2, rectangleEdge);
        }
    }
    
    protected void selectHorizontalAutoTickUnit(final Graphics2D graphics2D, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        final double translateValueToJava2D = this.translateValueToJava2D(0.0, rectangle2D2, rectangleEdge);
        final double estimateMaximumTickLabelWidth = this.estimateMaximumTickLabelWidth(graphics2D, (TickUnit)this.getTickUnit());
        final TickUnits standardTickUnits = this.getStandardTickUnits();
        final TickUnit ceilingTickUnit = standardTickUnits.getCeilingTickUnit((TickUnit)this.getTickUnit());
        NumberTickUnit numberTickUnit = (NumberTickUnit)standardTickUnits.getCeilingTickUnit(estimateMaximumTickLabelWidth / Math.abs(this.translateValueToJava2D(ceilingTickUnit.getSize(), rectangle2D2, rectangleEdge) - translateValueToJava2D) * ceilingTickUnit.getSize());
        if (this.estimateMaximumTickLabelWidth(graphics2D, (TickUnit)numberTickUnit) > Math.abs(this.translateValueToJava2D(numberTickUnit.getSize(), rectangle2D2, rectangleEdge) - translateValueToJava2D)) {
            numberTickUnit = (NumberTickUnit)standardTickUnits.getLargerTickUnit((TickUnit)numberTickUnit);
        }
        this.setTickUnit(numberTickUnit, false, false);
    }
    
    protected void selectVerticalAutoTickUnit(final Graphics2D graphics2D, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        final double translateValueToJava2D = this.translateValueToJava2D(0.0, rectangle2D2, rectangleEdge);
        final double estimateMaximumTickLabelHeight = this.estimateMaximumTickLabelHeight(graphics2D);
        final TickUnits standardTickUnits = this.getStandardTickUnits();
        final TickUnit ceilingTickUnit = standardTickUnits.getCeilingTickUnit((TickUnit)this.getTickUnit());
        NumberTickUnit numberTickUnit = (NumberTickUnit)standardTickUnits.getCeilingTickUnit(estimateMaximumTickLabelHeight / Math.abs(this.translateValueToJava2D(ceilingTickUnit.getSize(), rectangle2D2, rectangleEdge) - translateValueToJava2D) * ceilingTickUnit.getSize());
        if (this.estimateMaximumTickLabelHeight(graphics2D) > Math.abs(this.translateValueToJava2D(numberTickUnit.getSize(), rectangle2D2, rectangleEdge) - translateValueToJava2D)) {
            numberTickUnit = (NumberTickUnit)standardTickUnits.getLargerTickUnit((TickUnit)numberTickUnit);
        }
        this.setTickUnit(numberTickUnit, false, false);
    }
    
    public void refreshTicks(final Graphics2D graphics2D, final double n, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        if (RectangleEdge.isTopOrBottom(rectangleEdge)) {
            this.refreshHorizontalTicks(graphics2D, n, rectangle2D, rectangle2D2, rectangleEdge);
        }
        else if (RectangleEdge.isLeftOrRight(rectangleEdge)) {
            this.refreshVerticalTicks(graphics2D, n, rectangle2D, rectangle2D2, rectangleEdge);
        }
    }
    
    protected void refreshHorizontalTicks(final Graphics2D graphics2D, final double n, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        this.getTicks().clear();
        final Font tickLabelFont = this.getTickLabelFont();
        graphics2D.setFont(tickLabelFont);
        final FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        if (this.isAutoTickUnitSelection()) {
            this.selectAutoTickUnit(graphics2D, rectangle2D, rectangle2D2, rectangleEdge);
        }
        final double size = this.getTickUnit().getSize();
        final int calculateVisibleTickCount = this.calculateVisibleTickCount();
        final double calculateLowestVisibleTickValue = this.calculateLowestVisibleTickValue();
        if (calculateVisibleTickCount <= 500) {
            for (int i = 0; i < calculateVisibleTickCount; ++i) {
                final double n2 = calculateLowestVisibleTickValue + i * size;
                final double translateValueToJava2D = this.translateValueToJava2D(n2, rectangle2D2, rectangleEdge);
                final NumberFormat numberFormatOverride = this.getNumberFormatOverride();
                String s;
                if (numberFormatOverride != null) {
                    s = numberFormatOverride.format(n2);
                }
                else {
                    s = this.getTickUnit().valueToString(n2);
                }
                final Rectangle2D stringBounds = tickLabelFont.getStringBounds(s, fontRenderContext);
                final LineMetrics lineMetrics = tickLabelFont.getLineMetrics(s, fontRenderContext);
                final Insets tickLabelInsets = this.getTickLabelInsets();
                float n3;
                float n4;
                if (this.isVerticalTickLabels()) {
                    n3 = (float)(translateValueToJava2D + stringBounds.getHeight() / 2.0);
                    if (rectangleEdge == RectangleEdge.TOP) {
                        n4 = (float)(n - tickLabelInsets.bottom - stringBounds.getWidth());
                    }
                    else {
                        n4 = (float)(n + tickLabelInsets.top + stringBounds.getWidth());
                    }
                }
                else {
                    n3 = (float)(translateValueToJava2D - stringBounds.getWidth() / 2.0);
                    if (rectangleEdge == RectangleEdge.TOP) {
                        n4 = (float)(n - tickLabelInsets.bottom - lineMetrics.getLeading() - lineMetrics.getDescent());
                    }
                    else {
                        n4 = (float)(n + tickLabelInsets.top + stringBounds.getHeight());
                    }
                }
                this.getTicks().add(new Tick((Object)new Double(n2), s, n3, n4));
            }
        }
    }
    
    protected void refreshVerticalTicks(final Graphics2D graphics2D, final double n, final Rectangle2D rectangle2D, final Rectangle2D rectangle2D2, final RectangleEdge rectangleEdge) {
        this.getTicks().clear();
        final Font tickLabelFont = this.getTickLabelFont();
        graphics2D.setFont(tickLabelFont);
        if (this.isAutoTickUnitSelection()) {
            this.selectAutoTickUnit(graphics2D, rectangle2D, rectangle2D2, rectangleEdge);
        }
        final double size = this.getTickUnit().getSize();
        final int calculateVisibleTickCount = this.calculateVisibleTickCount();
        final double calculateLowestVisibleTickValue = this.calculateLowestVisibleTickValue();
        if (calculateVisibleTickCount <= 500) {
            for (int i = 0; i < calculateVisibleTickCount; ++i) {
                final double n2 = calculateLowestVisibleTickValue + i * size;
                final double translateValueToJava2D = this.translateValueToJava2D(n2, rectangle2D2, rectangleEdge);
                final NumberFormat numberFormatOverride = this.getNumberFormatOverride();
                String s;
                if (numberFormatOverride != null) {
                    s = numberFormatOverride.format(n2);
                }
                else {
                    s = this.getTickUnit().valueToString(n2);
                }
                final FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
                final Rectangle2D stringBounds = tickLabelFont.getStringBounds(s, fontRenderContext);
                final LineMetrics lineMetrics = tickLabelFont.getLineMetrics(s, fontRenderContext);
                float n3;
                if (rectangleEdge == RectangleEdge.LEFT) {
                    n3 = (float)(n - stringBounds.getWidth() - this.getTickLabelInsets().right);
                }
                else {
                    n3 = (float)(n + this.getTickLabelInsets().left);
                }
                this.getTicks().add(new Tick((Object)new Double(n2), s, n3, (float)(translateValueToJava2D + lineMetrics.getAscent() / 2.0f)));
            }
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final NumberAxis numberAxis = (NumberAxis)super.clone();
        if (this.numberFormatOverride != null) {
            numberAxis.numberFormatOverride = (NumberFormat)this.numberFormatOverride.clone();
        }
        return numberAxis;
    }
    
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof NumberAxis && super.equals(o)) {
            final NumberAxis numberAxis = (NumberAxis)o;
            final boolean b = this.autoRangeIncludesZero == numberAxis.autoRangeIncludesZero;
            final boolean b2 = this.autoRangeStickyZero == numberAxis.autoRangeStickyZero;
            final boolean equal = ObjectUtils.equal((Object)this.tickUnit, (Object)numberAxis.tickUnit);
            final boolean equal2 = ObjectUtils.equal((Object)this.numberFormatOverride, (Object)numberAxis.numberFormatOverride);
            return b && b2 && equal && equal2;
        }
        return false;
    }
    
    static {
        XXXmyClinitXXX();
    }
    
    public static void XXXmyClinitXXX() {
        Clinit.logClinit("org/jfree/chart/axis/NumberAxis");
        DEFAULT_TICK_UNIT = new NumberTickUnit(1.0, (NumberFormat)new DecimalFormat("0"));
    }
}
