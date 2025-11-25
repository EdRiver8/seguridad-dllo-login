package com.example.proyectoseguridaddllologin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura errores de validación y otros errores generales.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Maneja errores de validación de Bean Validation (JSR-380).
   * Se activa cuando una validación falla en un @Valid DTO.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    Map<String, Object> response = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    // Extraer todos los errores de validación
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Validation Failed");
    response.put("message", "Error de validación en los datos enviados");
    response.put("errors", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Maneja excepciones generales no capturadas específicamente.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Internal Server Error");
    response.put("message", ex.getMessage());

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
