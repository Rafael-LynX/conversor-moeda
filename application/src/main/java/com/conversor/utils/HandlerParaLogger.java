package com.conversor.utils;

import java.util.logging.LogRecord;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

public class HandlerParaLogger extends ConsoleHandler {
    
    public HandlerParaLogger() {
        setOutputStream(System.out);
        setFormatter(new CustomFormatter());
    }
    
    static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            String color = getColorForLevel(record.getLevel());
            String reset = "\u001B[0m"; // Reset color

            return String.format("%s[%s] [%s] %s%s%n",
                color,
                new java.util.Date(record.getMillis()),
                record.getLevel().getName(),
                formatMessage(record),
                reset);
        }
        
        private String getColorForLevel(Level level) {
            if (level == Level.SEVERE) return "\u001B[31m";   // Vermelho
            if (level == Level.WARNING) return "\u001B[33m";  // Amarelo
            if (level == Level.INFO) return "\u001B[32m";     // Verde
            if (level == Level.CONFIG) return "\u001B[36m";   // Ciano
            return "\u001B[0m";                                // Reset (sem cor)
        }
    }
}
