package com.thoughtworks.xstream;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.BigIntegerConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.converters.basic.CharConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.converters.basic.ShortConverter;
import com.thoughtworks.xstream.converters.basic.StringBufferConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.basic.URIConverter;
import com.thoughtworks.xstream.converters.basic.URLConverter;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.converters.collections.BitSetConverter;
import com.thoughtworks.xstream.converters.collections.CharArrayConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.collections.PropertiesConverter;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.converters.collections.TreeSetConverter;
import com.thoughtworks.xstream.converters.extended.ColorConverter;
import com.thoughtworks.xstream.converters.extended.DynamicProxyConverter;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;
import com.thoughtworks.xstream.converters.extended.FileConverter;
import com.thoughtworks.xstream.converters.extended.FontConverter;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.converters.extended.JavaClassConverter;
import com.thoughtworks.xstream.converters.extended.JavaFieldConverter;
import com.thoughtworks.xstream.converters.extended.JavaMethodConverter;
import com.thoughtworks.xstream.converters.extended.LocaleConverter;
import com.thoughtworks.xstream.converters.extended.LookAndFeelConverter;
import com.thoughtworks.xstream.converters.extended.SqlDateConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimeConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.converters.extended.TextAttributeConverter;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SelfStreamingInstanceChecker;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.MapBackedDataHolder;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshallingStrategy;
import com.thoughtworks.xstream.core.util.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.core.util.CustomObjectInputStream;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StatefulWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.AnnotationConfiguration;
import com.thoughtworks.xstream.mapper.ArrayMapper;
import com.thoughtworks.xstream.mapper.AttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.AttributeMapper;
import com.thoughtworks.xstream.mapper.CachingMapper;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;
import com.thoughtworks.xstream.mapper.DefaultImplementationsMapper;
import com.thoughtworks.xstream.mapper.DefaultMapper;
import com.thoughtworks.xstream.mapper.DynamicProxyMapper;
import com.thoughtworks.xstream.mapper.FieldAliasingMapper;
import com.thoughtworks.xstream.mapper.ImmutableTypesMapper;
import com.thoughtworks.xstream.mapper.ImplicitCollectionMapper;
import com.thoughtworks.xstream.mapper.LocalConversionMapper;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.mapper.OuterClassMapper;
import com.thoughtworks.xstream.mapper.PackageAliasingMapper;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.XStream11XmlFriendlyMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class XStream
{
  private ReflectionProvider reflectionProvider;
  private HierarchicalStreamDriver hierarchicalStreamDriver;
  private ClassLoaderReference classLoaderReference;
  private MarshallingStrategy marshallingStrategy;
  private ConverterLookup converterLookup;
  private ConverterRegistry converterRegistry;
  private Mapper mapper;
  private PackageAliasingMapper packageAliasingMapper;
  private ClassAliasingMapper classAliasingMapper;
  private FieldAliasingMapper fieldAliasingMapper;
  private AttributeAliasingMapper attributeAliasingMapper;
  private SystemAttributeAliasingMapper systemAttributeAliasingMapper;
  private AttributeMapper attributeMapper;
  private DefaultImplementationsMapper defaultImplementationsMapper;
  private ImmutableTypesMapper immutableTypesMapper;
  private ImplicitCollectionMapper implicitCollectionMapper;
  private LocalConversionMapper localConversionMapper;
  private AnnotationConfiguration annotationConfiguration;
  private transient JVM jvm = new JVM();
  public static final int NO_REFERENCES = 1001;
  public static final int ID_REFERENCES = 1002;
  public static final int XPATH_RELATIVE_REFERENCES = 1003;
  public static final int XPATH_ABSOLUTE_REFERENCES = 1004;
  public static final int SINGLE_NODE_XPATH_RELATIVE_REFERENCES = 1005;
  public static final int SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES = 1006;
  public static final int PRIORITY_VERY_HIGH = 10000;
  public static final int PRIORITY_NORMAL = 0;
  public static final int PRIORITY_LOW = -10;
  public static final int PRIORITY_VERY_LOW = -20;
  private static final String ANNOTATION_MAPPER_TYPE = "com.thoughtworks.xstream.mapper.AnnotationMapper";
  
  public XStream()
  {
    this(null, (Mapper)null, new XppDriver());
  }
  
  public XStream(ReflectionProvider reflectionProvider)
  {
    this(reflectionProvider, (Mapper)null, new XppDriver());
  }
  
  public XStream(HierarchicalStreamDriver hierarchicalStreamDriver)
  {
    this(null, (Mapper)null, hierarchicalStreamDriver);
  }
  
  public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver hierarchicalStreamDriver)
  {
    this(reflectionProvider, (Mapper)null, hierarchicalStreamDriver);
  }
  
  /**
   * @deprecated
   */
  public XStream(ReflectionProvider reflectionProvider, Mapper mapper, HierarchicalStreamDriver driver)
  {
    this(reflectionProvider, driver, new ClassLoaderReference(new CompositeClassLoader()), mapper, new DefaultConverterLookup(), null);
  }
  
  public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader)
  {
    this(reflectionProvider, driver, classLoader, null);
  }
  
  public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper)
  {
    this(reflectionProvider, driver, classLoader, mapper, new DefaultConverterLookup(), null);
  }
  
  public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry)
  {
    jvm = new JVM();
    if (reflectionProvider == null) {
      reflectionProvider = jvm.bestReflectionProvider();
    }
    this.reflectionProvider = reflectionProvider;
    hierarchicalStreamDriver = driver;
    classLoaderReference = ((classLoader instanceof ClassLoaderReference) ? (ClassLoaderReference)classLoader : new ClassLoaderReference(classLoader));
    
    this.converterLookup = converterLookup;
    this.converterRegistry = ((converterLookup instanceof ConverterRegistry) ? (ConverterRegistry)converterLookup : converterRegistry != null ? converterRegistry : null);
    
    this.mapper = (mapper == null ? buildMapper() : mapper);
    
    setupMappers();
    setupAliases();
    setupDefaultImplementations();
    setupConverters();
    setupImmutableTypes();
    setMode(1003);
  }
  
  private Mapper buildMapper()
  {
    Mapper mapper = new DefaultMapper(classLoaderReference);
    if (useXStream11XmlFriendlyMapper()) {
      mapper = new XStream11XmlFriendlyMapper(mapper);
    }
    mapper = new DynamicProxyMapper(mapper);
    mapper = new PackageAliasingMapper(mapper);
    mapper = new ClassAliasingMapper(mapper);
    mapper = new FieldAliasingMapper(mapper);
    mapper = new AttributeAliasingMapper(mapper);
    mapper = new SystemAttributeAliasingMapper(mapper);
    mapper = new ImplicitCollectionMapper(mapper);
    mapper = new OuterClassMapper(mapper);
    mapper = new ArrayMapper(mapper);
    mapper = new DefaultImplementationsMapper(mapper);
    mapper = new AttributeMapper(mapper, converterLookup, reflectionProvider);
    if (JVM.is15()) {
      mapper = buildMapperDynamically("com.thoughtworks.xstream.mapper.EnumMapper", new Class[] { Mapper.class }, new Object[] { mapper });
    }
    mapper = new LocalConversionMapper(mapper);
    mapper = new ImmutableTypesMapper(mapper);
    if (JVM.is15()) {
      mapper = buildMapperDynamically("com.thoughtworks.xstream.mapper.AnnotationMapper", new Class[] { Mapper.class, ConverterRegistry.class, ClassLoader.class, ReflectionProvider.class, JVM.class }, new Object[] { mapper, converterLookup, classLoaderReference, reflectionProvider, jvm });
    }
    mapper = wrapMapper((MapperWrapper)mapper);
    mapper = new CachingMapper(mapper);
    return mapper;
  }
  
  private Mapper buildMapperDynamically(String className, Class[] constructorParamTypes, Object[] constructorParamValues)
  {
    try
    {
      Class type = Class.forName(className, false, classLoaderReference.getReference());
      Constructor constructor = type.getConstructor(constructorParamTypes);
      return (Mapper)constructor.newInstance(constructorParamValues);
    }
    catch (Exception e)
    {
      throw new InitializationException("Could not instantiate mapper : " + className, e);
    }
  }
  
  protected MapperWrapper wrapMapper(MapperWrapper next)
  {
    return next;
  }
  
  protected boolean useXStream11XmlFriendlyMapper()
  {
    return false;
  }
  
  private void setupMappers()
  {
    packageAliasingMapper = ((PackageAliasingMapper)mapper.lookupMapperOfType(PackageAliasingMapper.class));
    
    classAliasingMapper = ((ClassAliasingMapper)mapper.lookupMapperOfType(ClassAliasingMapper.class));
    
    fieldAliasingMapper = ((FieldAliasingMapper)mapper.lookupMapperOfType(FieldAliasingMapper.class));
    
    attributeMapper = ((AttributeMapper)mapper.lookupMapperOfType(AttributeMapper.class));
    
    attributeAliasingMapper = ((AttributeAliasingMapper)mapper.lookupMapperOfType(AttributeAliasingMapper.class));
    
    systemAttributeAliasingMapper = ((SystemAttributeAliasingMapper)mapper.lookupMapperOfType(SystemAttributeAliasingMapper.class));
    
    implicitCollectionMapper = ((ImplicitCollectionMapper)mapper.lookupMapperOfType(ImplicitCollectionMapper.class));
    
    defaultImplementationsMapper = ((DefaultImplementationsMapper)mapper.lookupMapperOfType(DefaultImplementationsMapper.class));
    
    immutableTypesMapper = ((ImmutableTypesMapper)mapper.lookupMapperOfType(ImmutableTypesMapper.class));
    
    localConversionMapper = ((LocalConversionMapper)mapper.lookupMapperOfType(LocalConversionMapper.class));
    
    annotationConfiguration = ((AnnotationConfiguration)mapper.lookupMapperOfType(AnnotationConfiguration.class));
  }
  
  protected void setupAliases()
  {
    if (classAliasingMapper == null) {
      return;
    }
    alias("null", Mapper.Null.class);
    alias("int", Integer.class);
    alias("float", Float.class);
    alias("double", Double.class);
    alias("long", Long.class);
    alias("short", Short.class);
    alias("char", Character.class);
    alias("byte", Byte.class);
    alias("boolean", Boolean.class);
    alias("number", Number.class);
    alias("object", Object.class);
    alias("big-int", BigInteger.class);
    alias("big-decimal", BigDecimal.class);
    
    alias("string-buffer", StringBuffer.class);
    alias("string", String.class);
    alias("java-class", Class.class);
    alias("method", Method.class);
    alias("constructor", Constructor.class);
    alias("field", Field.class);
    alias("date", Date.class);
    alias("uri", URI.class);
    alias("url", URL.class);
    alias("bit-set", BitSet.class);
    
    alias("map", Map.class);
    alias("entry", Map.Entry.class);
    alias("properties", Properties.class);
    alias("list", List.class);
    alias("set", Set.class);
    alias("sorted-set", SortedSet.class);
    
    alias("linked-list", LinkedList.class);
    alias("vector", Vector.class);
    alias("tree-map", TreeMap.class);
    alias("tree-set", TreeSet.class);
    alias("hashtable", Hashtable.class);
    if (jvm.supportsAWT())
    {
      alias("awt-color", jvm.loadClass("java.awt.Color"));
      alias("awt-font", jvm.loadClass("java.awt.Font"));
      alias("awt-text-attribute", jvm.loadClass("java.awt.font.TextAttribute"));
    }
    if (jvm.supportsSQL())
    {
      alias("sql-timestamp", jvm.loadClass("java.sql.Timestamp"));
      alias("sql-time", jvm.loadClass("java.sql.Time"));
      alias("sql-date", jvm.loadClass("java.sql.Date"));
    }
    alias("file", File.class);
    alias("locale", Locale.class);
    alias("gregorian-calendar", Calendar.class);
    if (JVM.is14())
    {
      aliasDynamically("auth-subject", "javax.security.auth.Subject");
      alias("linked-hash-map", jvm.loadClass("java.util.LinkedHashMap"));
      alias("linked-hash-set", jvm.loadClass("java.util.LinkedHashSet"));
      alias("trace", jvm.loadClass("java.lang.StackTraceElement"));
      alias("currency", jvm.loadClass("java.util.Currency"));
      aliasType("charset", jvm.loadClass("java.nio.charset.Charset"));
    }
    if (JVM.is15())
    {
      aliasDynamically("duration", "javax.xml.datatype.Duration");
      alias("enum-set", jvm.loadClass("java.util.EnumSet"));
      alias("enum-map", jvm.loadClass("java.util.EnumMap"));
      alias("string-builder", jvm.loadClass("java.lang.StringBuilder"));
      alias("uuid", jvm.loadClass("java.util.UUID"));
    }
  }
  
  private void aliasDynamically(String alias, String className)
  {
    Class type = jvm.loadClass(className);
    if (type != null) {
      alias(alias, type);
    }
  }
  
  protected void setupDefaultImplementations()
  {
    if (defaultImplementationsMapper == null) {
      return;
    }
    addDefaultImplementation(HashMap.class, Map.class);
    addDefaultImplementation(ArrayList.class, List.class);
    addDefaultImplementation(HashSet.class, Set.class);
    addDefaultImplementation(TreeSet.class, SortedSet.class);
    addDefaultImplementation(GregorianCalendar.class, Calendar.class);
  }
  
  protected void setupConverters()
  {
    ReflectionConverter reflectionConverter = new ReflectionConverter(mapper, reflectionProvider);
    
    registerConverter(reflectionConverter, -20);
    
    registerConverter(new SerializableConverter(mapper, reflectionProvider, classLoaderReference), -10);
    
    registerConverter(new ExternalizableConverter(mapper, classLoaderReference), -10);
    
    registerConverter(new NullConverter(), 10000);
    registerConverter(new IntConverter(), 0);
    registerConverter(new FloatConverter(), 0);
    registerConverter(new DoubleConverter(), 0);
    registerConverter(new LongConverter(), 0);
    registerConverter(new ShortConverter(), 0);
    registerConverter(new CharConverter(), 0);
    registerConverter(new BooleanConverter(), 0);
    registerConverter(new ByteConverter(), 0);
    
    registerConverter(new StringConverter(), 0);
    registerConverter(new StringBufferConverter(), 0);
    registerConverter(new DateConverter(), 0);
    registerConverter(new BitSetConverter(), 0);
    registerConverter(new URIConverter(), 0);
    registerConverter(new URLConverter(), 0);
    registerConverter(new BigIntegerConverter(), 0);
    registerConverter(new BigDecimalConverter(), 0);
    
    registerConverter(new ArrayConverter(mapper), 0);
    registerConverter(new CharArrayConverter(), 0);
    registerConverter(new CollectionConverter(mapper), 0);
    registerConverter(new MapConverter(mapper), 0);
    registerConverter(new TreeMapConverter(mapper), 0);
    registerConverter(new TreeSetConverter(mapper), 0);
    registerConverter(new PropertiesConverter(), 0);
    registerConverter(new EncodedByteArrayConverter(), 0);
    
    registerConverter(new FileConverter(), 0);
    if (jvm.supportsSQL())
    {
      registerConverter(new SqlTimestampConverter(), 0);
      registerConverter(new SqlTimeConverter(), 0);
      registerConverter(new SqlDateConverter(), 0);
    }
    registerConverter(new DynamicProxyConverter(mapper, classLoaderReference), 0);
    
    registerConverter(new JavaClassConverter(classLoaderReference), 0);
    registerConverter(new JavaMethodConverter(classLoaderReference), 0);
    registerConverter(new JavaFieldConverter(classLoaderReference), 0);
    if (jvm.supportsAWT())
    {
      registerConverter(new FontConverter(), 0);
      registerConverter(new ColorConverter(), 0);
      registerConverter(new TextAttributeConverter(), 0);
    }
    if (jvm.supportsSwing()) {
      registerConverter(new LookAndFeelConverter(mapper, reflectionProvider), 0);
    }
    registerConverter(new LocaleConverter(), 0);
    registerConverter(new GregorianCalendarConverter(), 0);
    if (JVM.is14())
    {
      registerConverterDynamically("com.thoughtworks.xstream.converters.extended.SubjectConverter", 0, new Class[] { Mapper.class }, new Object[] { mapper });
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.extended.ThrowableConverter", 0, new Class[] { Converter.class }, new Object[] { reflectionConverter });
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.extended.StackTraceElementConverter", 0, null, null);
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.extended.CurrencyConverter", 0, null, null);
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.extended.RegexPatternConverter", 0, new Class[] { Converter.class }, new Object[] { reflectionConverter });
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.extended.CharsetConverter", 0, null, null);
    }
    if (JVM.is15())
    {
      if (jvm.loadClass("javax.xml.datatype.Duration") != null) {
        registerConverterDynamically("com.thoughtworks.xstream.converters.extended.DurationConverter", 0, null, null);
      }
      registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumConverter", 0, null, null);
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumSetConverter", 0, new Class[] { Mapper.class }, new Object[] { mapper });
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.enums.EnumMapConverter", 0, new Class[] { Mapper.class }, new Object[] { mapper });
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.basic.StringBuilderConverter", 0, null, null);
      
      registerConverterDynamically("com.thoughtworks.xstream.converters.basic.UUIDConverter", 0, null, null);
    }
    registerConverter(new SelfStreamingInstanceChecker(reflectionConverter, this), 0);
  }
  
  private void registerConverterDynamically(String className, int priority, Class[] constructorParamTypes, Object[] constructorParamValues)
  {
    try
    {
      Class type = Class.forName(className, false, classLoaderReference.getReference());
      Constructor constructor = type.getConstructor(constructorParamTypes);
      Object instance = constructor.newInstance(constructorParamValues);
      if ((instance instanceof Converter)) {
        registerConverter((Converter)instance, priority);
      } else if ((instance instanceof SingleValueConverter)) {
        registerConverter((SingleValueConverter)instance, priority);
      }
    }
    catch (Exception e)
    {
      throw new InitializationException("Could not instantiate converter : " + className, e);
    }
  }
  
  protected void setupImmutableTypes()
  {
    if (immutableTypesMapper == null) {
      return;
    }
    addImmutableType(Boolean.TYPE);
    addImmutableType(Boolean.class);
    addImmutableType(Byte.TYPE);
    addImmutableType(Byte.class);
    addImmutableType(Character.TYPE);
    addImmutableType(Character.class);
    addImmutableType(Double.TYPE);
    addImmutableType(Double.class);
    addImmutableType(Float.TYPE);
    addImmutableType(Float.class);
    addImmutableType(Integer.TYPE);
    addImmutableType(Integer.class);
    addImmutableType(Long.TYPE);
    addImmutableType(Long.class);
    addImmutableType(Short.TYPE);
    addImmutableType(Short.class);
    
    addImmutableType(Mapper.Null.class);
    addImmutableType(BigDecimal.class);
    addImmutableType(BigInteger.class);
    addImmutableType(String.class);
    addImmutableType(URI.class);
    addImmutableType(URL.class);
    addImmutableType(File.class);
    addImmutableType(Class.class);
    if (jvm.supportsAWT()) {
      addImmutableTypeDynamically("java.awt.font.TextAttribute");
    }
    if (JVM.is14())
    {
      addImmutableTypeDynamically("java.nio.charset.Charset");
      addImmutableTypeDynamically("java.util.Currency");
    }
  }
  
  private void addImmutableTypeDynamically(String className)
  {
    Class type = jvm.loadClass(className);
    if (type != null) {
      addImmutableType(type);
    }
  }
  
  public void setMarshallingStrategy(MarshallingStrategy marshallingStrategy)
  {
    this.marshallingStrategy = marshallingStrategy;
  }
  
  public String toXML(Object obj)
  {
    Writer writer = new StringWriter();
    toXML(obj, writer);
    return writer.toString();
  }
  
  public void toXML(Object obj, Writer out)
  {
    HierarchicalStreamWriter writer = hierarchicalStreamDriver.createWriter(out);
    try
    {
      marshal(obj, writer);
    }
    finally
    {
      writer.flush();
    }
  }
  
  public void toXML(Object obj, OutputStream out)
  {
    HierarchicalStreamWriter writer = hierarchicalStreamDriver.createWriter(out);
    try
    {
      marshal(obj, writer);
    }
    finally
    {
      writer.flush();
    }
  }
  
  public void marshal(Object obj, HierarchicalStreamWriter writer)
  {
    marshal(obj, writer, null);
  }
  
  public void marshal(Object obj, HierarchicalStreamWriter writer, DataHolder dataHolder)
  {
    marshallingStrategy.marshal(writer, obj, converterLookup, mapper, dataHolder);
  }
  
  public Object fromXML(String xml)
  {
    return fromXML(new StringReader(xml));
  }
  
  public Object fromXML(Reader reader)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(reader), null);
  }
  
  public Object fromXML(InputStream input)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(input), null);
  }
  
  public Object fromXML(URL url)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(url), null);
  }
  
  public Object fromXML(File file)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(file), null);
  }
  
  public Object fromXML(String xml, Object root)
  {
    return fromXML(new StringReader(xml), root);
  }
  
  public Object fromXML(Reader xml, Object root)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(xml), root);
  }
  
  public Object fromXML(URL url, Object root)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(url), root);
  }
  
  public Object fromXML(File file, Object root)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(file), root);
  }
  
  public Object fromXML(InputStream input, Object root)
  {
    return unmarshal(hierarchicalStreamDriver.createReader(input), root);
  }
  
  public Object unmarshal(HierarchicalStreamReader reader)
  {
    return unmarshal(reader, null, null);
  }
  
  public Object unmarshal(HierarchicalStreamReader reader, Object root)
  {
    return unmarshal(reader, root, null);
  }
  
  public Object unmarshal(HierarchicalStreamReader reader, Object root, DataHolder dataHolder)
  {
    try
    {
      return marshallingStrategy.unmarshal(root, reader, dataHolder, converterLookup, mapper);
    }
    catch (ConversionException e)
    {
      Package pkg = getClass().getPackage();
      e.add("version", pkg != null ? pkg.getImplementationVersion() : "not available");
      throw e;
    }
  }
  
  public void alias(String name, Class type)
  {
    if (classAliasingMapper == null) {
      throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
    }
    classAliasingMapper.addClassAlias(name, type);
  }
  
  public void aliasType(String name, Class type)
  {
    if (classAliasingMapper == null) {
      throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
    }
    classAliasingMapper.addTypeAlias(name, type);
  }
  
  public void alias(String name, Class type, Class defaultImplementation)
  {
    alias(name, type);
    addDefaultImplementation(defaultImplementation, type);
  }
  
  public void aliasPackage(String name, String pkgName)
  {
    if (packageAliasingMapper == null) {
      throw new InitializationException("No " + PackageAliasingMapper.class.getName() + " available");
    }
    packageAliasingMapper.addPackageAlias(name, pkgName);
  }
  
  public void aliasField(String alias, Class definedIn, String fieldName)
  {
    if (fieldAliasingMapper == null) {
      throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
    }
    fieldAliasingMapper.addFieldAlias(alias, definedIn, fieldName);
  }
  
  public void aliasAttribute(String alias, String attributeName)
  {
    if (attributeAliasingMapper == null) {
      throw new InitializationException("No " + AttributeAliasingMapper.class.getName() + " available");
    }
    attributeAliasingMapper.addAliasFor(attributeName, alias);
  }
  
  public void aliasSystemAttribute(String alias, String systemAttributeName)
  {
    if (systemAttributeAliasingMapper == null) {
      throw new InitializationException("No " + SystemAttributeAliasingMapper.class.getName() + " available");
    }
    systemAttributeAliasingMapper.addAliasFor(systemAttributeName, alias);
  }
  
  public void aliasAttribute(Class definedIn, String attributeName, String alias)
  {
    aliasField(alias, definedIn, attributeName);
    useAttributeFor(definedIn, attributeName);
  }
  
  public void useAttributeFor(String fieldName, Class type)
  {
    if (attributeMapper == null) {
      throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
    }
    attributeMapper.addAttributeFor(fieldName, type);
  }
  
  public void useAttributeFor(Class definedIn, String fieldName)
  {
    if (attributeMapper == null) {
      throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
    }
    attributeMapper.addAttributeFor(definedIn, fieldName);
  }
  
  public void useAttributeFor(Class type)
  {
    if (attributeMapper == null) {
      throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
    }
    attributeMapper.addAttributeFor(type);
  }
  
  public void addDefaultImplementation(Class defaultImplementation, Class ofType)
  {
    if (defaultImplementationsMapper == null) {
      throw new InitializationException("No " + DefaultImplementationsMapper.class.getName() + " available");
    }
    defaultImplementationsMapper.addDefaultImplementation(defaultImplementation, ofType);
  }
  
  public void addImmutableType(Class type)
  {
    if (immutableTypesMapper == null) {
      throw new InitializationException("No " + ImmutableTypesMapper.class.getName() + " available");
    }
    immutableTypesMapper.addImmutableType(type);
  }
  
  public void registerConverter(Converter converter)
  {
    registerConverter(converter, 0);
  }
  
  public void registerConverter(Converter converter, int priority)
  {
    if (converterRegistry != null) {
      converterRegistry.registerConverter(converter, priority);
    }
  }
  
  public void registerConverter(SingleValueConverter converter)
  {
    registerConverter(converter, 0);
  }
  
  public void registerConverter(SingleValueConverter converter, int priority)
  {
    if (converterRegistry != null) {
      converterRegistry.registerConverter(new SingleValueConverterWrapper(converter), priority);
    }
  }
  
  public void registerLocalConverter(Class definedIn, String fieldName, Converter converter)
  {
    if (localConversionMapper == null) {
      throw new InitializationException("No " + LocalConversionMapper.class.getName() + " available");
    }
    localConversionMapper.registerLocalConverter(definedIn, fieldName, converter);
  }
  
  public void registerLocalConverter(Class definedIn, String fieldName, SingleValueConverter converter)
  {
    registerLocalConverter(definedIn, fieldName, new SingleValueConverterWrapper(converter));
  }
  
  public Mapper getMapper()
  {
    return mapper;
  }
  
  public ReflectionProvider getReflectionProvider()
  {
    return reflectionProvider;
  }
  
  public ConverterLookup getConverterLookup()
  {
    return converterLookup;
  }
  
  public void setMode(int mode)
  {
    switch (mode)
    {
    case 1001: 
      setMarshallingStrategy(new TreeMarshallingStrategy());
      break;
    case 1002: 
      setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
      break;
    case 1003: 
      setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE));
      
      break;
    case 1004: 
      setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE));
      
      break;
    case 1005: 
      setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
      
      break;
    case 1006: 
      setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
      
      break;
    default: 
      throw new IllegalArgumentException("Unknown mode : " + mode);
    }
  }
  
  public void addImplicitCollection(Class ownerType, String fieldName)
  {
    addImplicitCollection(ownerType, fieldName, null, null);
  }
  
  public void addImplicitCollection(Class ownerType, String fieldName, Class itemType)
  {
    addImplicitCollection(ownerType, fieldName, null, itemType);
  }
  
  public void addImplicitCollection(Class ownerType, String fieldName, String itemFieldName, Class itemType)
  {
    addImplicitMap(ownerType, fieldName, itemFieldName, itemType, null);
  }
  
  public void addImplicitArray(Class ownerType, String fieldName)
  {
    addImplicitCollection(ownerType, fieldName);
  }
  
  public void addImplicitArray(Class ownerType, String fieldName, Class itemType)
  {
    addImplicitCollection(ownerType, fieldName, itemType);
  }
  
  public void addImplicitArray(Class ownerType, String fieldName, String itemName)
  {
    addImplicitCollection(ownerType, fieldName, itemName, null);
  }
  
  public void addImplicitMap(Class ownerType, String fieldName, Class itemType, String keyFieldName)
  {
    addImplicitMap(ownerType, fieldName, null, itemType, keyFieldName);
  }
  
  public void addImplicitMap(Class ownerType, String fieldName, String itemFieldName, Class itemType, String keyFieldName)
  {
    if (implicitCollectionMapper == null) {
      throw new InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
    }
    implicitCollectionMapper.add(ownerType, fieldName, itemFieldName, itemType, keyFieldName);
  }
  
  public DataHolder newDataHolder()
  {
    return new MapBackedDataHolder();
  }
  
  public ObjectOutputStream createObjectOutputStream(Writer writer)
    throws IOException
  {
    return createObjectOutputStream(hierarchicalStreamDriver.createWriter(writer), "object-stream");
  }
  
  public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer)
    throws IOException
  {
    return createObjectOutputStream(writer, "object-stream");
  }
  
  public ObjectOutputStream createObjectOutputStream(Writer writer, String rootNodeName)
    throws IOException
  {
    return createObjectOutputStream(hierarchicalStreamDriver.createWriter(writer), rootNodeName);
  }
  
  public ObjectOutputStream createObjectOutputStream(OutputStream out)
    throws IOException
  {
    return createObjectOutputStream(hierarchicalStreamDriver.createWriter(out), "object-stream");
  }
  
  public ObjectOutputStream createObjectOutputStream(OutputStream out, String rootNodeName)
    throws IOException
  {
    return createObjectOutputStream(hierarchicalStreamDriver.createWriter(out), rootNodeName);
  }
  
  public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer, String rootNodeName)
    throws IOException
  {
    StatefulWriter statefulWriter = new StatefulWriter(writer);
    statefulWriter.startNode(rootNodeName, null);
    return new CustomObjectOutputStream(new XStream.1(this, statefulWriter));
  }
  
  public ObjectInputStream createObjectInputStream(Reader xmlReader)
    throws IOException
  {
    return createObjectInputStream(hierarchicalStreamDriver.createReader(xmlReader));
  }
  
  public ObjectInputStream createObjectInputStream(InputStream in)
    throws IOException
  {
    return createObjectInputStream(hierarchicalStreamDriver.createReader(in));
  }
  
  public ObjectInputStream createObjectInputStream(HierarchicalStreamReader reader)
    throws IOException
  {
    return new CustomObjectInputStream(new XStream.2(this, reader), classLoaderReference);
  }
  
  public void setClassLoader(ClassLoader classLoader)
  {
    classLoaderReference.setReference(classLoader);
  }
  
  public ClassLoader getClassLoader()
  {
    return classLoaderReference.getReference();
  }
  
  public void omitField(Class definedIn, String fieldName)
  {
    if (fieldAliasingMapper == null) {
      throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
    }
    fieldAliasingMapper.omitField(definedIn, fieldName);
  }
  
  public void processAnnotations(Class[] types)
  {
    if (annotationConfiguration == null) {
      throw new InitializationException("No com.thoughtworks.xstream.mapper.AnnotationMapper available");
    }
    annotationConfiguration.processAnnotations(types);
  }
  
  public void processAnnotations(Class type)
  {
    processAnnotations(new Class[] { type });
  }
  
  public void autodetectAnnotations(boolean mode)
  {
    if (annotationConfiguration != null) {
      annotationConfiguration.autodetectAnnotations(mode);
    }
  }
  
  private Object readResolve()
  {
    jvm = new JVM();
    return this;
  }
}
