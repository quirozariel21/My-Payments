package com.quiroz.mypayments.exception;

import com.quiroz.mypayments.exception.dto.ApiErrorDto;
import com.quiroz.mypayments.exception.dto.ErrorMessage;
import com.quiroz.mypayments.exception.dto.ValidationErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("MethodArgumentNotValidException: ", ex);
        BindingResult bindingResult = ex.getBindingResult();
        List<ValidationErrorDto> validationErrors = prepareValidationErrorDtos(bindingResult, Boolean.FALSE);
        ApiErrorDto apiErrorDto = prepareApiErrorDto(ex, ErrorMessage.BAD_REQUEST, validationErrors, status);
        return ResponseEntity.badRequest()
                             .body(apiErrorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleNotFound(NotFoundException ex) {
        ApiErrorDto apiErrorDto = ApiErrorDto.builder()
                                             .status(HttpStatus.NOT_FOUND.value())
                                             .message(ex.getMessage())
                                            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(apiErrorDto);
    }

    private List<ValidationErrorDto> prepareValidationErrorDtos(Errors errors,
                                                                boolean useCodeForMessageKey) {
        return errors.getFieldErrors().stream()
                .map(fieldError -> {
                    ValidationErrorDto.ValidationErrorDtoBuilder builder = ValidationErrorDto.builder();
                    builder.field(fieldError.getField().replace("Original", ""));
                    builder.code(fieldError.getCode());
                    if (useCodeForMessageKey) {
                        builder.message(fieldError.getCode());
                    } else {
                        builder.message(fieldError.getDefaultMessage());
                    }
                    if(fieldError.getRejectedValue() != null) {
                        builder.rejectedValue(fieldError.getRejectedValue().toString());
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    private ApiErrorDto prepareApiErrorDto(Exception ex, ErrorMessage errorMessage,
                                           List<ValidationErrorDto> validationErrors,
                                           HttpStatusCode httpStatus) {
        return ApiErrorDto.builder()
                .message(errorMessage.getKey())
                .status(httpStatus.value())
                .validationErrors(validationErrors)
                .build();
    }
}
