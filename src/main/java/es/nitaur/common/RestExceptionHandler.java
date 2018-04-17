package es.nitaur.common;

import org.slf4j.*;
import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;
import javax.persistence.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Object> handleNoResultException(final NoResultException ex, final WebRequest request) {
        final ExceptionDetails detail = new ExceptionBuilder().exception(ex).httpStatus(HttpStatus.NOT_FOUND).webRequest(request).build();
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(final EmptyResultDataAccessException ex, final WebRequest request) {
        final ExceptionDetails detail = new ExceptionBuilder().exception(ex).httpStatus(HttpStatus.NOT_FOUND).webRequest(request).build();
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(final Exception ex, final WebRequest request) {
        final ExceptionDetails detail = new ExceptionBuilder().exception(ex).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).webRequest(request).build();
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}