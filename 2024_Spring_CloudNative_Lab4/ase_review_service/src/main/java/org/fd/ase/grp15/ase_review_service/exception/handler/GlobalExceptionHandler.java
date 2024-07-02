package org.fd.ase.grp15.ase_review_service.exception.handler;

import org.fd.ase.grp15.ase_review_service.exception.code.ReviewServiceErrorCode;
import org.fd.ase.grp15.ase_review_service.web.bind.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.fd.ase.grp15.ase_review_service.exception.code.ReviewServiceErrorCode.REV_RBT_NOT_FOUND;


@ControllerAdvice
public class GlobalExceptionHandler {

    public static Map<ReviewServiceErrorCode, HttpStatus> codeToStatusMap = Map.of(
            REV_RBT_NOT_FOUND, HttpStatus.NOT_FOUND
    );


    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        StringBuilder messageBuilder = new StringBuilder();
        e.getFieldErrors().forEach(fieldError -> {
            messageBuilder.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        });
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(messageBuilder.toString(), ReviewServiceErrorCode.ERR_BAD_REQUEST));
    }

    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage(), ReviewServiceErrorCode.ERR_BAD_REQUEST));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        ex.printStackTrace();
        Class<?> currentExceptionClazz = ex.getClass();
        while (currentExceptionClazz != null) {
            try {
                var handler = this.getClass().getDeclaredMethod("handle" + currentExceptionClazz.getSimpleName(), currentExceptionClazz);
                var rsp = handler.invoke(this, ex);
                return (ResponseEntity<ErrorResponse>) rsp;
            } catch (Exception ignored) {
            }
            currentExceptionClazz = currentExceptionClazz.getSuperclass();
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage(), ReviewServiceErrorCode.INTERNAL_SERVER_ERROR));
    }

}
