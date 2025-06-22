package com.pultyn.spring_jwt.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private ProblemDetail build(
            HttpStatusCode status,
            Exception ex,
            String description
    ) {
        ProblemDetail error = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        error.setProperty("description", description);
        return error;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleException(BadCredentialsException ex) {
        return build(HttpStatusCode.valueOf(401), ex,
                "Username or password invalid");
    }

    @ExceptionHandler(AccountStatusException.class)
    public ProblemDetail handleException(AccountStatusException ex) {
        return build(HttpStatusCode.valueOf(403), ex, "The account is locked");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleException(AccessDeniedException ex) {
        return build(
                HttpStatusCode.valueOf(403),
                ex,
                "You are not authorized to access this resource");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleException(ExpiredJwtException ex) {
        return build(
                HttpStatusCode.valueOf(401),
                ex,
                "The JWT token has expired");
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleException(SignatureException ex) {
        return build(
                HttpStatusCode.valueOf(401),
                ex,
                "The JWT token is invalid");
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleException(
            NotFoundException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), ex.getMessage());
        errorDetail.setProperty("path", request.getRequestURI());
        errorDetail.setProperty("description", "Resource not found");
        return errorDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ProblemDetail error = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed");

        error.setProperty("violations", errors);
        error.setProperty("path", request.getRequestURI());
        return error;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleException(DataIntegrityViolationException ex) {
        return build(
                HttpStatusCode.valueOf(403),
                ex,
                "Data integrity violated"
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleException(NoResourceFoundException ex) {
        return build(
                HttpStatusCode.valueOf(404),
                ex,
                "Resource not found"
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleException(IllegalArgumentException ex) {
        return build(
                HttpStatusCode.valueOf(400),
                ex,
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        return build(
                HttpStatusCode.valueOf(500),
                ex,
                "Unhandled server error"
        );
    }
}
