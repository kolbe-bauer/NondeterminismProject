// 
// Decompiled by Procyon v0.5.36
// 

package java141.util.logging;

import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.util.MissingResourceException;
import sun.reflect.Reflection;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.ArrayList;
import java.util.logging.Handler;

public class Logger
{
    private static final Handler[] emptyHandlers;
    private static final int offValue;
    private LogManager manager;
    private String name;
    private ArrayList handlers;
    private String resourceBundleName;
    private boolean useParentHandlers;
    private Filter filter;
    private boolean anonymous;
    private ResourceBundle catalog;
    private String catalogName;
    private Locale catalogLocale;
    private static Object treeLock;
    private Logger parent;
    private ArrayList kids;
    private Level levelObject;
    private volatile int levelValue;
    public static final Logger global;
    
    static {
        emptyHandlers = new Handler[0];
        offValue = Level.OFF.intValue();
        Logger.treeLock = new Object();
        global = getLogger("global");
    }
    
    protected Logger(final String name, final String resourceBundleName) {
        this.manager = LogManager.getLogManager();
        this.useParentHandlers = true;
        if (resourceBundleName != null) {
            this.setupResourceInfo(resourceBundleName);
        }
        this.name = name;
        this.levelValue = Level.INFO.intValue();
    }
    
    public static synchronized Logger getLogger(final String name) {
        final LogManager manager = LogManager.getLogManager();
        Logger result = manager.getLogger(name);
        if (result == null) {
            result = new Logger(name, null);
            manager.addLogger(result);
            result = manager.getLogger(name);
        }
        return result;
    }
    
    public static synchronized Logger getLogger(final String name, final String resourceBundleName) {
        final LogManager manager = LogManager.getLogManager();
        Logger result = manager.getLogger(name);
        if (result == null) {
            result = new Logger(name, resourceBundleName);
            manager.addLogger(result);
            result = manager.getLogger(name);
        }
        if (result.resourceBundleName == null) {
            result.setupResourceInfo(resourceBundleName);
        }
        else if (!result.resourceBundleName.equals(resourceBundleName)) {
            throw new IllegalArgumentException(String.valueOf(result.resourceBundleName) + " != " + resourceBundleName);
        }
        return result;
    }
    
    public static synchronized Logger getAnonymousLogger() {
        final LogManager manager = LogManager.getLogManager();
        final Logger result = new Logger(null, null);
        result.anonymous = true;
        final Logger root = manager.getLogger("");
        result.doSetParent(root);
        return result;
    }
    
    public static synchronized Logger getAnonymousLogger(final String resourceBundleName) {
        final LogManager manager = LogManager.getLogManager();
        final Logger result = new Logger(null, resourceBundleName);
        result.anonymous = true;
        final Logger root = manager.getLogger("");
        result.doSetParent(root);
        return result;
    }
    
    public ResourceBundle getResourceBundle() {
        return this.findResourceBundle(this.getResourceBundleName());
    }
    
    public String getResourceBundleName() {
        return this.resourceBundleName;
    }
    
    public void setFilter(final Filter newFilter) throws SecurityException {
        if (!this.anonymous) {
            this.manager.checkAccess();
        }
        this.filter = newFilter;
    }
    
    public Filter getFilter() {
        return this.filter;
    }
    
    public void log(final LogRecord record) {
        if (record.getLevel().intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        synchronized (this) {
            if (this.filter != null) {
                try {
                    Thread.sleep(200L);
                }
                catch (InterruptedException ex) {}
                if (!this.filter.isLoggable(record)) {
                    // monitorexit(this)
                    return;
                }
            }
        }
        for (Logger logger = this; logger != null; logger = logger.getParent()) {
            final Handler[] targets = logger.getHandlers();
            if (targets != null) {
                for (int i = 0; i < targets.length; ++i) {
                    targets[i].publish(record);
                }
            }
            if (!logger.getUseParentHandlers()) {
                break;
            }
        }
    }
    
    private void doLog(final LogRecord lr) {
        lr.setLoggerName(this.name);
        final String ebname = this.getEffectiveResourceBundleName();
        if (ebname != null) {
            lr.setResourceBundleName(ebname);
            lr.setResourceBundle(this.findResourceBundle(ebname));
        }
        this.log(lr);
    }
    
    public void log(final Level level, final String msg) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        this.doLog(lr);
    }
    
