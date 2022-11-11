// 
// Decompiled by Procyon v0.5.36
// 

package org.jfree.data.time;

import java.text.SimpleDateFormat;
import clinitrewriter.Clinit;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;
import org.jfree.date.SerialDate;
import java.text.DateFormat;
import java.io.Serializable;

public class Day extends RegularTimePeriod implements Serializable
{
    private static final long serialVersionUID = -7082667380758962755L;
    protected static final DateFormat DATE_FORMAT;
    protected static final DateFormat DATE_FORMAT_SHORT;
    protected static final DateFormat DATE_FORMAT_MEDIUM;
    protected static final DateFormat DATE_FORMAT_LONG;
    private SerialDate serialDate;
    private long firstMillisecond;
    private long lastMillisecond;
    
    public Day() {
        this(new Date());
    }
    
    public Day(final int day, final int month, final int year) {
        this.serialDate = SerialDate.createInstance(day, month, year);
        this.peg(Calendar.getInstance());
    }
    
    public Day(final SerialDate serialDate) {
        if (serialDate == null) {
            throw new IllegalArgumentException("Null 'serialDate' argument.");
        }
        this.serialDate = serialDate;
        this.peg(Calendar.getInstance());
    }
    
    public Day(final Date time) {
        this(time, TimeZone.getDefault(), Locale.getDefault());
    }
    
    public Day(final Date time, final TimeZone zone) {
        this(time, zone, Locale.getDefault());
    }
    
    public Day(final Date time, final TimeZone zone, final Locale locale) {
        if (time == null) {
            throw new IllegalArgumentException("Null 'time' argument.");
        }
        if (zone == null) {
            throw new IllegalArgumentException("Null 'zone' argument.");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Null 'locale' argument.");
        }
        final Calendar calendar = Calendar.getInstance(zone, locale);
        calendar.setTime(time);
        final int d = calendar.get(5);
        final int m = calendar.get(2) + 1;
        final int y = calendar.get(1);
        this.serialDate = SerialDate.createInstance(d, m, y);
        this.peg(calendar);
    }
    
    public SerialDate getSerialDate() {
        return this.serialDate;
    }
    
    public int getYear() {
        return this.serialDate.getYYYY();
    }
    
    public int getMonth() {
        return this.serialDate.getMonth();
    }
    
    public int getDayOfMonth() {
        return this.serialDate.getDayOfMonth();
    }
    
    public long getFirstMillisecond() {
        return this.firstMillisecond;
    }
    
    public long getLastMillisecond() {
        return this.lastMillisecond;
    }
    
    public void peg(final Calendar calendar) {
        this.firstMillisecond = this.getFirstMillisecond(calendar);
        this.lastMillisecond = this.getLastMillisecond(calendar);
    }
    
    public RegularTimePeriod previous() {
        final int serial = this.serialDate.toSerial();
        if (serial > 2) {
            final SerialDate yesterday = SerialDate.createInstance(serial - 1);
            return new Day(yesterday);
        }
        final Day result = null;
        return result;
    }
    
    public RegularTimePeriod next() {
        final int serial = this.serialDate.toSerial();
        if (serial < 2958465) {
            final SerialDate tomorrow = SerialDate.createInstance(serial + 1);
            return new Day(tomorrow);
        }
        final Day result = null;
        return result;
    }
    
    public long getSerialIndex() {
        return this.serialDate.toSerial();
    }
    
    public long getFirstMillisecond(final Calendar calendar) {
        final int year = this.serialDate.getYYYY();
        final int month = this.serialDate.getMonth();
        final int day = this.serialDate.getDayOfMonth();
        calendar.clear();
        calendar.set(year, month - 1, day, 0, 0, 0);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }
    
    public long getLastMillisecond(final Calendar calendar) {
        final int year = this.serialDate.getYYYY();
        final int month = this.serialDate.getMonth();
        final int day = this.serialDate.getDayOfMonth();
        calendar.clear();
        calendar.set(year, month - 1, day, 23, 59, 59);
        calendar.set(14, 999);
        return calendar.getTime().getTime();
    }
    
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Day)) {
            return false;
        }
        final Day that = (Day)obj;
        return this.serialDate.equals(that.getSerialDate());
    }
    
    public int hashCode() {
        return this.serialDate.hashCode();
    }
    
    public int compareTo(final Object o1) {
        int result;
        if (o1 instanceof Day) {
            final Day d = (Day)o1;
            result = -d.getSerialDate().compare(this.serialDate);
        }
        else if (o1 instanceof RegularTimePeriod) {
            result = 0;
        }
        else {
            result = 1;
        }
        return result;
    }
    
    public String toString() {
        return this.serialDate.toString();
    }
    
    public static Day parseDay(final String s) {
        try {
            return new Day(Day.DATE_FORMAT.parse(s));
        }
        catch (ParseException e1) {
            try {
                return new Day(Day.DATE_FORMAT_SHORT.parse(s));
            }
            catch (ParseException e2) {
                return null;
            }
        }
    }
    
    static {
        XXXmyClinitXXX();
    }
    
    public static void XXXmyClinitXXX() {
        Clinit.logClinit("org/jfree/data/time/Day");
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT_SHORT = DateFormat.getDateInstance(3);
        DATE_FORMAT_MEDIUM = DateFormat.getDateInstance(2);
        DATE_FORMAT_LONG = DateFormat.getDateInstance(1);
    }
}
