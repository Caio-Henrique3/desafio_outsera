package br.com.caio.desafio_tecnico.exception;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CsvFileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCsvFileNotFound(
            CsvFileNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.error("CSV not found: {}", exception.getMessage(), exception);

        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(CsvParsingException.class)
    public ResponseEntity<ErrorResponse> handleCsvParsing(
            CsvParsingException exception,
            HttpServletRequest request
    ) {
        logger.error("CSV parsing error: {}", exception.getMessage(), exception);

        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        logger.error("Business error: {}", exception.getMessage(), exception);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        logger.error("Unexpected error", exception);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        return ResponseEntity.status(status).body(response);
    }

    public record ErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
    }

}
