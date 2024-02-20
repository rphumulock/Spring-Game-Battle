package demosecurity.utilities;

import java.time.Duration;

public class SSEEvent {
    private String id;
    private String event;
    private String[] data; // Assuming array for simplicity, could also use List<String>
    private Duration retry;
    private boolean skipMinBytesCheck;

    // Private constructor to force use of the Builder
    SSEEvent() {}

    // Getters (and possibly setters if mutability is desired)
    public String getId() { return id; }
    public String getEvent() { return event; }
    public String[] getData() { return data; }
    public Duration getRetry() { return retry; }
    public boolean isSkipMinBytesCheck() { return skipMinBytesCheck; }

    // Static Builder class
    public static class Builder {
        private SSEEvent currEvent = new SSEEvent();

        public Builder withId(String id) {
            currEvent.id = id;
            return this;
        }

        public Builder withEvent(String event) {
            currEvent.event = event;
            return this;
        }

        public Builder withData(String[] data) {
            currEvent.data = data;
            return this;
        }

        public Builder withRetry(Duration retry) {
            currEvent.retry = retry;
            return this;
        }

        public Builder withSkipMinBytesCheck(boolean skip) {
            currEvent.skipMinBytesCheck = skip;
            return this;
        }

        public SSEEvent build() {
            return currEvent;
        }
    }
}
