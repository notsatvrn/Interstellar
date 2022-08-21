// Original code by YatopiaMC, licensed under MIT
// You can find the original code on https://github.com/YatopiaMC/Yatopia

package com.notsatvrn.graphene;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class StackTraceUtils {

    public static final String EXCEPTION_DETAILS_BELOW = "Exception details below: ";

    private static final Supplier<Map<Plugin, Set<Class<?>>>> loadedClassesSupplier = Suppliers.memoizeWithExpiration(StackTraceUtils::scanForPluginClasses, 5, TimeUnit.SECONDS);

    public static void print(StackTraceElement[] stackTrace, Consumer<String> out) {
        Set<Plugin> suspectedPlugins = getSuspectedPluginsFromStackTrace(stackTrace);

        printSuspectedPlugins(out, suspectedPlugins);
    }

    public static void print(Throwable t, Consumer<String> out) {
        Set<Plugin> suspectedPlugins = getSuspectedPluginsFromStackTrace(getStackTracesFromThrowable(t).toArray(new StackTraceElement[0]));

        printSuspectedPlugins(out, suspectedPlugins);
    }

    private static Set<StackTraceElement> getStackTracesFromThrowable(Throwable t) {
        if(t == null) return Collections.emptySet();
        Set<StackTraceElement> elements = new ObjectOpenHashSet<>();
        elements.addAll(getStackTracesFromThrowable(t.getCause()));
        elements.addAll(Arrays.stream(t.getSuppressed()).flatMap(throwable -> getStackTracesFromThrowable(throwable).stream()).collect(Collectors.toSet()));
        elements.addAll(Arrays.asList(t.getStackTrace()));
        return elements;
    }

    private static void printSuspectedPlugins(Consumer<String> out, Set<Plugin> suspectedPlugins) {
        if (!suspectedPlugins.isEmpty()) {
            out.accept("Suspected Plugins: ");
            for (Plugin plugin : suspectedPlugins) {
                StringBuilder builder = new StringBuilder("\t");
                builder.append(plugin.getName())
                        .append("{")
                        .append(plugin.isEnabled() ? "enabled" : "disabled")
                        .append(",").append("ver=").append(plugin.getDescription().getVersion());
                if (!plugin.isNaggable())
                    builder.append(",").append("nag");
                if (plugin instanceof JavaPlugin)
                    builder.append(",").append("path=").append(((JavaPlugin) plugin).getFile());

                builder.append("}");
                out.accept(builder.toString());
            }
        } else {
            out.accept("Suspected Plugins: None");
        }
    }

    private static Set<Plugin> getSuspectedPluginsFromStackTrace(StackTraceElement[] stackTrace) {
        Map<Plugin, Set<Class<?>>> loadedClasses = loadedClassesSupplier.get();
        Set<Plugin> suspectedPlugins = new HashSet<>();
        for (StackTraceElement stackTraceElement : stackTrace) {
            for (Map.Entry<Plugin, Set<Class<?>>> pluginSetEntry : loadedClasses.entrySet()) {
                if (pluginSetEntry.getValue().stream().anyMatch(clazz -> clazz.getName().equals(stackTraceElement.getClassName())))
                    suspectedPlugins.add(pluginSetEntry.getKey());
            }
        }
        return suspectedPlugins;
    }

    private static Map<Plugin, Set<Class<?>>> scanForPluginClasses() {
        Map<Plugin, Set<Class<?>>> loadedClasses = new Object2ObjectOpenHashMap<>();
        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            final SimplePluginManager pluginManager = (SimplePluginManager) Bukkit.getPluginManager();
            final Collection<PluginLoader> pluginLoaders = pluginManager.getPluginLoaders();
            for (PluginLoader pluginLoader : pluginLoaders) {
                if (pluginLoader instanceof JavaPluginLoader) {
                    JavaPluginLoader javaPluginLoader = (JavaPluginLoader) pluginLoader;
                    final List<PluginClassLoader> classLoaders = javaPluginLoader.getClassLoaders();
                    for (PluginClassLoader classLoader : classLoaders) {
                        loadedClasses.put(classLoader.getPlugin(), new ObjectOpenHashSet<>(classLoader.getLoadedClasses()));
                    }
                }
            }
        }
        return loadedClasses;
    }

}

