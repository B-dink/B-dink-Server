package com.app.bdink.payment.apple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class AppleReceipt {

    @JsonProperty("in_app")
    private List<InAppPurchase> inApp;
}
