package com.woundeddragons.securitystarter.web.api.common.response.composer;

import com.woundeddragons.securitystarter.web.api.common.response.BaseResponse;
import com.woundeddragons.securitystarter.web.api.common.response.ErrorsEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class UsernameNotFoundExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<UsernameNotFoundException> {

    public UsernameNotFoundExceptionResponseComposer() {
        super(UsernameNotFoundException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(UsernameNotFoundException e) {
        return this.buildResponse(ErrorsEnum.USERNAME_NOT_FOUND);
    }
}
