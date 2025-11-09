package com.bytestore.exception.handler;

import com.bytestore.dto.ErrorResponseDTO;
import com.bytestore.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ========== EXCEÇÕES DE RECURSOS NÃO ENCONTRADOS ==========

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductNotFound(
            ProductNotFoundException ex, HttpServletRequest request) {
        logger.warn("Produto não encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Produto não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderNotFound(
            OrderNotFoundException ex, HttpServletRequest request) {
        logger.warn("Pedido não encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Pedido não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {
        logger.warn("Usuário não encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Usuário não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ========== EXCEÇÕES DE RECURSOS DUPLICADOS ==========

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {
        logger.warn("Recurso duplicado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Recurso duplicado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ProductNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductNameExists(
            ProductNameAlreadyExistsException ex, HttpServletRequest request) {
        logger.warn("Nome de produto já existe: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Nome de produto já existe",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // ========== EXCEÇÕES DE ESTOQUE ==========

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientStock(
            InsufficientStockException ex, HttpServletRequest request) {
        logger.warn("Estoque insuficiente: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Estoque insuficiente",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(OrderCancelledDueToStockException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderCancelledDueToStock(
            OrderCancelledDueToStockException ex, HttpServletRequest request) {
        logger.warn("Pedido cancelado devido à falta de estoque: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Pedido cancelado - Estoque insuficiente",
                ex.getMessage(),
                request.getRequestURI(),
                "ORDER_CANCELLED_STOCK"
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    // ========== EXCEÇÕES DE PEDIDOS ==========

    @ExceptionHandler(OrderAlreadyPaidException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderAlreadyPaid(
            OrderAlreadyPaidException ex, HttpServletRequest request) {
        logger.warn("Pedido já pago: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Pedido já pago",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(OrderCancelledException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderCancelled(
            OrderCancelledException ex, HttpServletRequest request) {
        logger.warn("Pedido cancelado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Pedido cancelado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderAccessDenied(
            OrderAccessDeniedException ex, HttpServletRequest request) {
        logger.warn("Acesso negado ao pedido: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // ========== EXCEÇÕES DE VALIDAÇÃO ==========

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            ValidationException ex, HttpServletRequest request) {
        logger.warn("Erro de validação: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.warn("Erro de validação de argumentos: {}", ex.getMessage());
        
        List<ErrorResponseDTO.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDTO.FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Um ou mais campos estão inválidos",
                request.getRequestURI(),
                "VALIDATION_ERROR",
                fieldErrors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        logger.warn("Violação de constraint: {}", ex.getMessage());
        
        List<ErrorResponseDTO.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ErrorResponseDTO.FieldError(
                        getFieldName(violation),
                        violation.getMessage(),
                        violation.getInvalidValue()
                ))
                .collect(Collectors.toList());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Um ou mais campos estão inválidos",
                request.getRequestURI(),
                "CONSTRAINT_VIOLATION",
                fieldErrors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.warn("Tipo de argumento inválido: {}", ex.getMessage());
        
        String typeName = ex.getRequiredType() != null 
                ? ex.getRequiredType().getSimpleName() 
                : "válido";
        String message = String.format("O parâmetro '%s' deve ser do tipo %s", ex.getName(), typeName);
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Tipo de argumento inválido",
                message,
                request.getRequestURI(),
                "TYPE_MISMATCH"
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ========== EXCEÇÕES DE AUTENTICAÇÃO ==========

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthentication(
            com.bytestore.exception.AuthenticationException ex, HttpServletRequest request) {
        logger.warn("Erro de autenticação: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Erro de autenticação",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        logger.warn("Credenciais inválidas: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenciais inválidas",
                "Email ou senha incorretos",
                request.getRequestURI(),
                "BAD_CREDENTIALS"
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        logger.warn("Acesso negado: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado",
                "Você não tem permissão para realizar esta operação.",
                request.getRequestURI(),
                "ACCESS_DENIED"
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // ========== EXCEÇÕES GENÉRICAS ==========

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        logger.warn("Argumento inválido: {}", ex.getMessage());
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Argumento inválido",
                ex.getMessage(),
                request.getRequestURI(),
                "ILLEGAL_ARGUMENT"
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI(),
                "INTERNAL_ERROR"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {
        logger.error("Erro genérico: {}", ex.getMessage(), ex);
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI(),
                "INTERNAL_ERROR"
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private String getFieldName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        return path.substring(path.lastIndexOf('.') + 1);
    }
}

