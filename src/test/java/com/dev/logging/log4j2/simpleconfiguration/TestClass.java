package com.dev.logging.log4j2.simpleconfiguration;

import com.dev.logging.log4j2.Log4j2BaseIntegrationTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestClass  extends Log4j2BaseIntegrationTest {

    Logger logger = LogManager.getLogger(this.getClass());

    @BeforeClass
    public static void setUp() {
        PluginManager.addPackage("com.dev.logging.log4j2.simpleconfiguration");
    }

    @Test
    public void test1(){
        Marker markerContent = MarkerManager.getMarker("FLOW");
        logger.debug(markerContent, "Debug log message");
        logger.info(markerContent, "Info log message");
        logger.error(markerContent, "Error log message");


    }

}
