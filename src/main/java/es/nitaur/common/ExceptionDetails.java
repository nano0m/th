package es.nitaur.common;

public class ExceptionDetails {

    private long timestamp;
    private String method = "";
    private String path = "";
    private int status;
    private String statusText = "";
    private String exceptionClass = "";
    private String exceptionMessage = "";

    public ExceptionDetails() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public void setStatusText(final String statusText) {
        this.statusText = statusText;
    }

    public String getExceptionClass() {
        return this.exceptionClass;
    }

    public void setExceptionClass(final String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }

    public void setExceptionMessage(final String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
