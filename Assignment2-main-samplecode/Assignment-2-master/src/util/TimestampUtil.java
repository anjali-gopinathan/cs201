package util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimestampUtil {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
	private static final Instant startInstant = Instant.now();

	public static String getTimestamp() {
		// Naive timestamp approach. Works but not zeroed out.
	    return dateFormat.format(new Date()).substring(0, 11);
	}

	public static String getZeroTimestamp() {
		// Cooler timestamp approach!
		Duration duration = Duration.between(startInstant, Instant.now());
		return millisecondsToTimestamp(duration.toMillis());
	}

	private static String millisecondsToTimestamp(long millis) {
		// Calculate durations
		final long hours = TimeUnit.MILLISECONDS.toHours(millis);

		final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));

		final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

		final long ms = TimeUnit.MILLISECONDS.toMillis(millis)
				- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis));

		// Build formatted string
		return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, ms);
	}
}
