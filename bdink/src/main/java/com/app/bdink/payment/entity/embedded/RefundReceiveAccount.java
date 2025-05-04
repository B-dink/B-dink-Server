package com.app.bdink.payment.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundReceiveAccount {

    @Column(length = 10, name = "refund_bank_code")
    private String bankCode;

    @Column(length = 20, name = "refund_account_number")
    private String accountNumber;

    @Column(length = 60, name = "refund_holder_name")
    private String holderName;
}