// 
// Decompiled by Procyon v0.5.36
// 

package org.jfree.data;

import org.jfree.util.ObjectUtils;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class XYSeries extends Series implements Serializable
{
    private List data;
    private int maximumItemCount;
    private boolean allowDuplicateXValues;
    
    public XYSeries(final String s) {
        this(s, true);
    }
    
    public XYSeries(final String s, final boolean allowDuplicateXValues) {
        super(s);
        this.maximumItemCount = Integer.MAX_VALUE;
        this.allowDuplicateXValues = allowDuplicateXValues;
        this.data = new ArrayList();
    }
    
    public int getItemCount() {
        return this.data.size();
    }
    
    public int getMaximumItemCount() {
        return this.maximumItemCount;
    }
    
    public void setMaximumItemCount(final int maximumItemCount) {
        this.maximumItemCount = maximumItemCount;
    }
    
    public void add(final XYDataPair key) throws SeriesException {
        if (key == null) {
            throw new IllegalArgumentException("XYSeries.add(...): null item not allowed.");
        }
        final int binarySearch = Collections.binarySearch(this.data, key);
        if (binarySearch < 0) {
            this.data.add(-binarySearch - 1, key);
            if (this.getItemCount() > this.maximumItemCount) {
                this.data.remove(0);
            }
            this.fireSeriesChanged();
        }
        else {
            if (!this.allowDuplicateXValues) {
                throw new SeriesException("XYSeries.add(...): x-value already exists.");
            }
            this.data.add(binarySearch, key);
            if (this.getItemCount() > this.maximumItemCount) {
                this.data.remove(0);
            }
            this.fireSeriesChanged();
        }
    }
    
    public void add(final double value, final double value2) throws SeriesException {
        this.add(new Double(value), new Double(value2));
    }
    
    public void add(final double value, final Number n) throws SeriesException {
        this.add(new Double(value), n);
    }
    
    public void add(final Number n, final Number n2) throws SeriesException {
        this.add(new XYDataPair(n, n2));
    }
    
    public void delete(final int n, final int n2) {
        for (int i = n; i <= n2; ++i) {
            this.data.remove(n);
        }
        this.fireSeriesChanged();
    }
    
    public void clear() {
        if (this.data.size() > 0) {
            this.data.clear();
            this.fireSeriesChanged();
        }
    }
    
    public XYDataPair getDataPair(final int n) {
        return this.data.get(n);
    }
    
    public Number getXValue(final int n) {
        return this.getDataPair(n).getX();
    }
    
    public Number getYValue(final int n) {
        return this.getDataPair(n).getY();
    }
    
    public void update(final int n, final Number y) {
        this.getDataPair(n).setY(y);
        this.fireSeriesChanged();
    }
    
    public Object clone() {
        return this.createCopy(0, this.getItemCount() - 1);
    }
    
    public XYSeries createCopy(final int n, final int n2) {
        final XYSeries xySeries = (XYSeries)super.clone();
        xySeries.data = new ArrayList();
        if (this.data.size() > 0) {
            for (int i = n; i <= n2; ++i) {
                final XYDataPair xyDataPair = (XYDataPair)this.data.get(i).clone();
                try {
                    xySeries.add(xyDataPair);
                }
                catch (SeriesException ex) {
                    System.err.println("XYSeries.createCopy(): unable to add cloned data pair.");
                }
            }
        }
        return xySeries;
    }
    
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof XYSeries) {
            final XYSeries xySeries = (XYSeries)o;
            final boolean equalOrBothNull = ObjectUtils.equalOrBothNull((Object)this.data, (Object)xySeries.data);
            final boolean b = this.maximumItemCount == xySeries.maximumItemCount;
            final boolean b2 = this.allowDuplicateXValues == xySeries.allowDuplicateXValues;
            return equalOrBothNull && b && b2;
        }
        return false;
    }
}
