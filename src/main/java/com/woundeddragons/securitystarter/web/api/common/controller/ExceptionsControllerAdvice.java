package com.woundeddragons.securitystarter.web.api.common.controller;

import com.woundeddragons.securitystarter.web.api.common.response.BaseResponse;
import com.woundeddragons.securitystarter.web.api.common.response.composer.ExceptionResponseComposerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsControllerAdvice {
    @Autowired
    private ExceptionResponseComposerManager exceptionResponseComposerManager;

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception ex) throws Exception {
        return this.exceptionResponseComposerManager.delegateResponseComposition(ex).orElseThrow(() -> ex);
    }
}