    public void log(final Level level, final String msg, final Object param1) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        final Object[] params = { param1 };
        lr.setParameters(params);
        this.doLog(lr);
    }
    
    public void log(final Level level, final String msg, final Object[] params) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setParameters(params);
        this.doLog(lr);
    }
    
    public void log(final Level level, final String msg, final Throwable thrown) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setThrown(thrown);
        this.doLog(lr);
    }
    
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        this.doLog(lr);
    }
    
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg, final Object param1) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        final Object[] params = { param1 };
        lr.setParameters(params);
        this.doLog(lr);
    }
    
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg, final Object[] params) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        lr.setParameters(params);
        this.doLog(lr);
    }
    
    public void logp(final Level level, final String sourceClass, final String sourceMethod, final String msg, final Throwable thrown) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        lr.setThrown(thrown);
        this.doLog(lr);
    }
    
    private void doLog(final LogRecord lr, final String rbname) {
        lr.setLoggerName(this.name);
        if (rbname != null) {
            lr.setResourceBundleName(rbname);
            lr.setResourceBundle(this.findResourceBundle(rbname));
        }
        this.log(lr);
    }
    
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName, final String msg) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        this.doLog(lr, bundleName);
    }
    
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName, final String msg, final Object param1) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        final Object[] params = { param1 };
        lr.setParameters(params);
        this.doLog(lr, bundleName);
    }
    
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName, final String msg, final Object[] params) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        lr.setParameters(params);
        this.doLog(lr, bundleName);
    }
    
    public void logrb(final Level level, final String sourceClass, final String sourceMethod, final String bundleName, final String msg, final Throwable thrown) {
        if (level.intValue() < this.levelValue || this.levelValue == Logger.offValue) {
            return;
        }
        final LogRecord lr = new LogRecord(level, msg);
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        lr.setThrown(thrown);
        this.doLog(lr, bundleName);
    }
    
    public void entering(final String sourceClass, final String sourceMethod) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        this.logp(Level.FINER, sourceClass, sourceMethod, "ENTRY");
    }
    
    public void entering(final String sourceClass, final String sourceMethod, final Object param1) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        final Object[] params = { param1 };
        this.logp(Level.FINER, sourceClass, sourceMethod, "ENTRY {0}", params);
    }
    
    public void entering(final String sourceClass, final String sourceMethod, final Object[] params) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        String msg = "ENTRY";
        for (int i = 0; i < params.length; ++i) {
            msg = String.valueOf(msg) + " {" + i + "}";
        }
        this.logp(Level.FINER, sourceClass, sourceMethod, msg, params);
    }
    
    public void exiting(final String sourceClass, final String sourceMethod) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        this.logp(Level.FINER, sourceClass, sourceMethod, "RETURN");
    }
    
    public void exiting(final String sourceClass, final String sourceMethod, final Object result) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        final Object[] params = { result };
        this.logp(Level.FINER, sourceClass, sourceMethod, "RETURN {0}", result);
    }
    
    public void throwing(final String sourceClass, final String sourceMethod, final Throwable thrown) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        final LogRecord lr = new LogRecord(Level.FINER, "THROW");
        lr.setSourceClassName(sourceClass);
        lr.setSourceMethodName(sourceMethod);
        lr.setThrown(thrown);
        this.doLog(lr);
    }
    
    public void severe(final String msg) {
        if (Level.SEVERE.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.SEVERE, msg);
    }
    
    public void warning(final String msg) {
        if (Level.WARNING.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.WARNING, msg);
    }
    
    public void info(final String msg) {
        if (Level.INFO.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.INFO, msg);
    }
    
    public void config(final String msg) {
        if (Level.CONFIG.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.CONFIG, msg);
    }
    
    public void fine(final String msg) {
        if (Level.FINE.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.FINE, msg);
    }
    
    public void finer(final String msg) {
        if (Level.FINER.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.FINER, msg);
    }
    
    public void finest(final String msg) {
        if (Level.FINEST.intValue() < this.levelValue) {
            return;
        }
        this.log(Level.FINEST, msg);
    }
    
    public void setLevel(final Level newLevel) throws SecurityException {
        if (!this.anonymous) {
            this.manager.checkAccess();
        }
        synchronized (Logger.treeLock) {
            this.levelObject = newLevel;
            this.updateEffectiveLevel();
        }
        // monitorexit(Logger.treeLock)
    }
    
    public Level getLevel() {
        return this.levelObject;
    }
    
    public boolean isLoggable(final Level level) {
        return level.intValue() >= this.levelValue && this.levelValue != Logger.offValue;
    }
    
    public String getName() {
        return this.name;
    }
    
    public synchronized void addHandler(final Handler handler) throws SecurityException {
        handler.getClass();
        if (!this.anonymous) {
            this.manager.checkAccess();
        }
        if (this.handlers == null) {
            this.handlers = new ArrayList();
        }
        this.handlers.add(handler);
    }
    
    public synchronized void removeHandler(final Handler handler) throws SecurityException {
        if (!this.anonymous) {
            this.manager.checkAccess();
        }
        if (handler == null) {
            throw new NullPointerException();
        }
        if (this.handlers == null) {
            return;
        }
        this.handlers.remove(handler);
    }
    
    public synchronized Handler[] getHandlers() {
        if (this.handlers == null) {
            return Logger.emptyHandlers;
        }
        Handler[] result = new Handler[this.handlers.size()];
        result = this.handlers.toArray(result);
        return result;
    }
    
    public synchronized void setUseParentHandlers(final boolean useParentHandlers) {
        if (!this.anonymous) {
            this.manager.checkAccess();
        }
        this.useParentHandlers = useParentHandlers;
    }
    
    public synchronized boolean getUseParentHandlers() {
        return this.useParentHandlers;
    }
    
    private synchronized ResourceBundle findResourceBundle(final String name) {
        if (name == null) {
            return null;
        }
        final Locale currentLocale = Locale.getDefault();
        if (this.catalog != null && currentLocale == this.catalogLocale && name == this.catalogName) {
            return this.catalog;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        try {
            this.catalog = ResourceBundle.getBundle(name, currentLocale, cl);
            this.catalogName = name;
            this.catalogLocale = currentLocale;
            return this.catalog;
        }
        catch (MissingResourceException ex) {
            int ix = 0;
            while (true) {
                final Class clz = Reflection.getCallerClass(ix);
                if (clz == null) {
                    break;
                }
                ClassLoader cl2 = clz.getClassLoader();
                if (cl2 == null) {
                    cl2 = ClassLoader.getSystemClassLoader();
                }
                if (cl != cl2) {
                    cl = cl2;
                    try {
                        this.catalog = ResourceBundle.getBundle(name, currentLocale, cl);
                        this.catalogName = name;
                        this.catalogLocale = currentLocale;
                        return this.catalog;
                    }
                    catch (MissingResourceException ex2) {}
                }
                ++ix;
            }
            if (name.equals(this.catalogName)) {
                return this.catalog;
            }
            return null;
        }
    }
    
    private synchronized void setupResourceInfo(final String name) {
        if (name == null) {
            return;
        }
        final ResourceBundle rb = this.findResourceBundle(name);
        if (rb == null) {
            throw new MissingResourceException("Can't find " + name + " bundle", name, "");
        }
        this.resourceBundleName = name;
    }
    
    public Logger getParent() {
        synchronized (Logger.treeLock) {
            // monitorexit(Logger.treeLock)
            return this.parent;
        }
    }
    
    public void setParent(final Logger parent) {
        if (parent == null) {
            throw new NullPointerException();
        }
        this.manager.checkAccess();
        this.doSetParent(parent);
    }
    
    private void doSetParent(final Logger newParent) {
        synchronized (Logger.treeLock) {
            if (this.parent != null) {
                final Iterator iter = this.parent.kids.iterator();
                while (iter.hasNext()) {
                    final WeakReference ref = iter.next();
                    final Logger kid = (Logger)ref.get();
                    if (kid == this) {
                        iter.remove();
                        break;
                    }
                }
            }
            this.parent = newParent;
            if (this.parent.kids == null) {
                this.parent.kids = new ArrayList(2);
            }
            this.parent.kids.add(new WeakReference<Logger>(this));
            this.updateEffectiveLevel();
        }
        // monitorexit(Logger.treeLock)
    }
    
    private void updateEffectiveLevel() {
        int newLevelValue;
        if (this.levelObject != null) {
            newLevelValue = this.levelObject.intValue();
        }
        else if (this.parent != null) {
            newLevelValue = this.parent.levelValue;
        }
        else {
            newLevelValue = Level.INFO.intValue();
        }
        if (this.levelValue == newLevelValue) {
            return;
        }
        this.levelValue = newLevelValue;
        if (this.kids != null) {
            for (int i = 0; i < this.kids.size(); ++i) {
                final WeakReference ref = this.kids.get(i);
                final Logger kid = (Logger)ref.get();
                if (kid != null) {
                    kid.updateEffectiveLevel();
                }
            }
        }
    }
    
    private String getEffectiveResourceBundleName() {
        for (Logger target = this; target != null; target = target.getParent()) {
            final String rbn = target.getResourceBundleName();
            if (rbn != null) {
                return rbn;
            }
        }
        return null;
    }
}
