package com.woundeddragons.securitystarter.web.api.common.response.composer;

import com.woundeddragons.securitystarter.web.api.common.response.BaseResponse;
import com.woundeddragons.securitystarter.web.api.common.response.ErrorsEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExpiredJwtExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<ExpiredJwtException> {

    public ExpiredJwtExceptionResponseComposer() {
        super(ExpiredJwtException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(ExpiredJwtException e) {
        return this.buildResponse(ErrorsEnum.JWT_EXPIRED);
    }
}
