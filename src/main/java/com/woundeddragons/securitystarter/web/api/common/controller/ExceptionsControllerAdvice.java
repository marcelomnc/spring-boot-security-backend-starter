package com.woundeddragons.securitystarter.web.api.common.controller;

import com.woundeddragons.securitystarter.web.api.common.response.BaseResponse;
import com.woundeddragons.securitystarter.web.api.common.response.ResponseError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionsControllerAdvice {

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        // build a response body out of ex, and return that
        BaseResponse toRet = new BaseResponse();
        ex.getConstraintViolations().stream().forEach(constraintViolation -> {
            toRet.addResponseError(99, constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
        });

        return ResponseEntity.badRequest().body(toRet);
    }

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // build a response body out of ex, and return that
        BaseResponse toRet = new BaseResponse();
        ex.getBindingResult().getFieldErrors().forEach(objectError -> {
            ResponseError responseError = new ResponseError();
            responseError.setCode(100);
            responseError.setField(objectError.getField());
            responseError.setMessage(objectError.getDefaultMessage());
            toRet.addResponseError(responseError);
        });

        return ResponseEntity.badRequest().body(toRet);
    }

    // ... more such methods, one per exception type
}
