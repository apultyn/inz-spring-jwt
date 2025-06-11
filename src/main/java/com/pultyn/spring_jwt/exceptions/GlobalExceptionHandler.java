package com.pultyn.spring_jwt.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



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
                HttpStatusCode.valueOf(403),
                ex,
                "The JWT token has expired");
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

    @ExceptionHandler(InvalidDataException.class)
    public ProblemDetail handleException(InvalidDataException ex) {
        return build(
                HttpStatusCode.valueOf(400),
                ex,
                "Form contains invalid data");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleException(DataIntegrityViolationException ex) {
        return build(
                HttpStatusCode.valueOf(403),
                ex,
                "Data integrity violated"
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
