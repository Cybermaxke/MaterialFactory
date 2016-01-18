package net.minecraft.launchwrapper;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

public class LaunchClassLoader extends URLClassLoader {

    private final URLClassLoader parent;
    private final List<IClassTransformer> classTransformers = new ArrayList<>();
    private final List<IClassTransformer> unmodifiableClassTransformers =
            Collections.unmodifiableList(this.classTransformers);
    private final Set<String> transformerExceptions = new HashSet<>();
    private final Set<String> unmodifiableTransformerExceptions =
            Collections.unmodifiableSet(this.transformerExceptions);
    // This set is accessed by Mixins, even though we don't use it
    private final Set<String> invalidClasses = new HashSet<String>();

    public LaunchClassLoader(URLClassLoader parent) {
        super(new URL[0], parent.getParent());
        this.parent = parent;
    }
    
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.parent.loadClass(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return this.parent.getResources(name);
    }

    @Override
    public URL getResource(String name) {
        return this.parent.getResource(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return this.parent.getResourceAsStream(name);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        return this.parent.findResources(name);
    }

    @Override
    public URL[] getURLs() {
        return this.parent.getURLs();
    }

    @Override
    public void addURL(URL url) {
        try {
            final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(this.parent, url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        this.parent.close();
    }

    public List<URL> getSources() {
        return Arrays.asList(this.getURLs());
    }

    public List<IClassTransformer> getTransformers() {
        return this.unmodifiableClassTransformers;
    }

    public void addClassLoaderExclusion(String toExclude) {
        this.transformerExceptions.add(toExclude);
    }

    public void addTransformerExclusion(String toExclude) {
        this.transformerExceptions.add(toExclude);
    }

    public Set<String> getTransformerExclusions() {
        return this.unmodifiableTransformerExceptions;
    }

    public byte[] getClassBytes(String name) throws IOException {
        final InputStream stream = this.getResourceAsStream("/" + name.replace('.', '/') + ".class");
        return stream == null ? null : ByteStreams.toByteArray(stream);
    }

    public void clearNegativeEntries(Set<String> entriesToClear) {
    }

    public void registerTransformer(String transformerClassName) {
        try {
            this.classTransformers.add((IClassTransformer) this.loadClass(transformerClassName).newInstance());
            System.out.println("Transformer registered: " + transformerClassName);
        } catch (Exception e) {
            LoggerFactory.getLogger("LaunchWrapper").error(
            		"A critical problem occurred registering the ASM transformer class {}", transformerClassName, e);
        }
    }
}
