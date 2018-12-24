/**
 * This class demonstrates how to use ConfigurationBuilderFactory directly,
 * as described in Section 3 of "Programmatic Configuration with Log4j 2"
 **/

package com.dev.logging.log4j2.simpleconfigurator;

import com.dev.logging.log4j2.Log4j2BaseIntegrationTest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleConfiguratorIntegrationTest extends Log4j2BaseIntegrationTest {

    @Test
    public void givenDefaultLog4j2Environment_whenProgrammaticallyConfigured_thenLogsCorrectly() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        AppenderComponentBuilder console = builder.newAppender("Stdout", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
//        console.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));

        console.add(builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %l - %m%n"));


        LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");
        console.add(layout);

//        FilterComponentBuilder filter = builder.newFilter("MarkerFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
//        filter.addAttribute("marker", "FLOW");
//        console.add(filter);
//        builder.add(console);

        ComponentBuilder triggeringPolicies = builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy")
                        .addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", "100M"));
        AppenderComponentBuilder rollingFile = builder.newAppender("rolling", "RollingFile");
        rollingFile.addAttribute("fileName", "target/rolling2.log");
        rollingFile.addAttribute("filePattern", "target/archive/rolling-%d{MM-dd-yy}.log.gz");
        rollingFile.add(layout);
        rollingFile.addComponent(triggeringPolicies);
        builder.add(rollingFile);


        builder.add(console);
        builder.add(builder.newLogger("com", Level.DEBUG)
                .add(builder.newAppenderRef("Stdout"))
                .addAttribute("additivity", false));
        builder.add(builder.newRootLogger(Level.ERROR)
                .add(builder.newAppenderRef("Stdout")));
        Configurator.initialize(builder.build());
        LogPrinter logPrinter = new LogPrinter();
        logPrinter.printlog();
    }
}
