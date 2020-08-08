package com.woundeddragons.securitystarter.web.api.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseResponse implements Serializable {
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    private List<ResponseError> errors;

    public BaseResponse() {
        this.errors = new ArrayList<>();
    }

    public void addResponseError(ResponseError responseError) {
        this.errors.add(responseError);
    }

    public void addResponseError(int responseErrorCode, String responseErrorMessage) {
        ResponseError responseError = new ResponseError(responseErrorCode, responseErrorMessage);
        this.errors.add(responseError);
    }
}
