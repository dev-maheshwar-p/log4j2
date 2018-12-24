package com.dev.logging.log4j2.simpleconfiguration;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LogPrinter {
    private Logger logger = LogManager.getLogger(this.getClass());

    public void printlog() {
        logger.debug("Debug log message");
        logger.info("Info log message");
        logger.error("Error log message");
    }
}