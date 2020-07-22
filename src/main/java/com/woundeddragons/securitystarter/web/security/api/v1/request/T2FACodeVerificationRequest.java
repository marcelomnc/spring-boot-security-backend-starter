package com.woundeddragons.securitystarter.web.security.api.v1.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class T2FACodeVerificationRequest implements Serializable {
    @NotEmpty
    @Size(min = 6, max = 6)
    @Max(999999)
    @Min(000000)
    private String t2FACode;
}
