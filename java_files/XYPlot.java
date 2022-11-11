// 
// Decompiled by Procyon v0.5.36
// 

package org.jfree.chart.plot;

import clinitrewriter.Clinit;
import java.io.ObjectInputStream;
import java.io.IOException;
import org.jfree.io.SerialUtilities;
import java.io.ObjectOutputStream;
import org.jfree.util.PaintUtilities;
import org.jfree.util.ObjectUtilities;
import org.jfree.chart.LegendItem;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.data.Range;
import java.awt.geom.Line2D;
import java.util.Collections;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.chart.axis.AxisCollection;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.axis.AxisState;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Shape;
import org.jfree.chart.axis.Axis;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.event.MarkerChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.jfree.ui.Layer;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.ui.RectangleEdge;
import org.jfree.chart.event.PlotChangeEvent;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.data.general.DatasetChangeListener;
import java.util.TreeMap;
import java.util.HashMap;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisSpace;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.Map;
import org.jfree.util.ObjectList;
import org.jfree.ui.RectangleInsets;
import java.util.ResourceBundle;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.Serializable;
import org.jfree.util.PublicCloneable;
import org.jfree.chart.event.RendererChangeListener;

public class XYPlot extends Plot implements ValueAxisPlot, Zoomable, RendererChangeListener, Cloneable, PublicCloneable, Serializable
{
    private static final long serialVersionUID = 7044148245716569264L;
    public static final Stroke DEFAULT_GRIDLINE_STROKE;
    public static final Paint DEFAULT_GRIDLINE_PAINT;
    public static final boolean DEFAULT_CROSSHAIR_VISIBLE = false;
    public static final Stroke DEFAULT_CROSSHAIR_STROKE;
    public static final Paint DEFAULT_CROSSHAIR_PAINT;
    protected static ResourceBundle localizationResources;
    private PlotOrientation orientation;
    private RectangleInsets axisOffset;
    private ObjectList domainAxes;
    private ObjectList domainAxisLocations;
    private ObjectList rangeAxes;
    private ObjectList rangeAxisLocations;
    private ObjectList datasets;
    private ObjectList renderers;
    private Map datasetToDomainAxisMap;
    private Map datasetToRangeAxisMap;
    private transient Point2D quadrantOrigin;
    private transient Paint[] quadrantPaint;
    private boolean domainGridlinesVisible;
    private transient Stroke domainGridlineStroke;
    private transient Paint domainGridlinePaint;
    private boolean rangeGridlinesVisible;
    private transient Stroke rangeGridlineStroke;
    private transient Paint rangeGridlinePaint;
    private boolean domainZeroBaselineVisible;
    private transient Stroke domainZeroBaselineStroke;
    private transient Paint domainZeroBaselinePaint;
    private boolean rangeZeroBaselineVisible;
    private transient Stroke rangeZeroBaselineStroke;
    private transient Paint rangeZeroBaselinePaint;
    private boolean domainCrosshairVisible;
    private double domainCrosshairValue;
    private transient Stroke domainCrosshairStroke;
    private transient Paint domainCrosshairPaint;
    private boolean domainCrosshairLockedOnData;
    private boolean rangeCrosshairVisible;
    private double rangeCrosshairValue;
    private transient Stroke rangeCrosshairStroke;
    private transient Paint rangeCrosshairPaint;
    private boolean rangeCrosshairLockedOnData;
    private Map foregroundDomainMarkers;
    private Map backgroundDomainMarkers;
    private Map foregroundRangeMarkers;
    private Map backgroundRangeMarkers;
    private List annotations;
    private transient Paint domainTickBandPaint;
    private transient Paint rangeTickBandPaint;
    private AxisSpace fixedDomainAxisSpace;
    private AxisSpace fixedRangeAxisSpace;
    private DatasetRenderingOrder datasetRenderingOrder;
    private SeriesRenderingOrder seriesRenderingOrder;
    private int weight;
    private LegendItemCollection fixedLegendItems;
    
    public XYPlot() {
        this(null, null, null, null);
    }
    
    public XYPlot(final XYDataset dataset, final ValueAxis domainAxis, final ValueAxis rangeAxis, final XYItemRenderer renderer) {
        this.quadrantOrigin = new Point2D.Double(0.0, 0.0);
        this.quadrantPaint = new Paint[] { null, null, null, null };
        this.domainCrosshairLockedOnData = true;
        this.rangeCrosshairLockedOnData = true;
        this.datasetRenderingOrder = DatasetRenderingOrder.REVERSE;
        this.seriesRenderingOrder = SeriesRenderingOrder.REVERSE;
        this.orientation = PlotOrientation.VERTICAL;
        this.weight = 1;
        this.axisOffset = RectangleInsets.ZERO_INSETS;
        this.domainAxes = new ObjectList();
        this.domainAxisLocations = new ObjectList();
        this.foregroundDomainMarkers = new HashMap();
        this.backgroundDomainMarkers = new HashMap();
        this.rangeAxes = new ObjectList();
        this.rangeAxisLocations = new ObjectList();
        this.foregroundRangeMarkers = new HashMap();
        this.backgroundRangeMarkers = new HashMap();
        this.datasets = new ObjectList();
        this.renderers = new ObjectList();
        this.datasetToDomainAxisMap = new TreeMap();
        this.datasetToRangeAxisMap = new TreeMap();
        this.datasets.set(0, (Object)dataset);
        if (dataset != null) {
            dataset.addChangeListener((DatasetChangeListener)this);
        }
        this.renderers.set(0, (Object)renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener((RendererChangeListener)this);
        }
        this.domainAxes.set(0, (Object)domainAxis);
        this.mapDatasetToDomainAxis(0, 0);
        if (domainAxis != null) {
            domainAxis.setPlot((Plot)this);
            domainAxis.addChangeListener((AxisChangeListener)this);
        }
        this.domainAxisLocations.set(0, (Object)AxisLocation.BOTTOM_OR_LEFT);
        this.rangeAxes.set(0, (Object)rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot((Plot)this);
            rangeAxis.addChangeListener((AxisChangeListener)this);
        }
        this.rangeAxisLocations.set(0, (Object)AxisLocation.BOTTOM_OR_LEFT);
        this.configureDomainAxes();
        this.configureRangeAxes();
        this.domainGridlinesVisible = true;
        this.domainGridlineStroke = XYPlot.DEFAULT_GRIDLINE_STROKE;
        this.domainGridlinePaint = XYPlot.DEFAULT_GRIDLINE_PAINT;
        this.domainZeroBaselineVisible = false;
        this.domainZeroBaselinePaint = Color.black;
        this.domainZeroBaselineStroke = new BasicStroke(0.5f);
        this.rangeGridlinesVisible = true;
        this.rangeGridlineStroke = XYPlot.DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinePaint = XYPlot.DEFAULT_GRIDLINE_PAINT;
        this.rangeZeroBaselineVisible = false;
        this.rangeZeroBaselinePaint = Color.black;
        this.rangeZeroBaselineStroke = new BasicStroke(0.5f);
        this.domainCrosshairVisible = false;
        this.domainCrosshairValue = 0.0;
        this.domainCrosshairStroke = XYPlot.DEFAULT_CROSSHAIR_STROKE;
        this.domainCrosshairPaint = XYPlot.DEFAULT_CROSSHAIR_PAINT;
        this.rangeCrosshairVisible = false;
        this.rangeCrosshairValue = 0.0;
        this.rangeCrosshairStroke = XYPlot.DEFAULT_CROSSHAIR_STROKE;
        this.rangeCrosshairPaint = XYPlot.DEFAULT_CROSSHAIR_PAINT;
        this.annotations = new ArrayList();
    }
    
    public String getPlotType() {
        return XYPlot.localizationResources.getString("XY_Plot");
    }
    
    public PlotOrientation getOrientation() {
        return this.orientation;
    }
    
