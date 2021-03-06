package com.woundeddragons.securitystarter.web.api.common.response.composer;

import com.woundeddragons.securitystarter.web.api.common.response.BaseResponse;
import com.woundeddragons.securitystarter.web.api.common.response.ErrorsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractAuthenticationExceptionResponseComposer<T extends Exception> extends AbstractExceptionResponseComposer<T> {
    public AbstractAuthenticationExceptionResponseComposer(Class<T> wrappedExceptionClass) {
        super(wrappedExceptionClass);
    }

    protected ResponseEntity<BaseResponse> buildResponse(ErrorsEnum errorsEnum) {
        return this.build(HttpStatus.UNAUTHORIZED, errorsEnum);
    }
}