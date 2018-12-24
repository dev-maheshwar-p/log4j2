/**
  This class demonstrates how to build the components of 
  the configuration factory, as described in Section 3 of 
  "Programmatic Configuration with Log4j 2"
**/
package com.dev.logging.log4j2.simpleconfiguration;

import java.io.IOException;
import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "custom", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class CustomConfigurationFactory extends ConfigurationFactory {

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        AppenderComponentBuilder console = builder.newAppender("Stdout", "Console");

//        LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
//            .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");

        LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %l - %m%n");

        console.add(layout);
        FilterComponentBuilder filter = builder.newFilter("MarkerFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        filter.addAttribute("marker", "FLOW");
        console.add(filter);
        builder.add(console);
        ComponentBuilder triggeringPolicies = builder.newComponent("Policies")
            .addComponent(builder.newComponent("CronTriggeringPolicy")
                .addAttribute("schedule", "0 0 0 * * ?"))
            .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                .addAttribute("size", "100M"));
        AppenderComponentBuilder rollingFile = builder.newAppender("rolling", "RollingFile");
        rollingFile.addAttribute("fileName", "target/rolling.log");
        rollingFile.addAttribute("filePattern", "target/archive/rolling-%d{MM-dd-yy}.log.gz");
        rollingFile.add(layout);
        rollingFile.addComponent(triggeringPolicies);
        builder.add(rollingFile);
        AppenderComponentBuilder file = builder.newAppender("FileSystem", "File");
        file.addAttribute("fileName", "target/logging.log");
        file.add(layout);
        builder.add(file);
        LoggerComponentBuilder logger = builder.newLogger("com", Level.DEBUG);
        logger.add(builder.newAppenderRef("Stdout"));
        logger.add(builder.newAppenderRef("rolling"));
        logger.add(builder.newAppenderRef("FileSystem"));
        logger.addAttribute("additivity", false);
        builder.add(logger);
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.ERROR);
        rootLogger.add(builder.newAppenderRef("Stdout"));
        rootLogger.add(builder.newAppenderRef("rolling"));
        // rootLogger.add(builder.newAppenderRef("syslogAppender"));
        rootLogger.add(builder.newAppenderRef("FileSystem"));
        rootLogger.addAttribute("additivity", false);
        builder.add(rootLogger);
        try {
            builder.writeXmlConfiguration(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.build();

    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] { "*" };
    }
}

