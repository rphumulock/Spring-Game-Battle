package demosecurity.utilities;

public enum QuerySelector {

    SELF("self"),
    USEID(""),
    FRAGMENT("datastar-fragment"),
    REDIRECT("datastar-redirect"),
    ERROR("datastar-error");

    private final String eventValue;

    QuerySelector(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getEventValue() {
        return this.eventValue;
    }
}
