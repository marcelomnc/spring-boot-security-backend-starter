package com.woundeddragons.securitystarter.web.api.v1.response;

import com.woundeddragons.securitystarter.web.api.common.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountDataResponse extends BaseResponse {
    private boolean t2FAEnabled;
    private String t2FAQRCodeImageURL;
}
