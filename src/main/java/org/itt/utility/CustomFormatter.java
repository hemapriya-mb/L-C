package org.itt.utility;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.time.LocalDateTime;

public class CustomFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        String action = record.getMessage();
        return String.format("%s: User ID: %d, Time: %s%n", action, record.getParameters()[0], LocalDateTime.now());
    }
}
