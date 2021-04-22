package server.util;

import java.util.zip.DataFormatException;

/**
 * Exception for malformed schedule entries
 */
public class ScheduleFormatException extends DataFormatException {
    public ScheduleFormatException(String message) {
        super(message);
    }
}
