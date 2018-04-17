package es.nitaur.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.*;
import javax.servlet.http.HttpServletRequest;

/**
 * A builder for ExceptionDetail objects. This class facilitates the construction and population of ExceptionDetail
 * objects from an Exception and from REST service request data.
 *
 * @author Matt Warman
 */
public class ExceptionBuilder {

    /**
     * The ExceptionDetail object under construction.
     */
    private final transient ExceptionDetails exceptionDetails;

    /**
     * Constructs a new ExceptionDetailBuilder.
     */
    public ExceptionBuilder() {
        this.exceptionDetails = new ExceptionDetails();
    }

    /**
     * Invoke this method to obtain the ExceptionDetail object after using builder methods to populate it.
     *
     * @return An ExceptionDetail object.
     */
    public ExceptionDetails build() {
        return this.exceptionDetails;
    }

    /**
     * Populate the ExceptionDetail attributes with information from the Exception. Returns this ExceptionDetailBuilder
     * to chain method invocations.
     *
     * @param ex An Exception.
     * @return This ExceptionDetailBuilder object.
     */
    public ExceptionBuilder exception(final Exception ex) {
        if (ex != null) {
            this.exceptionDetails.setExceptionClass(ex.getClass().getName());
            this.exceptionDetails.setExceptionMessage(ex.getMessage());
        }
        return this;
    }

    /**
     * Populate the ExceptionDetail attributes with information from a HttpStatus. Returns this ExceptionDetailBuilder
     * to chain method invocations.
     *
     * @param status A HttpStatus.
     * @return This ExceptionDetailBuilder object.
     */
    public ExceptionBuilder httpStatus(final HttpStatus status) {
        if (status != null) {
            this.exceptionDetails.setStatus(status.value());
            this.exceptionDetails.setStatusText(status.getReasonPhrase());
        }
        return this;
    }

    /**
     * Populate the ExceptionDetail attributes with information from a WebRequest. Typically use either a WebRequest or
     * HttpServletRequest, but not both. Returns this ExceptionDetailBuilder to chain method invocations.
     *
     * @param request A WebRequest.
     * @return This ExceptionDetailBuilder object.
     */
    public ExceptionBuilder webRequest(final WebRequest request) {
        if (request instanceof ServletWebRequest) {
            final HttpServletRequest httpRequest = ((ServletWebRequest) request)
                    .getNativeRequest(HttpServletRequest.class);
            return httpServletRequest(httpRequest);
        }
        return this;
    }

    /**
     * Populate the ExceptionDetail attributes with information from a HttpServletRequest. Typically use either a
     * WebRequest or HttpServletRequest, but not both. Returns this ExceptionDetailBuilder to chain method invocations.
     *
     * @param request A HttpServletRequest.
     * @return This ExceptionDetailBuilder object.
     */
    public ExceptionBuilder httpServletRequest(final HttpServletRequest request) {
        if (request != null) {
            this.exceptionDetails.setMethod(request.getMethod());
            this.exceptionDetails.setPath(request.getServletPath());
        }
        return this;
    }
}