package com.example.sheduleApp.sheduleApp.exceptions;

import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.ElementCollection;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class CustomRestExceptionHandler  extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConsultationAlreadyExistInRangeException.class})
    public ResponseEntity<Object> handleConsultationAlreadyExistInRangeException(ConsultationAlreadyExistInRangeException ex){
        String errorMessage = "Consultation already exist in this time range";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }
    @ExceptionHandler({RoleParsingException.class})
    public ResponseEntity<Object> handleRoleParsingException(RoleParsingException ex){
        String errorMessage = "Error while parsing user's role, check your query parameters)";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }


    @ExceptionHandler({JsonProcessingException.class})
    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex){
        String errorMessage = "Error while parsing JSON, check your request body)";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }


    @ExceptionHandler({UserIsNotActivatedException.class})
    public ResponseEntity<Object> handleUserIsNotActivatedException(UserIsNotActivatedException ex){
        String errorMessage = "User is not activated, check your mailbox to activate your account";

        ApiError error = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({UserActivationException.class})
    public ResponseEntity<Object> handleUserAuthenticationException(UserActivationException ex){
        String errorMessage = "Invalid credentials";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex){
        String errorMessage = "Invalid credentials";

        ApiError error = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<Object> handleDisabledException(DisabledException ex){
        String errorMessage = "User is currently disabled";

        ApiError error = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({InvalidJwtAuthenticationException.class})
    public ResponseEntity<Object> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex){
        String errorMessage = "Incorrect credentials";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({ImpossibleActionForRoleException.class})
    public ResponseEntity<Object> handleImpossibleActionForRoleException(ImpossibleActionForRoleException ex){
        String errorMessage = "Impossible action for user with this role";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<Object> handelDateTimeParseException(DateTimeParseException ex){
        String errorMessage = "Incorrect date or time format";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler({ IncorrectIdException.class })
    public ResponseEntity<Object> handleIncorrectIdException(IncorrectIdException ex){
        String errorMessage = "Incorrect object id";

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorMessage);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
