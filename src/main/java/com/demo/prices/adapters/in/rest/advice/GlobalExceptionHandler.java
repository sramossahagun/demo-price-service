package com.demo.prices.adapters.in.rest.advice;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demo.prices.adapters.in.rest.model.ErrorResponseDto;
import com.demo.prices.domain.consts.ErrorCode;
import com.demo.prices.domain.exception.DemoPriceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Procesa excepciones de negocio.
     *
     * @param exception excepción de negocio
     * @return respuesta de error
     */
    @ExceptionHandler(DemoPriceException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(
            final DemoPriceException exception) {

        return buildResponse(
                exception.getCodeStatus(),
                exception.getCode(),
                exception.getMessage(),
                exception.getDescription());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(
            final Exception exception) {

        log.error("Unexpected error: {}", exception.getMessage(), exception);

        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return buildResponse(
        		errorCode.getCodeStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                "Se ha producido un error inesperado durante el procesamiento de la petición.");
    }
    
    /**
     * Procesa errores de conversión de tipos.
     *
     * @param exception excepción producida
     * @return respuesta HTTP
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
            final MethodArgumentTypeMismatchException exception) {
    	
        return buildResponse(
        		ErrorCode.INVALID_INPUT_PARAMETER, 
        		String.format("El parámetro '%s' contiene un valor no válido.", exception.getName()));
    }
    
    
    /**
     * Parámetro obligatorio ausente.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {

        final ErrorCode error = ErrorCode.INVALID_INPUT_PARAMETER;

        return buildObjectResponse(
                error.getCodeStatus(),
                error.getCode(),
                error.getMessage(),
                String.format("El parámetro '%s' es obligatorio.", exception.getParameterName()));
    }
    
    private ResponseEntity<ErrorResponseDto> buildResponse(ErrorCode error, String description) {
    	return buildResponse(
                error.getCodeStatus(),
                error.getCode(),
                error.getMessage(),
                description);
    }
    
    private ResponseEntity<ErrorResponseDto> buildResponse(
            final int httpStatus,
            final String code,
            final String message,
            final String description) {
        return ResponseEntity
                .status(Optional.ofNullable(HttpStatus.resolve(httpStatus))
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .body(buildErrorResponse(code, message, description));
    }
    
    private ResponseEntity<Object> buildObjectResponse(
            final int httpStatus,
            final String code,
            final String message,
            final String description) {

    	return ResponseEntity
                .status(Optional.ofNullable(HttpStatus.resolve(httpStatus))
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .body(buildErrorResponse(code, message, description));
    }


    private ErrorResponseDto buildErrorResponse(
            final String code,
            final String message,
            final String description) {

        return new ErrorResponseDto()
                .code(code)
                .message(message)
                .description(description)
                .timestamp(LocalDateTime.now());
    }

}