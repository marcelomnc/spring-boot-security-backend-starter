package com.woundeddragons.securitystarter.web.api.v1.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ResponseError implements Serializable {
    private int code;
    private String message;
}
