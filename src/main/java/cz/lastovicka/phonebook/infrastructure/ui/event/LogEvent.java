package cz.lastovicka.phonebook.infrastructure.ui.event;

import com.google.common.base.Objects;

/**
 *
 * This event allows reporting special messages.
 *
 * @author Jan Lastovicka
 * @since 2019-01-24
 */
public final class LogEvent {

    /**
     * Creates new log event with INFO severity
     * @param message must not be null
     * @return never null
     * @throws IllegalArgumentException if message is null
     */
    public static LogEvent info(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null.");
        }

        return new LogEvent("INFO", message);
    }

    /**
     * Creates a new event with ERROR severity
     * @param message must not be null
     * @return never null
     * @throws IllegalArgumentException if message is null
     */
    public static LogEvent error(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null.");
        }

        return new LogEvent("ERROR", message);
    }

    //-------------------------------------------------------------------
    //INSTANCE SCOPE
    //-------------------------------------------------------------------

    private final String severity;
    private final String message;

    public LogEvent(String severity, String message) {
        this.severity = severity;
        this.message = message;
    }

    public String message() {
        return message;
    }

    public String severity() {
        return severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEvent logEvent = (LogEvent) o;
        return Objects.equal(severity, logEvent.severity) &&
                Objects.equal(message, logEvent.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(severity, message);
    }
}
