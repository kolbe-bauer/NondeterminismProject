// 
// Decompiled by Procyon v0.5.36
// 

package org.jfree.data.time;

import clinitrewriter.Clinit;
import org.jfree.data.SeriesException;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import org.jfree.data.Series;

public class TimeSeries extends Series implements Serializable
{
    protected static final String DEFAULT_DOMAIN_DESCRIPTION = "Time";
    protected static final String DEFAULT_RANGE_DESCRIPTION = "Value";
    private String domain;
    private String range;
    private Class timePeriodClass;
    private List data;
    private int maximumItemCount;
    private int historyCount;
    static /* synthetic */ Class class$org$jfree$data$time$Day;
    
    public TimeSeries(final String s) {
        this(s, "Time", "Value", Day.class);
    }
    
    public TimeSeries(final String s, final Class clazz) {
        this(s, "Time", "Value", clazz);
    }
    
    public TimeSeries(final String s, final String domain, final String range, final Class timePeriodClass) {
        super(s);
        this.domain = domain;
        this.range = range;
        this.timePeriodClass = timePeriodClass;
        this.data = new ArrayList();
        this.maximumItemCount = Integer.MAX_VALUE;
        this.historyCount = 0;
    }
    
    public String getDomainDescription() {
        return this.domain;
    }
    
    public void setDomainDescription(final String domain) {
        this.firePropertyChange("Domain", (Object)this.domain, (Object)(this.domain = domain));
    }
    
    public String getRangeDescription() {
        return this.range;
    }
    