    public void setOrientation(final PlotOrientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        if (orientation != this.orientation) {
            this.orientation = orientation;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public RectangleInsets getAxisOffset() {
        return this.axisOffset;
    }
    
    public void setAxisOffset(final RectangleInsets offset) {
        if (offset == null) {
            throw new IllegalArgumentException("Null 'offset' argument.");
        }
        this.axisOffset = offset;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public ValueAxis getDomainAxis() {
        return this.getDomainAxis(0);
    }
    
    public ValueAxis getDomainAxis(final int index) {
        ValueAxis result = null;
        if (index < this.domainAxes.size()) {
            result = (ValueAxis)this.domainAxes.get(index);
        }
        if (result == null) {
            final Plot parent = this.getParent();
            if (parent instanceof XYPlot) {
                final XYPlot xy = (XYPlot)parent;
                result = xy.getDomainAxis(index);
            }
        }
        return result;
    }
    
    public void setDomainAxis(final ValueAxis axis) {
        this.setDomainAxis(0, axis);
    }
    
    public void setDomainAxis(final int index, final ValueAxis axis) {
        this.setDomainAxis(index, axis, true);
    }
    
    public void setDomainAxis(final int index, final ValueAxis axis, final boolean notify) {
        final ValueAxis existing = this.getDomainAxis(index);
        if (existing != null) {
            existing.removeChangeListener((AxisChangeListener)this);
        }
        if (axis != null) {
            axis.setPlot((Plot)this);
        }
        this.domainAxes.set(index, (Object)axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener((AxisChangeListener)this);
        }
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public void setDomainAxes(final ValueAxis[] axes) {
        for (int i = 0; i < axes.length; ++i) {
            this.setDomainAxis(i, axes[i], false);
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public AxisLocation getDomainAxisLocation() {
        return (AxisLocation)this.domainAxisLocations.get(0);
    }
    
    public void setDomainAxisLocation(final AxisLocation location) {
        this.setDomainAxisLocation(0, location, true);
    }
    
    public void setDomainAxisLocation(final AxisLocation location, final boolean notify) {
        this.setDomainAxisLocation(0, location, notify);
    }
    
    public RectangleEdge getDomainAxisEdge() {
        return Plot.resolveDomainAxisLocation(this.getDomainAxisLocation(), this.orientation);
    }
    
    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }
    
    public void clearDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            final ValueAxis axis = (ValueAxis)this.domainAxes.get(i);
            if (axis != null) {
                axis.removeChangeListener((AxisChangeListener)this);
            }
        }
        this.domainAxes.clear();
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void configureDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            final ValueAxis axis = (ValueAxis)this.domainAxes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
    }
    
    public AxisLocation getDomainAxisLocation(final int index) {
        AxisLocation result = null;
        if (index < this.domainAxisLocations.size()) {
            result = (AxisLocation)this.domainAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(this.getDomainAxisLocation());
        }
        return result;
    }
    
    public void setDomainAxisLocation(final int index, final AxisLocation location) {
        this.setDomainAxisLocation(index, location, true);
    }
    
    public void setDomainAxisLocation(final int index, final AxisLocation location, final boolean notify) {
        if (index == 0 && location == null) {
            throw new IllegalArgumentException("Null 'location' for index 0 not permitted.");
        }
        this.domainAxisLocations.set(index, (Object)location);
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public RectangleEdge getDomainAxisEdge(final int index) {
        final AxisLocation location = this.getDomainAxisLocation(index);
        RectangleEdge result = Plot.resolveDomainAxisLocation(location, this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(this.getDomainAxisEdge());
        }
        return result;
    }
    
    public ValueAxis getRangeAxis() {
        return this.getRangeAxis(0);
    }
    
    public void setRangeAxis(final ValueAxis axis) {
        if (axis != null) {
            axis.setPlot((Plot)this);
        }
        final ValueAxis existing = this.getRangeAxis();
        if (existing != null) {
            existing.removeChangeListener((AxisChangeListener)this);
        }
        this.rangeAxes.set(0, (Object)axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener((AxisChangeListener)this);
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public AxisLocation getRangeAxisLocation() {
        return (AxisLocation)this.rangeAxisLocations.get(0);
    }
    
    public void setRangeAxisLocation(final AxisLocation location) {
        this.setRangeAxisLocation(0, location, true);
    }
    
    public void setRangeAxisLocation(final AxisLocation location, final boolean notify) {
        this.setRangeAxisLocation(0, location, notify);
    }
    
    public RectangleEdge getRangeAxisEdge() {
        return Plot.resolveRangeAxisLocation(this.getRangeAxisLocation(), this.orientation);
    }
    
    public ValueAxis getRangeAxis(final int index) {
        ValueAxis result = null;
        if (index < this.rangeAxes.size()) {
            result = (ValueAxis)this.rangeAxes.get(index);
        }
        if (result == null) {
            final Plot parent = this.getParent();
            if (parent instanceof XYPlot) {
                final XYPlot xy = (XYPlot)parent;
                result = xy.getRangeAxis(index);
            }
        }
        return result;
    }
    
    public void setRangeAxis(final int index, final ValueAxis axis) {
        this.setRangeAxis(index, axis, true);
    }
    
    public void setRangeAxis(final int index, final ValueAxis axis, final boolean notify) {
        final ValueAxis existing = this.getRangeAxis(index);
        if (existing != null) {
            existing.removeChangeListener((AxisChangeListener)this);
        }
        if (axis != null) {
            axis.setPlot((Plot)this);
        }
        this.rangeAxes.set(index, (Object)axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener((AxisChangeListener)this);
        }
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public void setRangeAxes(final ValueAxis[] axes) {
        for (int i = 0; i < axes.length; ++i) {
            this.setRangeAxis(i, axes[i], false);
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public int getRangeAxisCount() {
        return this.rangeAxes.size();
    }
    
    public void clearRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            final ValueAxis axis = (ValueAxis)this.rangeAxes.get(i);
            if (axis != null) {
                axis.removeChangeListener((AxisChangeListener)this);
            }
        }
        this.rangeAxes.clear();
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void configureRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            final ValueAxis axis = (ValueAxis)this.rangeAxes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
    }
    
    public AxisLocation getRangeAxisLocation(final int index) {
        AxisLocation result = null;
        if (index < this.rangeAxisLocations.size()) {
            result = (AxisLocation)this.rangeAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(this.getRangeAxisLocation());
        }
        return result;
    }
    
    public void setRangeAxisLocation(final int index, final AxisLocation location) {
        this.setRangeAxisLocation(index, location, true);
    }
    
    public void setRangeAxisLocation(final int index, final AxisLocation location, final boolean notify) {
        if (index == 0 && location == null) {
            throw new IllegalArgumentException("Null 'location' for index 0 not permitted.");
        }
        this.rangeAxisLocations.set(index, (Object)location);
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public RectangleEdge getRangeAxisEdge(final int index) {
        final AxisLocation location = this.getRangeAxisLocation(index);
        RectangleEdge result = Plot.resolveRangeAxisLocation(location, this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(this.getRangeAxisEdge());
        }
        return result;
    }
    
    public XYDataset getDataset() {
        return this.getDataset(0);
    }
    
    public XYDataset getDataset(final int index) {
        XYDataset result = null;
        if (this.datasets.size() > index) {
            result = (XYDataset)this.datasets.get(index);
        }
        return result;
    }
    
    public void setDataset(final XYDataset dataset) {
        this.setDataset(0, dataset);
    }
    
    public void setDataset(final int index, final XYDataset dataset) {
        final XYDataset existing = this.getDataset(index);
        if (existing != null) {
            existing.removeChangeListener((DatasetChangeListener)this);
        }
        this.datasets.set(index, (Object)dataset);
        if (dataset != null) {
            dataset.addChangeListener((DatasetChangeListener)this);
        }
        final DatasetChangeEvent event = new DatasetChangeEvent((Object)this, (Dataset)dataset);
        this.datasetChanged(event);
    }
    
    public int getDatasetCount() {
        return this.datasets.size();
    }
    
    public int indexOf(final XYDataset dataset) {
        int result = -1;
        for (int i = 0; i < this.datasets.size(); ++i) {
            if (dataset == this.datasets.get(i)) {
                result = i;
                break;
            }
        }
        return result;
    }
    
    public void mapDatasetToDomainAxis(final int index, final int axisIndex) {
        this.datasetToDomainAxisMap.put(new Integer(index), new Integer(axisIndex));
        this.datasetChanged(new DatasetChangeEvent((Object)this, (Dataset)this.getDataset(index)));
    }
    
    public void mapDatasetToRangeAxis(final int index, final int axisIndex) {
        this.datasetToRangeAxisMap.put(new Integer(index), new Integer(axisIndex));
        this.datasetChanged(new DatasetChangeEvent((Object)this, (Dataset)this.getDataset(index)));
    }
    
    public XYItemRenderer getRenderer() {
        return this.getRenderer(0);
    }
    
    public XYItemRenderer getRenderer(final int index) {
        XYItemRenderer result = null;
        if (this.renderers.size() > index) {
            result = (XYItemRenderer)this.renderers.get(index);
        }
        return result;
    }
    
    public void setRenderer(final XYItemRenderer renderer) {
        this.setRenderer(0, renderer);
    }
    
    public void setRenderer(final int index, final XYItemRenderer renderer) {
        this.setRenderer(index, renderer, true);
    }
    
    public void setRenderer(final int index, final XYItemRenderer renderer, final boolean notify) {
        final XYItemRenderer existing = this.getRenderer(index);
        if (existing != null) {
            existing.removeChangeListener((RendererChangeListener)this);
        }
        this.renderers.set(index, (Object)renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener((RendererChangeListener)this);
        }
        this.configureDomainAxes();
        this.configureRangeAxes();
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public void setRenderers(final XYItemRenderer[] renderers) {
        for (int i = 0; i < renderers.length; ++i) {
            this.setRenderer(i, renderers[i], false);
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.datasetRenderingOrder;
    }
    
    public void setDatasetRenderingOrder(final DatasetRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.datasetRenderingOrder = order;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public SeriesRenderingOrder getSeriesRenderingOrder() {
        return this.seriesRenderingOrder;
    }
    
    public void setSeriesRenderingOrder(final SeriesRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.seriesRenderingOrder = order;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public int getIndexOf(final XYItemRenderer renderer) {
        return this.renderers.indexOf((Object)renderer);
    }
    
    public XYItemRenderer getRendererForDataset(final XYDataset dataset) {
        XYItemRenderer result = null;
        int i = 0;
        while (i < this.datasets.size()) {
            if (this.datasets.get(i) == dataset) {
                result = (XYItemRenderer)this.renderers.get(i);
                if (result == null) {
                    result = this.getRenderer();
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return result;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public void setWeight(final int weight) {
        this.weight = weight;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }
    
    public void setDomainGridlinesVisible(final boolean visible) {
        if (this.domainGridlinesVisible != visible) {
            this.domainGridlinesVisible = visible;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }
    
    public void setDomainGridlineStroke(final Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainGridlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }
    
    public void setDomainGridlinePaint(final Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }
    
    public void setRangeGridlinesVisible(final boolean visible) {
        if (this.rangeGridlinesVisible != visible) {
            this.rangeGridlinesVisible = visible;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public Stroke getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }
    
    public void setRangeGridlineStroke(final Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeGridlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }
    
    public void setRangeGridlinePaint(final Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean isDomainZeroBaselineVisible() {
        return this.domainZeroBaselineVisible;
    }
    
    public void setDomainZeroBaselineVisible(final boolean visible) {
        this.domainZeroBaselineVisible = visible;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Stroke getDomainZeroBaselineStroke() {
        return this.domainZeroBaselineStroke;
    }
    
    public void setDomainZeroBaselineStroke(final Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainZeroBaselineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getDomainZeroBaselinePaint() {
        return this.domainZeroBaselinePaint;
    }
    
    public void setDomainZeroBaselinePaint(final Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainZeroBaselinePaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean isRangeZeroBaselineVisible() {
        return this.rangeZeroBaselineVisible;
    }
    
    public void setRangeZeroBaselineVisible(final boolean visible) {
        this.rangeZeroBaselineVisible = visible;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Stroke getRangeZeroBaselineStroke() {
        return this.rangeZeroBaselineStroke;
    }
    
    public void setRangeZeroBaselineStroke(final Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeZeroBaselineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getRangeZeroBaselinePaint() {
        return this.rangeZeroBaselinePaint;
    }
    
    public void setRangeZeroBaselinePaint(final Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeZeroBaselinePaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getDomainTickBandPaint() {
        return this.domainTickBandPaint;
    }
    
    public void setDomainTickBandPaint(final Paint paint) {
        this.domainTickBandPaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getRangeTickBandPaint() {
        return this.rangeTickBandPaint;
    }
    
    public void setRangeTickBandPaint(final Paint paint) {
        this.rangeTickBandPaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Point2D getQuadrantOrigin() {
        return this.quadrantOrigin;
    }
    
    public void setQuadrantOrigin(final Point2D origin) {
        if (origin == null) {
            throw new IllegalArgumentException("Null 'origin' argument.");
        }
        this.quadrantOrigin = origin;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getQuadrantPaint(final int index) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("The index value (" + index + ") should be in the range 0 to 3.");
        }
        return this.quadrantPaint[index];
    }
    
    public void setQuadrantPaint(final int index, final Paint paint) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("The index value (" + index + ") should be in the range 0 to 3.");
        }
        this.quadrantPaint[index] = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void addDomainMarker(final Marker marker) {
        this.addDomainMarker(marker, Layer.FOREGROUND);
    }
    
    public void addDomainMarker(final Marker marker, final Layer layer) {
        this.addDomainMarker(0, marker, layer);
    }
    
    public void clearDomainMarkers() {
        if (this.backgroundDomainMarkers != null) {
            final Set keys = this.backgroundDomainMarkers.keySet();
            for (final Integer key : keys) {
                this.clearDomainMarkers(key);
            }
            this.backgroundDomainMarkers.clear();
        }
        if (this.foregroundDomainMarkers != null) {
            final Set keys = this.foregroundDomainMarkers.keySet();
            for (final Integer key : keys) {
                this.clearDomainMarkers(key);
            }
            this.foregroundDomainMarkers.clear();
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void clearDomainMarkers(final int index) {
        final Integer key = new Integer(index);
        if (this.backgroundDomainMarkers != null) {
            final Collection markers = this.backgroundDomainMarkers.get(key);
            if (markers != null) {
                for (final Marker m : markers) {
                    m.removeChangeListener((MarkerChangeListener)this);
                }
                markers.clear();
            }
        }
        if (this.foregroundRangeMarkers != null) {
            final Collection markers = this.foregroundDomainMarkers.get(key);
            if (markers != null) {
                for (final Marker m : markers) {
                    m.removeChangeListener((MarkerChangeListener)this);
                }
                markers.clear();
            }
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void addDomainMarker(final int index, final Marker marker, final Layer layer) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' not permitted.");
        }
        if (layer == null) {
            throw new IllegalArgumentException("Null 'layer' not permitted.");
        }
        if (layer == Layer.FOREGROUND) {
            Collection markers = this.foregroundDomainMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            Collection markers = this.backgroundDomainMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener((MarkerChangeListener)this);
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean removeDomainMarker(final Marker marker) {
        return this.removeDomainMarker(marker, Layer.FOREGROUND);
    }
    
    public boolean removeDomainMarker(final Marker marker, final Layer layer) {
        return this.removeDomainMarker(0, marker, layer);
    }
    
    public boolean removeDomainMarker(final int index, final Marker marker, final Layer layer) {
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = this.foregroundDomainMarkers.get(new Integer(index));
        }
        else {
            markers = this.backgroundDomainMarkers.get(new Integer(index));
        }
        final boolean removed = markers.remove(marker);
        if (removed) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
        return removed;
    }
    
    public void addRangeMarker(final Marker marker) {
        this.addRangeMarker(marker, Layer.FOREGROUND);
    }
    
    public void addRangeMarker(final Marker marker, final Layer layer) {
        this.addRangeMarker(0, marker, layer);
    }
    
    public void clearRangeMarkers() {
        if (this.backgroundRangeMarkers != null) {
            final Set keys = this.backgroundRangeMarkers.keySet();
            for (final Integer key : keys) {
                this.clearRangeMarkers(key);
            }
            this.backgroundRangeMarkers.clear();
        }
        if (this.foregroundRangeMarkers != null) {
            final Set keys = this.foregroundRangeMarkers.keySet();
            for (final Integer key : keys) {
                this.clearRangeMarkers(key);
            }
            this.foregroundRangeMarkers.clear();
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void addRangeMarker(final int index, final Marker marker, final Layer layer) {
        if (layer == Layer.FOREGROUND) {
            Collection markers = this.foregroundRangeMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList();
                this.foregroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            Collection markers = this.backgroundRangeMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList();
                this.backgroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener((MarkerChangeListener)this);
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public void clearRangeMarkers(final int index) {
        final Integer key = new Integer(index);
        if (this.backgroundRangeMarkers != null) {
            final Collection markers = this.backgroundRangeMarkers.get(key);
            if (markers != null) {
                for (final Marker m : markers) {
                    m.removeChangeListener((MarkerChangeListener)this);
                }
                markers.clear();
            }
        }
        if (this.foregroundRangeMarkers != null) {
            final Collection markers = this.foregroundRangeMarkers.get(key);
            if (markers != null) {
                for (final Marker m : markers) {
                    m.removeChangeListener((MarkerChangeListener)this);
                }
                markers.clear();
            }
        }
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean removeRangeMarker(final Marker marker) {
        return this.removeRangeMarker(marker, Layer.FOREGROUND);
    }
    
    public boolean removeRangeMarker(final Marker marker, final Layer layer) {
        return this.removeRangeMarker(0, marker, layer);
    }
    
    public boolean removeRangeMarker(final int index, final Marker marker, final Layer layer) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' argument.");
        }
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = this.foregroundRangeMarkers.get(new Integer(index));
        }
        else {
            markers = this.backgroundRangeMarkers.get(new Integer(index));
        }
        final boolean removed = markers.remove(marker);
        if (removed) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
        return removed;
    }
    
    public void addAnnotation(final XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean removeAnnotation(final XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        final boolean removed = this.annotations.remove(annotation);
        if (removed) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
        return removed;
    }
    
    public List getAnnotations() {
        return new ArrayList(this.annotations);
    }
    
    public void clearAnnotations() {
        this.annotations.clear();
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    protected AxisSpace calculateAxisSpace(final Graphics2D g2, final Rectangle2D plotArea) {
        AxisSpace space = new AxisSpace();
        space = this.calculateDomainAxisSpace(g2, plotArea, space);
        space = this.calculateRangeAxisSpace(g2, plotArea, space);
        return space;
    }
    
    protected AxisSpace calculateDomainAxisSpace(final Graphics2D g2, final Rectangle2D plotArea, AxisSpace space) {
        if (space == null) {
            space = new AxisSpace();
        }
        if (this.fixedDomainAxisSpace != null) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getLeft(), RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getRight(), RectangleEdge.RIGHT);
            }
            else if (this.orientation == PlotOrientation.VERTICAL) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getTop(), RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getBottom(), RectangleEdge.BOTTOM);
            }
        }
        else {
            for (int i = 0; i < this.domainAxes.size(); ++i) {
                final Axis axis = (Axis)this.domainAxes.get(i);
                if (axis != null) {
                    final RectangleEdge edge = this.getDomainAxisEdge(i);
                    space = axis.reserveSpace(g2, (Plot)this, plotArea, edge, space);
                }
            }
        }
        return space;
    }
    
    protected AxisSpace calculateRangeAxisSpace(final Graphics2D g2, final Rectangle2D plotArea, AxisSpace space) {
        if (space == null) {
            space = new AxisSpace();
        }
        if (this.fixedRangeAxisSpace != null) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getTop(), RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getBottom(), RectangleEdge.BOTTOM);
            }
            else if (this.orientation == PlotOrientation.VERTICAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getLeft(), RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getRight(), RectangleEdge.RIGHT);
            }
        }
        else {
            for (int i = 0; i < this.rangeAxes.size(); ++i) {
                final Axis axis = (Axis)this.rangeAxes.get(i);
                if (axis != null) {
                    final RectangleEdge edge = this.getRangeAxisEdge(i);
                    space = axis.reserveSpace(g2, (Plot)this, plotArea, edge, space);
                }
            }
        }
        return space;
    }
    
    public void draw(final Graphics2D g2, final Rectangle2D area, Point2D anchor, final PlotState parentState, final PlotRenderingInfo info) {
        final boolean b1 = area.getWidth() <= 10.0;
        final boolean b2 = area.getHeight() <= 10.0;
        if (b1 || b2) {
            return;
        }
        if (info != null) {
            info.setPlotArea(area);
        }
        final RectangleInsets insets = this.getInsets();
        insets.trim(area);
        final AxisSpace space = this.calculateAxisSpace(g2, area);
        final Rectangle2D dataArea = space.shrink(area, (Rectangle2D)null);
        this.axisOffset.trim(dataArea);
        if (info != null) {
            info.setDataArea(dataArea);
        }
        this.drawBackground(g2, dataArea);
        final Map axisStateMap = this.drawAxes(g2, area, dataArea, info);
        final PlotOrientation orient = this.getOrientation();
        if (anchor != null && !dataArea.contains(anchor)) {
            anchor = null;
        }
        final CrosshairState crosshairState = new CrosshairState();
        crosshairState.setCrosshairDistance(Double.POSITIVE_INFINITY);
        crosshairState.setAnchor(anchor);
        crosshairState.setAnchorX(Double.NaN);
        crosshairState.setAnchorY(Double.NaN);
        if (anchor != null) {
            final ValueAxis domainAxis = this.getDomainAxis();
            if (domainAxis != null) {
                double x;
                if (orient == PlotOrientation.VERTICAL) {
                    x = domainAxis.java2DToValue(anchor.getX(), dataArea, this.getDomainAxisEdge());
                }
                else {
                    x = domainAxis.java2DToValue(anchor.getY(), dataArea, this.getDomainAxisEdge());
                }
                crosshairState.setAnchorX(x);
            }
            final ValueAxis rangeAxis = this.getRangeAxis();
            if (rangeAxis != null) {
                double y;
                if (orient == PlotOrientation.VERTICAL) {
                    y = rangeAxis.java2DToValue(anchor.getY(), dataArea, this.getRangeAxisEdge());
                }
                else {
                    y = rangeAxis.java2DToValue(anchor.getX(), dataArea, this.getRangeAxisEdge());
                }
                crosshairState.setAnchorY(y);
            }
        }
        crosshairState.setCrosshairX(this.getDomainCrosshairValue());
        crosshairState.setCrosshairY(this.getRangeCrosshairValue());
        final Shape originalClip = g2.getClip();
        final Composite originalComposite = g2.getComposite();
        g2.clip(dataArea);
        g2.setComposite(AlphaComposite.getInstance(3, this.getForegroundAlpha()));
        AxisState domainAxisState = axisStateMap.get(this.getDomainAxis());
        if (domainAxisState == null && parentState != null) {
            domainAxisState = parentState.getSharedAxisStates().get(this.getDomainAxis());
        }
        AxisState rangeAxisState = axisStateMap.get(this.getRangeAxis());
        if (rangeAxisState == null && parentState != null) {
            rangeAxisState = parentState.getSharedAxisStates().get(this.getRangeAxis());
        }
        if (domainAxisState != null) {
            this.drawDomainTickBands(g2, dataArea, domainAxisState.getTicks());
        }
        if (rangeAxisState != null) {
            this.drawRangeTickBands(g2, dataArea, rangeAxisState.getTicks());
        }
        if (domainAxisState != null) {
            this.drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
            this.drawZeroDomainBaseline(g2, dataArea);
        }
        if (rangeAxisState != null) {
            this.drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
            this.drawZeroRangeBaseline(g2, dataArea);
        }
        for (int i = 0; i < this.renderers.size(); ++i) {
            this.drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        for (int i = 0; i < this.renderers.size(); ++i) {
            this.drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        boolean foundData = false;
        final DatasetRenderingOrder order = this.getDatasetRenderingOrder();
        if (order == DatasetRenderingOrder.FORWARD) {
            final int rendererCount = this.renderers.size();
            for (int j = 0; j < rendererCount; ++j) {
                final XYItemRenderer r = this.getRenderer(j);
                if (r != null) {
                    final ValueAxis domainAxis2 = this.getDomainAxisForDataset(j);
                    final ValueAxis rangeAxis2 = this.getRangeAxisForDataset(j);
                    r.drawAnnotations(g2, dataArea, domainAxis2, rangeAxis2, Layer.BACKGROUND, info);
                }
            }
            for (int j = 0; j < this.getDatasetCount(); ++j) {
                foundData = (this.render(g2, dataArea, j, info, crosshairState) || foundData);
            }
            for (int j = 0; j < rendererCount; ++j) {
                final XYItemRenderer r = this.getRenderer(j);
                if (r != null) {
                    final ValueAxis domainAxis2 = this.getDomainAxisForDataset(j);
                    final ValueAxis rangeAxis2 = this.getRangeAxisForDataset(j);
                    r.drawAnnotations(g2, dataArea, domainAxis2, rangeAxis2, Layer.FOREGROUND, info);
                }
            }
        }
        else if (order == DatasetRenderingOrder.REVERSE) {
            final int rendererCount = this.renderers.size();
            for (int j = rendererCount - 1; j >= 0; --j) {
                final XYItemRenderer r = this.getRenderer(j);
                if (j < this.getDatasetCount()) {
                    if (r != null) {
                        final ValueAxis domainAxis2 = this.getDomainAxisForDataset(j);
                        final ValueAxis rangeAxis2 = this.getRangeAxisForDataset(j);
                        r.drawAnnotations(g2, dataArea, domainAxis2, rangeAxis2, Layer.BACKGROUND, info);
                    }
                }
            }
            for (int j = this.getDatasetCount() - 1; j >= 0; --j) {
                foundData = (this.render(g2, dataArea, j, info, crosshairState) || foundData);
            }
            for (int j = rendererCount - 1; j >= 0; --j) {
                final XYItemRenderer r = this.getRenderer(j);
                if (j < this.getDatasetCount()) {
                    if (r != null) {
                        final ValueAxis domainAxis2 = this.getDomainAxisForDataset(j);
                        final ValueAxis rangeAxis2 = this.getRangeAxisForDataset(j);
                        r.drawAnnotations(g2, dataArea, domainAxis2, rangeAxis2, Layer.FOREGROUND, info);
                    }
                }
            }
        }
        final int xAxisIndex = crosshairState.getDomainAxisIndex();
        final ValueAxis xAxis = this.getDomainAxis(xAxisIndex);
        final RectangleEdge xAxisEdge = this.getDomainAxisEdge(xAxisIndex);
        if (!this.domainCrosshairLockedOnData && anchor != null) {
            double xx;
            if (orient == PlotOrientation.VERTICAL) {
                xx = xAxis.java2DToValue(anchor.getX(), dataArea, xAxisEdge);
            }
            else {
                xx = xAxis.java2DToValue(anchor.getY(), dataArea, xAxisEdge);
            }
            crosshairState.setCrosshairX(xx);
        }
        this.setDomainCrosshairValue(crosshairState.getCrosshairX(), false);
        if (this.isDomainCrosshairVisible()) {
            final double x2 = this.getDomainCrosshairValue();
            final Paint paint = this.getDomainCrosshairPaint();
            final Stroke stroke = this.getDomainCrosshairStroke();
            this.drawDomainCrosshair(g2, dataArea, orient, x2, xAxis, stroke, paint);
        }
        final int yAxisIndex = crosshairState.getRangeAxisIndex();
        final ValueAxis yAxis = this.getRangeAxis(yAxisIndex);
        final RectangleEdge yAxisEdge = this.getRangeAxisEdge(yAxisIndex);
        if (!this.rangeCrosshairLockedOnData && anchor != null) {
            double yy;
            if (orient == PlotOrientation.VERTICAL) {
                yy = yAxis.java2DToValue(anchor.getY(), dataArea, yAxisEdge);
            }
            else {
                yy = yAxis.java2DToValue(anchor.getX(), dataArea, yAxisEdge);
            }
            crosshairState.setCrosshairY(yy);
        }
        this.setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
        if (this.isRangeCrosshairVisible()) {
            final double y2 = this.getRangeCrosshairValue();
            final Paint paint2 = this.getRangeCrosshairPaint();
            final Stroke stroke2 = this.getRangeCrosshairStroke();
            this.drawRangeCrosshair(g2, dataArea, orient, y2, yAxis, stroke2, paint2);
        }
        if (!foundData) {
            this.drawNoDataMessage(g2, dataArea);
        }
        for (int k = 0; k < this.renderers.size(); ++k) {
            this.drawDomainMarkers(g2, dataArea, k, Layer.FOREGROUND);
        }
        for (int k = 0; k < this.renderers.size(); ++k) {
            this.drawRangeMarkers(g2, dataArea, k, Layer.FOREGROUND);
        }
        this.drawAnnotations(g2, dataArea, info);
        g2.setClip(originalClip);
        g2.setComposite(originalComposite);
        this.drawOutline(g2, dataArea);
    }
    
    public void drawBackground(final Graphics2D g2, final Rectangle2D area) {
        this.fillBackground(g2, area, this.orientation);
        this.drawQuadrants(g2, area);
        this.drawBackgroundImage(g2, area);
    }
    
    protected void drawQuadrants(final Graphics2D g2, final Rectangle2D area) {
        boolean somethingToDraw = false;
        final ValueAxis xAxis = this.getDomainAxis();
        final double x = xAxis.getRange().constrain(this.quadrantOrigin.getX());
        final double xx = xAxis.valueToJava2D(x, area, this.getDomainAxisEdge());
        final ValueAxis yAxis = this.getRangeAxis();
        final double y = yAxis.getRange().constrain(this.quadrantOrigin.getY());
        final double yy = yAxis.valueToJava2D(y, area, this.getRangeAxisEdge());
        final double xmin = xAxis.getLowerBound();
        final double xxmin = xAxis.valueToJava2D(xmin, area, this.getDomainAxisEdge());
        final double xmax = xAxis.getUpperBound();
        final double xxmax = xAxis.valueToJava2D(xmax, area, this.getDomainAxisEdge());
        final double ymin = yAxis.getLowerBound();
        final double yymin = yAxis.valueToJava2D(ymin, area, this.getRangeAxisEdge());
        final double ymax = yAxis.getUpperBound();
        final double yymax = yAxis.valueToJava2D(ymax, area, this.getRangeAxisEdge());
        final Rectangle2D[] r = { null, null, null, null };
        if (this.quadrantPaint[0] != null && x > xmin && y < ymax) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                r[0] = new Rectangle2D.Double(Math.min(yymax, yy), Math.min(xxmin, xx), Math.abs(yy - yymax), Math.abs(xx - xxmin));
            }
            else {
                r[0] = new Rectangle2D.Double(Math.min(xxmin, xx), Math.min(yymax, yy), Math.abs(xx - xxmin), Math.abs(yy - yymax));
            }
            somethingToDraw = true;
        }
        if (this.quadrantPaint[1] != null && x < xmax && y < ymax) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                r[1] = new Rectangle2D.Double(Math.min(yymax, yy), Math.min(xxmax, xx), Math.abs(yy - yymax), Math.abs(xx - xxmax));
            }
            else {
                r[1] = new Rectangle2D.Double(Math.min(xx, xxmax), Math.min(yymax, yy), Math.abs(xx - xxmax), Math.abs(yy - yymax));
            }
            somethingToDraw = true;
        }
        if (this.quadrantPaint[2] != null && x > xmin && y > ymin) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                r[2] = new Rectangle2D.Double(Math.min(yymin, yy), Math.min(xxmin, xx), Math.abs(yy - yymin), Math.abs(xx - xxmin));
            }
            else {
                r[2] = new Rectangle2D.Double(Math.min(xxmin, xx), Math.min(yymin, yy), Math.abs(xx - xxmin), Math.abs(yy - yymin));
            }
            somethingToDraw = true;
        }
        if (this.quadrantPaint[3] != null && x < xmax && y > ymin) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                r[3] = new Rectangle2D.Double(Math.min(yymin, yy), Math.min(xxmax, xx), Math.abs(yy - yymin), Math.abs(xx - xxmax));
            }
            else {
                r[3] = new Rectangle2D.Double(Math.min(xx, xxmax), Math.min(yymin, yy), Math.abs(xx - xxmax), Math.abs(yy - yymin));
            }
            somethingToDraw = true;
        }
        if (somethingToDraw) {
            final Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(3, this.getBackgroundAlpha()));
            for (int i = 0; i < 4; ++i) {
                if (this.quadrantPaint[i] != null && r[i] != null) {
                    g2.setPaint(this.quadrantPaint[i]);
                    g2.fill(r[i]);
                }
            }
            g2.setComposite(originalComposite);
        }
    }
    
    public void drawDomainTickBands(final Graphics2D g2, final Rectangle2D dataArea, final List ticks) {
        final Paint bandPaint = this.getDomainTickBandPaint();
        if (bandPaint != null) {
            boolean fillBand = false;
            final ValueAxis xAxis = this.getDomainAxis();
            double previous = xAxis.getLowerBound();
            for (final ValueTick tick : ticks) {
                final double current = tick.getValue();
                if (fillBand) {
                    this.getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            }
            final double end = xAxis.getUpperBound();
            if (fillBand) {
                this.getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, end);
            }
        }
    }
    
    public void drawRangeTickBands(final Graphics2D g2, final Rectangle2D dataArea, final List ticks) {
        final Paint bandPaint = this.getRangeTickBandPaint();
        if (bandPaint != null) {
            boolean fillBand = false;
            final ValueAxis axis = this.getRangeAxis();
            double previous = axis.getLowerBound();
            for (final ValueTick tick : ticks) {
                final double current = tick.getValue();
                if (fillBand) {
                    this.getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            }
            final double end = axis.getUpperBound();
            if (fillBand) {
                this.getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, end);
            }
        }
    }
    
    protected Map drawAxes(final Graphics2D g2, final Rectangle2D plotArea, final Rectangle2D dataArea, final PlotRenderingInfo plotState) {
        final AxisCollection axisCollection = new AxisCollection();
        for (int index = 0; index < this.domainAxes.size(); ++index) {
            final ValueAxis axis = (ValueAxis)this.domainAxes.get(index);
            if (axis != null) {
                axisCollection.add((Axis)axis, this.getDomainAxisEdge(index));
            }
        }
        for (int index = 0; index < this.rangeAxes.size(); ++index) {
            final ValueAxis yAxis = (ValueAxis)this.rangeAxes.get(index);
            if (yAxis != null) {
                axisCollection.add((Axis)yAxis, this.getRangeAxisEdge(index));
            }
        }
        final Map axisStateMap = new HashMap();
        double cursor = dataArea.getMinY() - this.axisOffset.calculateTopOutset(dataArea.getHeight());
        for (final ValueAxis axis2 : axisCollection.getAxesAtTop()) {
            final AxisState info = axis2.draw(g2, cursor, plotArea, dataArea, RectangleEdge.TOP, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis2, info);
        }
        cursor = dataArea.getMaxY() + this.axisOffset.calculateBottomOutset(dataArea.getHeight());
        for (final ValueAxis axis2 : axisCollection.getAxesAtBottom()) {
            final AxisState info = axis2.draw(g2, cursor, plotArea, dataArea, RectangleEdge.BOTTOM, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis2, info);
        }
        cursor = dataArea.getMinX() - this.axisOffset.calculateLeftOutset(dataArea.getWidth());
        for (final ValueAxis axis2 : axisCollection.getAxesAtLeft()) {
            final AxisState info = axis2.draw(g2, cursor, plotArea, dataArea, RectangleEdge.LEFT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis2, info);
        }
        cursor = dataArea.getMaxX() + this.axisOffset.calculateRightOutset(dataArea.getWidth());
        for (final ValueAxis axis2 : axisCollection.getAxesAtRight()) {
            final AxisState info = axis2.draw(g2, cursor, plotArea, dataArea, RectangleEdge.RIGHT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis2, info);
        }
        return axisStateMap;
    }
    
    public boolean render(final Graphics2D g2, final Rectangle2D dataArea, final int index, final PlotRenderingInfo info, final CrosshairState crosshairState) {
        boolean foundData = false;
        final XYDataset dataset = this.getDataset(index);
        if (!DatasetUtilities.isEmptyOrNull(dataset)) {
            foundData = true;
            final ValueAxis xAxis = this.getDomainAxisForDataset(index);
            final ValueAxis yAxis = this.getRangeAxisForDataset(index);
            XYItemRenderer renderer = this.getRenderer(index);
            if (renderer == null) {
                renderer = this.getRenderer();
                if (renderer == null) {
                    return foundData;
                }
            }
            final XYItemRendererState state = renderer.initialise(g2, dataArea, this, dataset, info);
            final int passCount = renderer.getPassCount();
            final SeriesRenderingOrder seriesOrder = this.getSeriesRenderingOrder();
            if (seriesOrder == SeriesRenderingOrder.REVERSE) {
                for (int pass = 0; pass < passCount; ++pass) {
                    final int seriesCount = dataset.getSeriesCount();
                    for (int series = seriesCount - 1; series >= 0; --series) {
                        int firstItem = 0;
                        int lastItem = dataset.getItemCount(series) - 1;
                        if (lastItem != -1) {
                            if (state.getProcessVisibleItemsOnly()) {
                                final int[] itemBounds = RendererUtilities.findLiveItems(dataset, series, xAxis.getLowerBound(), xAxis.getUpperBound());
                                firstItem = itemBounds[0];
                                lastItem = itemBounds[1];
                            }
                            for (int item = firstItem; item <= lastItem; ++item) {
                                renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);
                            }
                        }
                    }
                }
            }
            else {
                for (int pass = 0; pass < passCount; ++pass) {
                    for (int seriesCount = dataset.getSeriesCount(), series = 0; series < seriesCount; ++series) {
                        int firstItem = 0;
                        int lastItem = dataset.getItemCount(series) - 1;
                        if (state.getProcessVisibleItemsOnly()) {
                            final int[] itemBounds = RendererUtilities.findLiveItems(dataset, series, xAxis.getLowerBound(), xAxis.getUpperBound());
                            firstItem = itemBounds[0];
                            lastItem = itemBounds[1];
                        }
                        for (int item = firstItem; item <= lastItem; ++item) {
                            renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);
                        }
                    }
                }
            }
        }
        return foundData;
    }
    
    public ValueAxis getDomainAxisForDataset(final int index) {
        if (index < 0 || index >= this.getDatasetCount()) {
            throw new IllegalArgumentException("Index " + index + " out of bounds.");
        }
        ValueAxis valueAxis = null;
        final Integer axisIndex = this.datasetToDomainAxisMap.get(new Integer(index));
        if (axisIndex != null) {
            valueAxis = this.getDomainAxis(axisIndex);
        }
        else {
            valueAxis = this.getDomainAxis(0);
        }
        return valueAxis;
    }
    
    public ValueAxis getRangeAxisForDataset(final int index) {
        if (index < 0 || index >= this.getDatasetCount()) {
            throw new IllegalArgumentException("Index " + index + " out of bounds.");
        }
        ValueAxis valueAxis = null;
        final Integer axisIndex = this.datasetToRangeAxisMap.get(new Integer(index));
        if (axisIndex != null) {
            valueAxis = this.getRangeAxis(axisIndex);
        }
        else {
            valueAxis = this.getRangeAxis(0);
        }
        return valueAxis;
    }
    
    protected void drawDomainGridlines(final Graphics2D g2, final Rectangle2D dataArea, final List ticks) {
        if (this.getRenderer() == null) {
            return;
        }
        if (this.isDomainGridlinesVisible()) {
            final Stroke gridStroke = this.getDomainGridlineStroke();
            final Paint gridPaint = this.getDomainGridlinePaint();
            if (gridStroke != null && gridPaint != null) {
                for (final ValueTick tick : ticks) {
                    this.getRenderer().drawDomainGridLine(g2, this, this.getDomainAxis(), dataArea, tick.getValue());
                }
            }
        }
    }
    
    protected void drawRangeGridlines(final Graphics2D g2, final Rectangle2D area, final List ticks) {
        if (this.getRenderer() == null) {
            return;
        }
        if (this.isRangeGridlinesVisible()) {
            final Stroke gridStroke = this.getRangeGridlineStroke();
            final Paint gridPaint = this.getRangeGridlinePaint();
            final ValueAxis axis = this.getRangeAxis();
            if (axis != null) {
                for (final ValueTick tick : ticks) {
                    if (tick.getValue() != 0.0 || !this.isRangeZeroBaselineVisible()) {
                        this.getRenderer().drawRangeLine(g2, this, this.getRangeAxis(), area, tick.getValue(), gridPaint, gridStroke);
                    }
                }
            }
        }
    }
    
    protected void drawZeroDomainBaseline(final Graphics2D g2, final Rectangle2D area) {
        if (this.isDomainZeroBaselineVisible()) {
            final XYItemRenderer r = this.getRenderer();
            if (r instanceof AbstractXYItemRenderer) {
                final AbstractXYItemRenderer renderer = (AbstractXYItemRenderer)r;
                renderer.drawDomainLine(g2, this, this.getDomainAxis(), area, 0.0, this.domainZeroBaselinePaint, this.domainZeroBaselineStroke);
            }
        }
    }
    
    protected void drawZeroRangeBaseline(final Graphics2D g2, final Rectangle2D area) {
        if (this.isRangeZeroBaselineVisible()) {
            this.getRenderer().drawRangeLine(g2, this, this.getRangeAxis(), area, 0.0, this.rangeZeroBaselinePaint, this.rangeZeroBaselineStroke);
        }
    }
    
    public void drawAnnotations(final Graphics2D g2, final Rectangle2D dataArea, final PlotRenderingInfo info) {
        for (final XYAnnotation annotation : this.annotations) {
            final ValueAxis xAxis = this.getDomainAxis();
            final ValueAxis yAxis = this.getRangeAxis();
            annotation.draw(g2, this, dataArea, xAxis, yAxis, 0, info);
        }
    }
    
    protected void drawDomainMarkers(final Graphics2D g2, final Rectangle2D dataArea, final int index, final Layer layer) {
        final XYItemRenderer r = this.getRenderer(index);
        if (r == null) {
            return;
        }
        if (index >= this.getDatasetCount()) {
            return;
        }
        final Collection markers = this.getDomainMarkers(index, layer);
        final ValueAxis axis = this.getDomainAxisForDataset(index);
        if (markers != null && axis != null) {
            for (final Marker marker : markers) {
                r.drawDomainMarker(g2, this, axis, marker, dataArea);
            }
        }
    }
    
    protected void drawRangeMarkers(final Graphics2D g2, final Rectangle2D dataArea, final int index, final Layer layer) {
        final XYItemRenderer r = this.getRenderer(index);
        if (r == null) {
            return;
        }
        if (index >= this.getDatasetCount()) {
            return;
        }
        final Collection markers = this.getRangeMarkers(index, layer);
        final ValueAxis axis = this.getRangeAxisForDataset(index);
        if (markers != null && axis != null) {
            for (final Marker marker : markers) {
                r.drawRangeMarker(g2, this, axis, marker, dataArea);
            }
        }
    }
    
    public Collection getDomainMarkers(final Layer layer) {
        return this.getDomainMarkers(0, layer);
    }
    
    public Collection getRangeMarkers(final Layer layer) {
        return this.getRangeMarkers(0, layer);
    }
    
    public Collection getDomainMarkers(final int index, final Layer layer) {
        Collection result = null;
        final Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = this.foregroundDomainMarkers.get(key);
        }
        else if (layer == Layer.BACKGROUND) {
            result = this.backgroundDomainMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection((Collection<?>)result);
        }
        return result;
    }
    
    public Collection getRangeMarkers(final int index, final Layer layer) {
        Collection result = null;
        final Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = this.foregroundRangeMarkers.get(key);
        }
        else if (layer == Layer.BACKGROUND) {
            result = this.backgroundRangeMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection((Collection<?>)result);
        }
        return result;
    }
    
    protected void drawHorizontalLine(final Graphics2D g2, final Rectangle2D dataArea, final double value, final Stroke stroke, final Paint paint) {
        ValueAxis axis = this.getRangeAxis();
        if (this.getOrientation() == PlotOrientation.HORIZONTAL) {
            axis = this.getDomainAxis();
        }
        if (axis.getRange().contains(value)) {
            final double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
            final Line2D line = new Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }
    
    protected void drawDomainCrosshair(final Graphics2D g2, final Rectangle2D dataArea, final PlotOrientation orientation, final double value, final ValueAxis axis, final Stroke stroke, final Paint paint) {
        if (axis.getRange().contains(value)) {
            Line2D line = null;
            if (orientation == PlotOrientation.VERTICAL) {
                final double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
                line = new Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            }
            else {
                final double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
                line = new Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            }
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }
    
    protected void drawVerticalLine(final Graphics2D g2, final Rectangle2D dataArea, final double value, final Stroke stroke, final Paint paint) {
        ValueAxis axis = this.getDomainAxis();
        if (this.getOrientation() == PlotOrientation.HORIZONTAL) {
            axis = this.getRangeAxis();
        }
        if (axis.getRange().contains(value)) {
            final double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
            final Line2D line = new Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }
    
    protected void drawRangeCrosshair(final Graphics2D g2, final Rectangle2D dataArea, final PlotOrientation orientation, final double value, final ValueAxis axis, final Stroke stroke, final Paint paint) {
        if (axis.getRange().contains(value)) {
            Line2D line = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                final double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
                line = new Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            }
            else {
                final double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
                line = new Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            }
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }
    
    public void handleClick(final int x, final int y, final PlotRenderingInfo info) {
        final Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            final ValueAxis da = this.getDomainAxis();
            if (da != null) {
                final double hvalue = da.java2DToValue((double)x, info.getDataArea(), this.getDomainAxisEdge());
                this.setDomainCrosshairValue(hvalue);
            }
            final ValueAxis ra = this.getRangeAxis();
            if (ra != null) {
                final double vvalue = ra.java2DToValue((double)y, info.getDataArea(), this.getRangeAxisEdge());
                this.setRangeCrosshairValue(vvalue);
            }
        }
    }
    
    private List getDatasetsMappedToDomainAxis(final Integer axisIndex) {
        if (axisIndex == null) {
            throw new IllegalArgumentException("Null 'axisIndex' argument.");
        }
        final List result = new ArrayList();
        for (int i = 0; i < this.datasets.size(); ++i) {
            final Integer mappedAxis = this.datasetToDomainAxisMap.get(new Integer(i));
            if (mappedAxis == null) {
                if (axisIndex.equals(XYPlot.ZERO)) {
                    result.add(this.datasets.get(i));
                }
            }
            else if (mappedAxis.equals(axisIndex)) {
                result.add(this.datasets.get(i));
            }
        }
        return result;
    }
    
    private List getDatasetsMappedToRangeAxis(final Integer axisIndex) {
        if (axisIndex == null) {
            throw new IllegalArgumentException("Null 'axisIndex' argument.");
        }
        final List result = new ArrayList();
        for (int i = 0; i < this.datasets.size(); ++i) {
            final Integer mappedAxis = this.datasetToRangeAxisMap.get(new Integer(i));
            if (mappedAxis == null) {
                if (axisIndex.equals(XYPlot.ZERO)) {
                    result.add(this.datasets.get(i));
                }
            }
            else if (mappedAxis.equals(axisIndex)) {
                result.add(this.datasets.get(i));
            }
        }
        return result;
    }
    
    public int getDomainAxisIndex(final ValueAxis axis) {
        int result = this.domainAxes.indexOf((Object)axis);
        if (result < 0) {
            final Plot parent = this.getParent();
            if (parent instanceof XYPlot) {
                final XYPlot p = (XYPlot)parent;
                result = p.getDomainAxisIndex(axis);
            }
        }
        return result;
    }
    
    public int getRangeAxisIndex(final ValueAxis axis) {
        int result = this.rangeAxes.indexOf((Object)axis);
        if (result < 0) {
            final Plot parent = this.getParent();
            if (parent instanceof XYPlot) {
                final XYPlot p = (XYPlot)parent;
                result = p.getRangeAxisIndex(axis);
            }
        }
        return result;
    }
    
    public Range getDataRange(final ValueAxis axis) {
        Range result = null;
        final List mappedDatasets = new ArrayList();
        boolean isDomainAxis = true;
        final int domainIndex = this.getDomainAxisIndex(axis);
        if (domainIndex >= 0) {
            isDomainAxis = true;
            mappedDatasets.addAll(this.getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        }
        final int rangeIndex = this.getRangeAxisIndex(axis);
        if (rangeIndex >= 0) {
            isDomainAxis = false;
            mappedDatasets.addAll(this.getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        }
        for (final XYDataset d : mappedDatasets) {
            if (d != null) {
                final XYItemRenderer r = this.getRendererForDataset(d);
                if (isDomainAxis) {
                    if (r != null) {
                        result = Range.combine(result, r.findDomainBounds(d));
                    }
                    else {
                        result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                    }
                }
                else if (r != null) {
                    result = Range.combine(result, r.findRangeBounds(d));
                }
                else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
        }
        return result;
    }
    
    public void datasetChanged(final DatasetChangeEvent event) {
        this.configureDomainAxes();
        this.configureRangeAxes();
        if (this.getParent() != null) {
            this.getParent().datasetChanged(event);
        }
        else {
            final PlotChangeEvent e = new PlotChangeEvent((Plot)this);
            e.setType(ChartChangeEventType.DATASET_UPDATED);
            this.notifyListeners(e);
        }
    }
    
    public void rendererChanged(final RendererChangeEvent event) {
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean isDomainCrosshairVisible() {
        return this.domainCrosshairVisible;
    }
    
    public void setDomainCrosshairVisible(final boolean flag) {
        if (this.domainCrosshairVisible != flag) {
            this.domainCrosshairVisible = flag;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public boolean isDomainCrosshairLockedOnData() {
        return this.domainCrosshairLockedOnData;
    }
    
    public void setDomainCrosshairLockedOnData(final boolean flag) {
        if (this.domainCrosshairLockedOnData != flag) {
            this.domainCrosshairLockedOnData = flag;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public double getDomainCrosshairValue() {
        return this.domainCrosshairValue;
    }
    
    public void setDomainCrosshairValue(final double value) {
        this.setDomainCrosshairValue(value, true);
    }
    
    public void setDomainCrosshairValue(final double value, final boolean notify) {
        this.domainCrosshairValue = value;
        if (this.isDomainCrosshairVisible() && notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public Stroke getDomainCrosshairStroke() {
        return this.domainCrosshairStroke;
    }
    
    public void setDomainCrosshairStroke(final Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainCrosshairStroke = stroke;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getDomainCrosshairPaint() {
        return this.domainCrosshairPaint;
    }
    
    public void setDomainCrosshairPaint(final Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainCrosshairPaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public boolean isRangeCrosshairVisible() {
        return this.rangeCrosshairVisible;
    }
    
    public void setRangeCrosshairVisible(final boolean flag) {
        if (this.rangeCrosshairVisible != flag) {
            this.rangeCrosshairVisible = flag;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public boolean isRangeCrosshairLockedOnData() {
        return this.rangeCrosshairLockedOnData;
    }
    
    public void setRangeCrosshairLockedOnData(final boolean flag) {
        if (this.rangeCrosshairLockedOnData != flag) {
            this.rangeCrosshairLockedOnData = flag;
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public double getRangeCrosshairValue() {
        return this.rangeCrosshairValue;
    }
    
    public void setRangeCrosshairValue(final double value) {
        this.setRangeCrosshairValue(value, true);
    }
    
    public void setRangeCrosshairValue(final double value, final boolean notify) {
        this.rangeCrosshairValue = value;
        if (this.isRangeCrosshairVisible() && notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public Stroke getRangeCrosshairStroke() {
        return this.rangeCrosshairStroke;
    }
    
    public void setRangeCrosshairStroke(final Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeCrosshairStroke = stroke;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public Paint getRangeCrosshairPaint() {
        return this.rangeCrosshairPaint;
    }
    
    public void setRangeCrosshairPaint(final Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeCrosshairPaint = paint;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public AxisSpace getFixedDomainAxisSpace() {
        return this.fixedDomainAxisSpace;
    }
    
    public void setFixedDomainAxisSpace(final AxisSpace space) {
        this.setFixedDomainAxisSpace(space, true);
    }
    
    public void setFixedDomainAxisSpace(final AxisSpace space, final boolean notify) {
        this.fixedDomainAxisSpace = space;
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public AxisSpace getFixedRangeAxisSpace() {
        return this.fixedRangeAxisSpace;
    }
    
    public void setFixedRangeAxisSpace(final AxisSpace space) {
        this.setFixedRangeAxisSpace(space, true);
    }
    
    public void setFixedRangeAxisSpace(final AxisSpace space, final boolean notify) {
        this.fixedRangeAxisSpace = space;
        if (notify) {
            this.notifyListeners(new PlotChangeEvent((Plot)this));
        }
    }
    
    public void zoomDomainAxes(final double factor, final PlotRenderingInfo info, final Point2D source) {
        this.zoomDomainAxes(factor, info, source, false);
    }
    
    public void zoomDomainAxes(final double factor, final PlotRenderingInfo info, final Point2D source, final boolean useAnchor) {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            final ValueAxis domainAxis = (ValueAxis)this.domainAxes.get(i);
            if (domainAxis != null) {
                if (useAnchor) {
                    double sourceX = source.getX();
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceX = source.getY();
                    }
                    final double anchorX = domainAxis.java2DToValue(sourceX, info.getDataArea(), this.getDomainAxisEdge());
                    domainAxis.resizeRange(factor, anchorX);
                }
                else {
                    domainAxis.resizeRange(factor);
                }
            }
        }
    }
    
    public void zoomDomainAxes(final double lowerPercent, final double upperPercent, final PlotRenderingInfo info, final Point2D source) {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            final ValueAxis domainAxis = (ValueAxis)this.domainAxes.get(i);
            if (domainAxis != null) {
                domainAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }
    
    public void zoomRangeAxes(final double factor, final PlotRenderingInfo info, final Point2D source) {
        this.zoomRangeAxes(factor, info, source, false);
    }
    
    public void zoomRangeAxes(final double factor, final PlotRenderingInfo info, final Point2D source, final boolean useAnchor) {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            final ValueAxis rangeAxis = (ValueAxis)this.rangeAxes.get(i);
            if (rangeAxis != null) {
                if (useAnchor) {
                    double sourceY = source.getY();
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceY = source.getX();
                    }
                    final double anchorY = rangeAxis.java2DToValue(sourceY, info.getDataArea(), this.getRangeAxisEdge());
                    rangeAxis.resizeRange(factor, anchorY);
                }
                else {
                    rangeAxis.resizeRange(factor);
                }
            }
        }
    }
    
    public void zoomRangeAxes(final double lowerPercent, final double upperPercent, final PlotRenderingInfo info, final Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            final ValueAxis rangeAxis = (ValueAxis)this.rangeAxes.get(i);
            if (rangeAxis != null) {
                rangeAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }
    
    public boolean isDomainZoomable() {
        return true;
    }
    
    public boolean isRangeZoomable() {
        return true;
    }
    
    public int getSeriesCount() {
        int result = 0;
        final XYDataset dataset = this.getDataset();
        if (dataset != null) {
            result = dataset.getSeriesCount();
        }
        return result;
    }
    
    public LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }
    
    public void setFixedLegendItems(final LegendItemCollection items) {
        this.fixedLegendItems = items;
        this.notifyListeners(new PlotChangeEvent((Plot)this));
    }
    
    public LegendItemCollection getLegendItems() {
        if (this.fixedLegendItems != null) {
            return this.fixedLegendItems;
        }
        final LegendItemCollection result = new LegendItemCollection();
        for (int count = this.datasets.size(), datasetIndex = 0; datasetIndex < count; ++datasetIndex) {
            final XYDataset dataset = this.getDataset(datasetIndex);
            if (dataset != null) {
                XYItemRenderer renderer = this.getRenderer(datasetIndex);
                if (renderer == null) {
                    renderer = this.getRenderer(0);
                }
                if (renderer != null) {
                    for (int seriesCount = dataset.getSeriesCount(), i = 0; i < seriesCount; ++i) {
                        if (renderer.isSeriesVisible(i) && renderer.isSeriesVisibleInLegend(i)) {
                            final LegendItem item = renderer.getLegendItem(datasetIndex, i);
                            if (item != null) {
                                result.add(item);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYPlot)) {
            return false;
        }
        final XYPlot that = (XYPlot)obj;
        if (this.weight != that.weight) {
            return false;
        }
        if (this.orientation != that.orientation) {
            return false;
        }
        if (!this.domainAxes.equals((Object)that.domainAxes)) {
            return false;
        }
        if (!this.domainAxisLocations.equals((Object)that.domainAxisLocations)) {
            return false;
        }
        if (this.rangeCrosshairLockedOnData != that.rangeCrosshairLockedOnData) {
            return false;
        }
        if (this.domainGridlinesVisible != that.domainGridlinesVisible) {
            return false;
        }
        if (this.rangeGridlinesVisible != that.rangeGridlinesVisible) {
            return false;
        }
        if (this.domainZeroBaselineVisible != that.domainZeroBaselineVisible) {
            return false;
        }
        if (this.rangeZeroBaselineVisible != that.rangeZeroBaselineVisible) {
            return false;
        }
        if (this.domainCrosshairVisible != that.domainCrosshairVisible) {
            return false;
        }
        if (this.domainCrosshairValue != that.domainCrosshairValue) {
            return false;
        }
        if (this.domainCrosshairLockedOnData != that.domainCrosshairLockedOnData) {
            return false;
        }
        if (this.rangeCrosshairVisible != that.rangeCrosshairVisible) {
            return false;
        }
        if (this.rangeCrosshairValue != that.rangeCrosshairValue) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.axisOffset, (Object)that.axisOffset)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.renderers, (Object)that.renderers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.rangeAxes, (Object)that.rangeAxes)) {
            return false;
        }
        if (!this.rangeAxisLocations.equals((Object)that.rangeAxisLocations)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.datasetToDomainAxisMap, (Object)that.datasetToDomainAxisMap)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.datasetToRangeAxisMap, (Object)that.datasetToRangeAxisMap)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.domainGridlineStroke, (Object)that.domainGridlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainGridlinePaint, that.domainGridlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.rangeGridlineStroke, (Object)that.rangeGridlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeGridlinePaint, that.rangeGridlinePaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainZeroBaselinePaint, that.domainZeroBaselinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.domainZeroBaselineStroke, (Object)that.domainZeroBaselineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeZeroBaselinePaint, that.rangeZeroBaselinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.rangeZeroBaselineStroke, (Object)that.rangeZeroBaselineStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.domainCrosshairStroke, (Object)that.domainCrosshairStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainCrosshairPaint, that.domainCrosshairPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.rangeCrosshairStroke, (Object)that.rangeCrosshairStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeCrosshairPaint, that.rangeCrosshairPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.foregroundDomainMarkers, (Object)that.foregroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.backgroundDomainMarkers, (Object)that.backgroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.foregroundRangeMarkers, (Object)that.foregroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.backgroundRangeMarkers, (Object)that.backgroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.foregroundDomainMarkers, (Object)that.foregroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.backgroundDomainMarkers, (Object)that.backgroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.foregroundRangeMarkers, (Object)that.foregroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.backgroundRangeMarkers, (Object)that.backgroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal((Object)this.annotations, (Object)that.annotations)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainTickBandPaint, that.domainTickBandPaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeTickBandPaint, that.rangeTickBandPaint)) {
            return false;
        }
        if (!this.quadrantOrigin.equals(that.quadrantOrigin)) {
            return false;
        }
        for (int i = 0; i < 4; ++i) {
            if (!PaintUtilities.equal(this.quadrantPaint[i], that.quadrantPaint[i])) {
                return false;
            }
        }
        return super.equals(obj);
    }
    
    public Object clone() throws CloneNotSupportedException {
        final XYPlot clone = (XYPlot)super.clone();
        clone.domainAxes = (ObjectList)ObjectUtilities.clone((Object)this.domainAxes);
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            final ValueAxis axis = (ValueAxis)this.domainAxes.get(i);
            if (axis != null) {
                final ValueAxis clonedAxis = (ValueAxis)axis.clone();
                clone.domainAxes.set(i, (Object)clonedAxis);
                clonedAxis.setPlot((Plot)clone);
                clonedAxis.addChangeListener((AxisChangeListener)clone);
            }
        }
        clone.domainAxisLocations = (ObjectList)this.domainAxisLocations.clone();
        clone.rangeAxes = (ObjectList)ObjectUtilities.clone((Object)this.rangeAxes);
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            final ValueAxis axis = (ValueAxis)this.rangeAxes.get(i);
            if (axis != null) {
                final ValueAxis clonedAxis = (ValueAxis)axis.clone();
                clone.rangeAxes.set(i, (Object)clonedAxis);
                clonedAxis.setPlot((Plot)clone);
                clonedAxis.addChangeListener((AxisChangeListener)clone);
            }
        }
        clone.rangeAxisLocations = (ObjectList)ObjectUtilities.clone((Object)this.rangeAxisLocations);
        clone.datasets = (ObjectList)ObjectUtilities.clone((Object)this.datasets);
        for (int i = 0; i < clone.datasets.size(); ++i) {
            final XYDataset d = this.getDataset(i);
            if (d != null) {
                d.addChangeListener((DatasetChangeListener)clone);
            }
        }
        (clone.datasetToDomainAxisMap = new TreeMap()).putAll(this.datasetToDomainAxisMap);
        (clone.datasetToRangeAxisMap = new TreeMap()).putAll(this.datasetToRangeAxisMap);
        clone.renderers = (ObjectList)ObjectUtilities.clone((Object)this.renderers);
        for (int i = 0; i < this.renderers.size(); ++i) {
            final XYItemRenderer renderer2 = (XYItemRenderer)this.renderers.get(i);
            if (renderer2 instanceof PublicCloneable) {
                final PublicCloneable pc = (PublicCloneable)renderer2;
                clone.renderers.set(i, pc.clone());
            }
        }
        clone.foregroundDomainMarkers = (Map)ObjectUtilities.clone((Object)this.foregroundDomainMarkers);
        clone.backgroundDomainMarkers = (Map)ObjectUtilities.clone((Object)this.backgroundDomainMarkers);
        clone.foregroundRangeMarkers = (Map)ObjectUtilities.clone((Object)this.foregroundRangeMarkers);
        clone.backgroundRangeMarkers = (Map)ObjectUtilities.clone((Object)this.backgroundRangeMarkers);
        clone.annotations = (List)ObjectUtilities.deepClone((Collection)this.annotations);
        if (this.fixedDomainAxisSpace != null) {
            clone.fixedDomainAxisSpace = (AxisSpace)ObjectUtilities.clone((Object)this.fixedDomainAxisSpace);
        }
        if (this.fixedRangeAxisSpace != null) {
            clone.fixedRangeAxisSpace = (AxisSpace)ObjectUtilities.clone((Object)this.fixedRangeAxisSpace);
        }
        clone.quadrantOrigin = (Point2D)ObjectUtilities.clone((Object)this.quadrantOrigin);
        clone.quadrantPaint = this.quadrantPaint.clone();
        return clone;
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeStroke(this.domainGridlineStroke, stream);
        SerialUtilities.writePaint(this.domainGridlinePaint, stream);
        SerialUtilities.writeStroke(this.rangeGridlineStroke, stream);
        SerialUtilities.writePaint(this.rangeGridlinePaint, stream);
        SerialUtilities.writeStroke(this.rangeZeroBaselineStroke, stream);
        SerialUtilities.writePaint(this.rangeZeroBaselinePaint, stream);
        SerialUtilities.writeStroke(this.domainCrosshairStroke, stream);
        SerialUtilities.writePaint(this.domainCrosshairPaint, stream);
        SerialUtilities.writeStroke(this.rangeCrosshairStroke, stream);
        SerialUtilities.writePaint(this.rangeCrosshairPaint, stream);
        SerialUtilities.writePaint(this.domainTickBandPaint, stream);
        SerialUtilities.writePaint(this.rangeTickBandPaint, stream);
        SerialUtilities.writePoint2D(this.quadrantOrigin, stream);
        for (int i = 0; i < 4; ++i) {
            SerialUtilities.writePaint(this.quadrantPaint[i], stream);
        }
        SerialUtilities.writeStroke(this.domainZeroBaselineStroke, stream);
        SerialUtilities.writePaint(this.domainZeroBaselinePaint, stream);
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.domainGridlineStroke = SerialUtilities.readStroke(stream);
        this.domainGridlinePaint = SerialUtilities.readPaint(stream);
        this.rangeGridlineStroke = SerialUtilities.readStroke(stream);
        this.rangeGridlinePaint = SerialUtilities.readPaint(stream);
        this.rangeZeroBaselineStroke = SerialUtilities.readStroke(stream);
        this.rangeZeroBaselinePaint = SerialUtilities.readPaint(stream);
        this.domainCrosshairStroke = SerialUtilities.readStroke(stream);
        this.domainCrosshairPaint = SerialUtilities.readPaint(stream);
        this.rangeCrosshairStroke = SerialUtilities.readStroke(stream);
        this.rangeCrosshairPaint = SerialUtilities.readPaint(stream);
        this.domainTickBandPaint = SerialUtilities.readPaint(stream);
        this.rangeTickBandPaint = SerialUtilities.readPaint(stream);
        this.quadrantOrigin = SerialUtilities.readPoint2D(stream);
        this.quadrantPaint = new Paint[4];
        for (int i = 0; i < 4; ++i) {
            this.quadrantPaint[i] = SerialUtilities.readPaint(stream);
        }
        this.domainZeroBaselineStroke = SerialUtilities.readStroke(stream);
        this.domainZeroBaselinePaint = SerialUtilities.readPaint(stream);
        for (int domainAxisCount = this.domainAxes.size(), j = 0; j < domainAxisCount; ++j) {
            final Axis axis = (Axis)this.domainAxes.get(j);
            if (axis != null) {
                axis.setPlot((Plot)this);
                axis.addChangeListener((AxisChangeListener)this);
            }
        }
        for (int rangeAxisCount = this.rangeAxes.size(), k = 0; k < rangeAxisCount; ++k) {
            final Axis axis2 = (Axis)this.rangeAxes.get(k);
            if (axis2 != null) {
                axis2.setPlot((Plot)this);
                axis2.addChangeListener((AxisChangeListener)this);
            }
        }
        for (int datasetCount = this.datasets.size(), l = 0; l < datasetCount; ++l) {
            final Dataset dataset = (Dataset)this.datasets.get(l);
            if (dataset != null) {
                dataset.addChangeListener((DatasetChangeListener)this);
            }
        }
        for (int rendererCount = this.renderers.size(), m = 0; m < rendererCount; ++m) {
            final XYItemRenderer renderer = (XYItemRenderer)this.renderers.get(m);
            if (renderer != null) {
                renderer.addChangeListener((RendererChangeListener)this);
            }
        }
    }
    
    static {
        XXXmyClinitXXX();
    }
    
    public static void XXXmyClinitXXX() {
        Clinit.logClinit("org/jfree/chart/plot/XYPlot");
        XYPlot.localizationResources = null;
        DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f, 0, 2, 0.0f, new float[] { 2.0f, 2.0f }, 0.0f);
        DEFAULT_GRIDLINE_PAINT = Color.lightGray;
        DEFAULT_CROSSHAIR_STROKE = XYPlot.DEFAULT_GRIDLINE_STROKE;
        DEFAULT_CROSSHAIR_PAINT = Color.blue;
        XYPlot.localizationResources = ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");
    }
}
