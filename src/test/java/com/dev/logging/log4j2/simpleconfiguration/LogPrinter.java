package com.dev.logging.log4j2.simpleconfiguration;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;


public class LogPrinter {
    private Logger logger = LogManager.getLogger(this.getClass());

    public void printlog() {


        Marker markerContent = MarkerManager.getMarker("FLOW");
        logger.debug(markerContent, "Debug log message");
        logger.info(markerContent, "Info log message");
        logger.error(markerContent, "Error log message");


//        logger.debug("Debug log message");
//        logger.info("Info log message");
//        logger.error("Error log message");
    }
}