    public void setRangeDescription(final String range) {
        this.firePropertyChange("Range", (Object)this.range, (Object)(this.range = range));
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
    
    public int getHistoryCount() {
        return this.historyCount;
    }
    
    public void setHistoryCount(final int historyCount) {
        this.historyCount = historyCount;
    }
    
    public Class getTimePeriodClass() {
        return this.timePeriodClass;
    }
    
    public TimeSeriesDataItem getDataPair(final int n) {
        return this.data.get(n);
    }
    
    public TimeSeriesDataItem getDataPair(final RegularTimePeriod regularTimePeriod) {
        if (regularTimePeriod == null) {
            throw new IllegalArgumentException("TimeSeries.getDataPair(...): null time period not allowed.");
        }
        final int binarySearch = Collections.binarySearch(this.data, new TimeSeriesDataItem(regularTimePeriod, (Number)new Integer(0)));
        if (binarySearch >= 0) {
            return (TimeSeriesDataItem)this.data.get(binarySearch);
        }
        return null;
    }
    
    public RegularTimePeriod getTimePeriod(final int n) {
        return this.getDataPair(n).getPeriod();
    }
    
    public RegularTimePeriod getNextTimePeriod() {
        return this.getTimePeriod(this.getItemCount() - 1).next();
    }
    
    public Collection getTimePeriods() {
        final ArrayList<RegularTimePeriod> list = new ArrayList<RegularTimePeriod>();
        for (int i = 0; i < this.getItemCount(); ++i) {
            list.add(this.getTimePeriod(i));
        }
        return list;
    }
    
    public Collection getTimePeriodsUniqueToOtherSeries(final TimeSeries timeSeries) {
        final ArrayList<RegularTimePeriod> list = new ArrayList<RegularTimePeriod>();
        for (int i = 0; i < timeSeries.getItemCount(); ++i) {
            final RegularTimePeriod timePeriod = timeSeries.getTimePeriod(i);
            if (this.getIndex(timePeriod) < 0) {
                list.add(timePeriod);
            }
        }
        return list;
    }
    
    public int getIndex(final RegularTimePeriod regularTimePeriod) {
        if (regularTimePeriod != null) {
            return Collections.binarySearch(this.data, new TimeSeriesDataItem(regularTimePeriod, (Number)new Integer(0)));
        }
        return -1;
    }
    
    public Number getValue(final int n) {
        return this.getDataPair(n).getValue();
    }
    
    public Number getValue(final RegularTimePeriod regularTimePeriod) {
        final int index = this.getIndex(regularTimePeriod);
        if (index >= 0) {
            return this.getValue(index);
        }
        return null;
    }
    
    public void add(final TimeSeriesDataItem key) throws SeriesException {
        if (key == null) {
            throw new IllegalArgumentException("TimeSeries.add(...): null item not allowed.");
        }
        if (!key.getPeriod().getClass().equals(this.timePeriodClass)) {
            throw new SeriesException("TimeSeries.add(): you are trying to add data where the time " + "period class is " + key.getPeriod().getClass().getName() + ", " + "but the TimeSeries is expecting an instance of " + this.timePeriodClass.getName() + ".");
        }
        final int binarySearch = Collections.binarySearch(this.data, key);
        if (binarySearch < 0) {
            this.data.add(-binarySearch - 1, key);
            if (this.getItemCount() > this.maximumItemCount) {
                this.data.remove(0);
            }
            if (this.getItemCount() > 1 && this.historyCount > 0) {
                while (this.getTimePeriod(this.getItemCount() - 1).getSerialIndex() - this.getTimePeriod(0).getSerialIndex() >= this.historyCount) {
                    this.data.remove(0);
                }
            }
            this.fireSeriesChanged();
            return;
        }
        throw new SeriesException("TimeSeries.add(...): time period already exists.");
    }
    
    public void add(final RegularTimePeriod regularTimePeriod, final double n) throws SeriesException {
        this.add(new TimeSeriesDataItem(regularTimePeriod, n));
    }
    
    public void add(final RegularTimePeriod regularTimePeriod, final Number n) throws SeriesException {
        this.add(new TimeSeriesDataItem(regularTimePeriod, n));
    }
    
    public void update(final RegularTimePeriod regularTimePeriod, final Number value) throws SeriesException {
        final int binarySearch = Collections.binarySearch(this.data, new TimeSeriesDataItem(regularTimePeriod, value));
        if (binarySearch >= 0) {
            ((TimeSeriesDataItem)this.data.get(binarySearch)).setValue(value);
            this.fireSeriesChanged();
            return;
        }
        throw new SeriesException("TimeSeries.update(TimePeriod, Number): period does not exist.");
    }
    
    public void update(final int n, final Number value) {
        this.getDataPair(n).setValue(value);
        this.fireSeriesChanged();
    }
    
    public TimeSeries addAndOrUpdate(final TimeSeries timeSeries) {
        final TimeSeries timeSeries2 = new TimeSeries("Overwritten values from: " + this.getName());
        for (int i = 0; i < timeSeries.getItemCount(); ++i) {
            final TimeSeriesDataItem dataPair = timeSeries.getDataPair(i);
            final TimeSeriesDataItem addOrUpdate = this.addOrUpdate(dataPair.getPeriod(), dataPair.getValue());
            if (addOrUpdate != null) {
                try {
                    timeSeries2.add(addOrUpdate);
                }
                catch (SeriesException ex) {
                    System.err.println("TimeSeries.addAndOrUpdate(series): unable to add data to overwritten series.");
                }
            }
        }
        return timeSeries2;
    }
    
    public TimeSeriesDataItem addOrUpdate(final RegularTimePeriod regularTimePeriod, final Number value) {
        TimeSeriesDataItem timeSeriesDataItem = null;
        final int binarySearch = Collections.binarySearch(this.data, new TimeSeriesDataItem(regularTimePeriod, value));
        if (binarySearch >= 0) {
            final TimeSeriesDataItem timeSeriesDataItem2 = this.data.get(binarySearch);
            timeSeriesDataItem = (TimeSeriesDataItem)timeSeriesDataItem2.clone();
            timeSeriesDataItem2.setValue(value);
            this.fireSeriesChanged();
        }
        else {
            this.data.add(-binarySearch - 1, new TimeSeriesDataItem(regularTimePeriod, value));
            this.fireSeriesChanged();
        }
        return timeSeriesDataItem;
    }
    
    public void delete(final RegularTimePeriod regularTimePeriod) {
        this.data.remove(this.getIndex(regularTimePeriod));
    }
    
    public void delete(final int n, final int n2) {
        for (int i = 0; i <= n2 - n; ++i) {
            this.data.remove(n);
        }
        this.fireSeriesChanged();
    }
    
    public Object clone() {
        return this.createCopy(0, this.getItemCount() - 1);
    }
    
    public TimeSeries createCopy(final int n, final int n2) {
        final TimeSeries timeSeries = (TimeSeries)super.clone();
        timeSeries.data = new ArrayList();
        if (this.data.size() > 0) {
            for (int i = n; i <= n2; ++i) {
                final TimeSeriesDataItem timeSeriesDataItem = (TimeSeriesDataItem)this.data.get(i).clone();
                try {
                    timeSeries.add(timeSeriesDataItem);
                }
                catch (SeriesException ex) {
                    System.err.println("TimeSeries.createCopy(): unable to add cloned data pair.");
                }
            }
        }
        return timeSeries;
    }
    
    public TimeSeries createCopy(final RegularTimePeriod regularTimePeriod, final RegularTimePeriod regularTimePeriod2) {
        return this.createCopy(this.getIndex(regularTimePeriod), this.getIndex(regularTimePeriod2));
    }
    
    public boolean equals(final Object o) {
        int n = 0;
        if (this == o) {
            n = 1;
        }
        else if (o instanceof TimeSeries) {
            final TimeSeries timeSeries = (TimeSeries)o;
            final int itemCount = this.getItemCount();
            int n2 = 1;
            if (timeSeries.getItemCount() == itemCount) {
                for (int i = 0; i < itemCount; ++i) {
                    n2 = ((n2 && this.getDataPair(i).equals((Object)timeSeries.getDataPair(i))) ? 1 : 0);
                    if (n2 == 0) {}
                }
            }
            n = n2;
        }
        return n != 0;
    }
    
    static {
        XXXmyClinitXXX();
    }
    
    public static void XXXmyClinitXXX() {
        Clinit.logClinit("org/jfree/data/time/TimeSeries");
        TimeSeries.class$org$jfree$data$time$Day = null;
    }
}
